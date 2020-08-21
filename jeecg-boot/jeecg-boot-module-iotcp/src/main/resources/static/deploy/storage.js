var storage = {
    name: 'iotcp',
    version: 1,
    objectStore: '',
    database: '',
    indexedDB: '',

    init: function (callback) {
        var _this = this;
        // console.log('storage init...');
        this.indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB;
        // console.log(this.indexedDB);
        var request = indexedDB.open(this.name, this.version);
        // console.log(request);
        request.onupgradeneeded = function (event) {
            console.log(event);
            var db = event.target.result;
            console.log(db);
            if (!db.objectStoreNames.contains('scene')) {
                objectStore = db.createObjectStore('scene', {
                    keyPath: 'id'
                });
            }
            if (!db.objectStoreNames.contains('device')) {
                objectStore = db.createObjectStore('device', {
                    keyPath: 'id'
                });
            }
//            if (!db.objectStoreNames.contains('work')) {
//                objectStore = db.createObjectStore('work', {
//                    keyPath: 'id'
//                });
//            }
        };
        request.onsuccess = function (event) {
            console.log('request.onsuccess...');
            _this.database = event.target.result;
            callback();
        };
        request.onerror = function (event) {
            console.error('IndexedDB'+ event);
        };
    },

    get: function (storeName, id, callback) {
        if (this.indexedDB === undefined) {
            return false;
        }
        var transaction = this.database.transaction([storeName], 'readwrite');
        var objectStore = transaction.objectStore(storeName);
        var request = objectStore.get(id);
        request.onsuccess = function (event) {
            callback(event.target.result);
        };
    },

    set: function (storeName, obj, callback) {
        if (this.indexedDB === undefined) {
            return false;
        }
        var start = performance.now();
        var transaction = this.database.transaction([storeName], 'readwrite');
        var objectStore = transaction.objectStore(storeName);
        var request = objectStore.put({
            id: obj.userData.modelFile,
            // obj : obj,
            obj: JSON.parse(JSON.stringify(obj))
        });
        request.onsuccess = function (event) {
            console.log('[' + /\d\d\:\d\d\:\d\d/.exec(new Date())[0] + ']', '已缓存到本地，耗时：' + (performance.now() - start).toFixed(2) + 'ms');
            callback('已缓存到本地，耗时：' + (performance.now() - start).toFixed(2) + 'ms');
        };
        request.onerror = function (event) {
            console.error(event);
        };
    },

    clear: function (storeName, callbeck) {
        if (this.indexedDB === undefined) {
            return false;
        }
        if (this.database === undefined)
            return;
        var transaction = this.database.transaction([storeName], 'readwrite');
        var objectStore = transaction.objectStore(storeName);
        var request = objectStore.clear();
        request.onsuccess = function (event) {
            callbeck(event.target.result);
        };
    },
};
