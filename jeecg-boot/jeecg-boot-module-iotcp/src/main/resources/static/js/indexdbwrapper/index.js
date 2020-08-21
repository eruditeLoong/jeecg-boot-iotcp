class DBWrapper {
    constructor(
        name,
        version,
        {onupgradeneeded, onversionchange = this._onversionchange} = {}
    ) {
        this._name = name;
        this._version = version;
        this._onupgradeneeded = onupgradeneeded;
        this._onversionchange = onversionchange;

        this._db = null;
    }

    get db() {
        return this._db;
    }

    static async deleteDatabase(name) {
        await new Promise((resolve, reject) => {
            const request = indexedDB.deleteDatabase(name);

            request.onerror = ({target}) => {
                reject(target.error);
            };

            request.onblocked = () => {
                reject(new Error("Delete blocked"));
            };

            request.onsuccess = () => {
                resolve();
            };
        });
    }

    async open() {
        if (this._db) return;
        this._db = await new Promise((resolve, reject) => {
            let openRequestTimedOut = false;
            setTimeout(() => {
                openRequestTimedOut = true;
                reject(new Error("The open request was blocked and timed out"));
            }, this.OPEN_TIMEOUT);
            const openRequest = indexedDB.open(this._name, this._version);

            openRequest.onerror = () => reject(openRequest.error);

            openRequest.onupgradeneeded = evt => {
                if (openRequestTimedOut) {
                    openRequest.transaction.abort();
                    evt.target.result.close();
                } else if (this._onupgradeneeded) {
                    this._onupgradeneeded(evt);
                }
            };

            openRequest.onsuccess = ({target}) => {
                const db = target.result;

                if (openRequestTimedOut) {
                    db.close();
                } else {
                    db.onversionchange = this._onversionchange.bind(this);
                    resolve(db);
                }
            };
        });
        return this;
    }

    async getKey(storeName, query) {
        return (await this.getAllKeys(storeName, query, 1))[0];
    }

    async getAll(storeName, query, count) {
        return await this.getAllMatching(storeName, {
            query,
            count
        });
    }

    async getAllKeys(storeName, query, count) {
        return (await this.getAllMatching(storeName, {
            query,
            count,
            includeKeys: true
        })).map(({key}) => key);
    }

    async getAllMatching(
        storeName,
        {index, query = null, direction = "next", count, includeKeys} = {}
    ) {
        return await this.transaction([storeName], "readonly", (txn, done) => {
            const store = txn.objectStore(storeName);
            const target = index ? store.index(index) : store;
            const results = [];

            target.openCursor(query, direction).onsuccess = ({target}) => {
                const cursor = target.result;

                if (cursor) {
                    const {primaryKey, key, value} = cursor;
                    results.push(
                        includeKeys
                            ? {
                                primaryKey,
                                key,
                                value
                            }
                            : value
                    );

                    if (count && results.length >= count) {
                        done(results);
                    } else {
                        cursor.continue();
                    }
                } else {
                    done(results);
                }
            };
        });
    }

    async transaction(storeNames, type, callback) {
        await this.open();
        return await new Promise((resolve, reject) => {
            const txn = this._db.transaction(storeNames, type);

            txn.onabort = ({target}) => reject(target.error);

            txn.oncomplete = () => resolve();

            callback(txn, value => resolve(value));
        });
    }

    async _call(method, storeName, type, ...args) {
        const callback = (txn, done) => {
            txn.objectStore(storeName)[method](...args).onsuccess = ({target}) => {
                done(target.result);
            };
        };

        return await this.transaction([storeName], type, callback);
    }

    _onversionchange() {
        this.close();
    }

    close() {
        if (this._db) {
            this._db.close();

            this._db = null;
        }
    }
}

DBWrapper.prototype.OPEN_TIMEOUT = 2000;

(function () {
    const methodsToWrap = {
        readonly: ["get", "count", "getKey", "getAll", "getAllKeys"],
        readwrite: ["add", "put", "clear", "delete"]
    };

    for (const [mode, methods] of Object.entries(methodsToWrap)) {
        for (const method of methods) {
            if (method in IDBObjectStore.prototype) {
                DBWrapper.prototype[method] = async function (storeName, ...args) {
                    return await this._call(method, storeName, mode, ...args);
                };
            }
        }
    }
})();

export default DBWrapper;
