# IndexDBWrapper

#### 介绍

封装的 IndexDB 类包，采用 asyn/await，方便使用。

#### 安装教程

```
npm i indexdbwrapper
```

#### 使用说明

构造器

```js
import IndexDBWrapper from "indexdbwrapper";

var db = new IndexDBWrapper("数据库名", 数据库版本, {
  onupgradeneeded: f => f,
  onversionchange: f => f
});
```

```
属性：

- db：获取原生 IDBDatabase 对象。


方法：

- add：使用方式与原生方法一致，对返回结果做了 Promise 化。

- clear：使用方式与原生方法一致，对返回结果做了 Promise 化。

- count：使用方式与原生方法一致，对返回结果做了 Promise 化。

- delete：使用方式与原生方法一致，对返回结果做了 Promise 化。

- get：使用方式与原生方法一致，对返回结果做了 Promise 化。

- put：使用方式与原生方法一致，对返回结果做了 Promise 化。

- close()：关闭数据库。

- open()：打开数据库。

- deleteDatabase(name)：删除数据库。

- getAll(storeName, query, count)：指定条件获取所有数据。

- getAllKeys(storeName, query, count)：指定条件获取所有 key。

- getKey(storeName, query)：指定条件获取相应 key。

- getAllMatching(storeName, { index, query = null, direction = "next", count, includeKeys })：获取指定匹配结果。

- transaction(storeNames, type, callback(txn, done))：callback 封装化的事务。

```

部分代码参考 [workbox](https://github.com/GoogleChrome/workbox)。
