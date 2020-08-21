var device = {
    // 子对象数组
    childObjs: [],

    /**
     * 初始化已部署的设备 deviceFrame页面调用
     */
    initDeviceOnSence: function (sceneId) {
        $.getJSON("sceneController.do?getDeployDeviceListBySceneId&sceneId=" + sceneId, function (result) {
            if (result.success) {
                var deviceList = result.obj || [];
                // 循环放置模型
                for (var i = 0; i < deviceList.length; i++) {
                    device.initDeviceObj(deviceList[i]);
                }
            } else {
                alert("请求失败");
            }
        });
    },

    initDeviceObj: function (deviceObj) {
        storage.get('device', deviceObj.modelFile, function (result) {
            if (result == null) { // 数据库没有，远程加载
                device.loadDevice(deviceObj);
            } else {
                var objLoader = new THREE.ObjectLoader();
                var obj = objLoader.parse(result.obj);
                obj.scale.multiplyScalar(1); // 缩放模型大小
                obj.uuid = deviceObj.id;
                obj.name = deviceObj.name;
                obj.userData = {
                    id: deviceObj.id,
                    confBy: deviceObj.confBy,
                    entity: deviceObj.entity,
                    position: deviceObj.threeData,
                    code: deviceObj.code,
                    parentBy: deviceObj.parentBy,
                    modelFile: deviceObj.modelFile,
                    type: deviceObj.type,
                }
                obj.traverse(function (child) {
                    if (child.isMesh) {
                        child.castShadow = true;
                        child.receiveShadow = true;
                    }
                });
                loader.setObjPosition(obj);
                scene.add(label.createLabel(obj));
            }
        });
    },

    loadDevice: function (deviceObj) {
        loader.loadFile('img/server/' + deviceObj.modelFile, function (obj) {
            obj.scale.multiplyScalar(1); // 缩放模型大小
            obj.uuid = deviceObj.id;
            obj.name = deviceObj.name;
            obj.userData = {
                id: deviceObj.id,
                confBy: deviceObj.confBy,
                entity: deviceObj.entity,
                position: deviceObj.threeData,
                code: deviceObj.code,
                modelFile: deviceObj.modelFile,
                type: deviceObj.type,
            }
            loader.setObjPosition(obj);
            scene.add(label.createLabel(obj));
            storage.set("device", obj, function () {
            });
        });
    },

    /** 放置设备模型 */
    deployDevice: function (deviceObj) {
        isDeploy = true;
        storage.get('device', deviceObj.userData.modelFile, function (event) {
            var result = event.target.result;
            if (result == null) { // 数据库没有，远程加载
                var loader = getLoaderByUrlName(deviceObj.modelFile);
                loader.load('${webRoot}/img/server/' + deviceObj.modelFile, function (obj) {
                    rollOverMesh = obj.clone();
                    rollOverMesh.uuid = deviceObj.id;
                    rollOverMesh.name = deviceObj.name;
                    rollOverMesh.position.set(0, 0, 0);
                    rollOverMesh.scale.multiplyScalar(1); // 缩放模型大小
                    rollOverMesh.userData = {
                        confBy: deviceObj.confBy,
                        entity: deviceObj.entity,
                        id: deviceObj.id,
                        code: deviceObj.code,
                        deviceBy: deviceObj.deviceBy,
                        parentBy: deviceObj.parentBy,
                        position: deviceObj.threeData,
                        modelFile: deviceObj.modelFile,
                        type: deviceObj.type
                    }
                    scene.add(rollOverMesh);
                    /* scene.add(label.createLabel(rollOverMesh)); */
                    storage.set("device", rollOverMesh, function () {
                    });
                });
            } else {
                var loader = new THREE.ObjectLoader();
                var obj = loader.parse(result.obj);
                rollOverMesh = obj.clone();
                rollOverMesh.uuid = deviceObj.id;
                rollOverMesh.name = deviceObj.name;
                rollOverMesh.position.set(0, 0, 0);
                rollOverMesh.scale.multiplyScalar(1); // 缩放模型大小
                rollOverMesh.userData = {
                    confBy: deviceObj.confBy,
                    entity: deviceObj.entity,
                    id: deviceObj.id,
                    code: deviceObj.code,
                    deviceBy: deviceObj.deviceBy,
                    parentBy: deviceObj.parentBy,
                    position: deviceObj.threeData,
                    modelFile: deviceObj.modelFile,
                    type: deviceObj.type
                }
                scene.add(rollOverMesh);
                /* scene.add(label.createLabel(rollOverMesh)); */
            }
        });
    },

    getChildObjs: function (obj) {
        var pobj = obj.children;
        for (var i in pobj) {
            var o = pobj[i];
            if (o.type == 'Mesh') {
                this.childObjs.push(o);
            } else {
                this.getChildObjs(o);
            }
        }
        return this.childObjs;
    },

    getParentGroup: function (obj) {
        var pobj = obj.parent;
        if (pobj.type == 'Scene') {
            return obj;
        } else {
            return this.getParentGroup(pobj);
        }
    },

    checkCodeUniq: function (obj) {
        var objs = objects || [];
        for (var i in objs) {
            // 比较模型名称相同
            if (obj.name == objs[i].name) {
                if (obj.code == objs[i].userData.code) {
                    return true;
                }
            }
        }
        return false;
    }
}