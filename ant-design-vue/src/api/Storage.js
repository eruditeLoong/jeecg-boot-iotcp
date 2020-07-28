/**
 * @author mrdoob / http://mrdoob.com/
 */

var Storage = function () {
    var indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB

    if (indexedDB === undefined) {
        console.warn('Storage: IndexedDB not available.')
        return {
            init: function () {},
            get: function () {},
            set: function () {},
            clear: function () {}
        }
    }

    var name = 'iotcp-three'
    var version = 1

    var database

    return {

        init: function (callback) {
            var request = indexedDB.open(name, version)
            request.onupgradeneeded = function (event) {
                var db = event.target.result

                if (db.objectStoreNames.contains('states') === false) {
                    let store = db.createObjectStore('states', {
                        keyPath: 'key'
                    })
                    store.createIndex('keyIndex', 'key', {
                        unique: true
                    });

                }
            }
            request.onsuccess = function (event) {
                database = event.target.result

                callback()
            }
            request.onerror = function (event) {
                console.error('IndexedDB', event)
            }
        },

        get: function (id, callback) {
            var transaction = database.transaction(['states'], 'readwrite')
            var objectStore = transaction.objectStore('states')
            var request = objectStore.get(id)
            request.onsuccess = function (event) {
                callback(event.target.result)
            }
        },

        getBig: function (id, callback) {
            let bigObj = '';
            var transaction = database.transaction(['states'], 'readwrite')
            var objectStore = transaction.objectStore('states')
            console.log(objectStore)
            let request = objectStore.index("keyIndex").openCursor();

            request.onsuccess = e => {
                // console.log('遍历成功', e);
                console.log(request)
                if (request.result) {
                    let cursor = request.result;
                    if (cursor.key.indexOf(id) > -1) {
                        bigObj += cursor.key;
                        console.log(bigObj)
                    }
                    cursor.continue();
                } else {
                    console.log("数据为空");
                }
            };
            request.onerror = e => {
                console.log("遍历失败", e);
            };
            return (bigObj)
        },

        set: function (key, value) {
            console.log('缓存对象', key)

            var start = performance.now()

            var transaction = database.transaction(['states'], 'readwrite')
            var objectStore = transaction.objectStore('states')

            let request1 = objectStore.put({
                'key': key,
                'obj': value
            })
            request1.onsuccess = function () {
                console.log('保存[' + key + ']成功. 用时：' + (performance.now() - start).toFixed(2) + 'ms')
            }
            request1.onerror = function (err) {
                console.log(err.target.error.message)
            }
        },

        setBig: function (key, value) {
            let len = value.length
            console.log('数据大小', len)
            if (len < 133000000) {
                this.set(key, value)
            } else {
                let size = 133000000
                let count = Math.ceil(len / size)
                console.log('分段数', count)
                for (let i = 0; i < count; i++) {
                    if (i === 0) {
                        console.log(i, '->', i, size)
                        this.set(key + '-' + i, value.substring(i, size))
                    } else if (i > 0 && i < (count - 1)) {
                        console.log(i, '->', (i * size) + 1, (i + 1) * size)
                        this.set(key + '-' + i, value.substring((i * size) + 1, (i + 1) * size))
                    } else {
                        console.log(i, '->', (i * size) + 1, len - 1)
                        this.set(key + '-' + i, value.substring((i * size) + 1, len - 1))
                    }
                }
            }
        },

        clear: function () {
            if (database === undefined) return

            var transaction = database.transaction(['states'], 'readwrite')
            var objectStore = transaction.objectStore('states')
            var request = objectStore.clear()
            request.onsuccess = function () {
                console.log('[' + /\d\d\:\d\d\:\d\d/.exec(new Date())[0] + ']', 'Cleared IndexedDB.')
            }
        }

    }
}

export {
    Storage
}
