var sceneObj = {
    conf : {},

    resetScene : function() {
        var _this = this;
        new TWEEN.Tween(orbit.target).to({
            x : 0,
            y : 0,
            z : 0
        }, 500).start();
        new TWEEN.Tween(camera.position).to({
            x : 1000,
            y : 500,
            z : 1000
        }, 500).start();

        // console.log(_this.conf);
        orbit.autoRotate = _this.conf.isAutoRotate; // 自动旋转
        _this.statsDisplay(_this.conf.isStatsDisplay); // 性能控件
        // _this.shadowDisplay(_this.conf.isShadowDisplay); // 阴影显示
        deviceRelation.display(_this.conf.isDeviceRelation); // 设备关系
        deviceStatus.display(_this.conf.isDeviceStatus); // 设备统计
        label.display(_this.conf.isLabelDisplay); // 设备标签
        userPosition.display(_this.conf.isUserPosition); // 人员定位
        // 用toolBarBottom元素来判断是部署页面还是展示页面
        if (document.getElementById('toolBarBottom')) {
            scenePanel.display(true); // 设备列表
        } else {
            devicePanel.display(_this.conf.isDevicePanel); // 设备列表
        }
    },

    /* 阴影显示 */
    shadowDisplay : function(flag) {
        if (flag) {
            for ( var i in objects) {
                var object = objects[i];
                object.traverse(function(child) {
                    if (child.isMesh) {
                        child.castShadow = true;
                        child.receiveShadow = true;
                    }
                });
            }
        } else {
            for ( var i in objects) {
                var object = objects[i];
                object.traverse(function(child) {
                    if (child.isMesh) {
                        child.castShadow = false;
                        child.receiveShadow = false;
                    }
                });
            }
        }
    },

    /* 性能控件 */
    statsDisplay : function(flag) {
        if (flag) {
            $('#stats').show();
        } else {
            $('#stats').hide();
        }
    },

    initScene : function(scene) {
        var _this = this;
        // console.log("initScene...." + scene);
        if (scene) {
            _this.initInstanceScene(scene);
            _this.conf = scene;
            _this.initGUI(scene);
        } else {
            $.getJSON("sceneController.do?goDefaultScene", function(result) {
                if (result.success) {
                    var scene_ = result.obj;
                    _this.initInstanceScene(scene_);
                    _this.conf = scene_;
                    _this.initGUI(scene_);
                }
            });
        }
    },

    /* 初始化应用场景 */
    initInstanceScene : function(scene_) {
        // document.getElementById("container").innerHTML = "";
        while (objects.length > 0) {
            var obj = device.getParentGroup(objects[0]);
            objects.splice(objects.indexOf(obj), 1);
            scene.remove(obj);
        }
        // device-label
        $('.device-label').each(function(i, o) {
            // alert(i);
            $(o).remove();
        });
        // 加载新场景
        var objUrl = scene_.modelFile3D;
        var userData = {
            id : scene_.id,
            name : scene_.name,
            modelFile : objUrl.lastIndexOf(',') != -1 ? objUrl.substring(0, objUrl.length - 1) : objUrl,
            type : 'scene',
            position : scene_.threeData
        }
        // 用toolBarBottom元素来判断是部署页面还是展示页面
        if (document.getElementById('deployBar')) {
            // 如果已经有panel不再创建
            if (!document.getElementById("scenePanel")) {
                scenePanel.create(scene_.id);
            }
        } else {
            devicePanel.create(scene_.id);
            alarmPanel.create(scene_.id);
            deviceStatus.create(scene_.id);
        }
        this.initSceneObj(userData);
    },

    initSceneObj : function(userData) {
        // 查询数据库中是否有场景数据
        // console.log(userData.modelFile);
        var models = userData.modelFile.split(",") || [];
        // console.log(models);
        for (let i = 0; i < models.length; i++) {
            // console.log(models[i]);
            if (models[i].length < 1)
                return;
            storage.get('scene', models[i], function(result) {
                if (result == null) { // 数据库没有，远程加载
                    // 复制一份userData
                    var userData1 = JSON.parse(JSON.stringify(userData));
                    // 重新设置模型路径，单个模型
                    userData1.modelFile = models[i];
                    sceneObj.loadScene(userData1);
                } else {
                    var objectLoader = new THREE.ObjectLoader();
                    var obj = objectLoader.parse(result.obj);
                    // obj.traverse(function (child) {
                    // if (child.isMesh) {
                    // child.castShadow = true;
                    // child.receiveShadow = true;
                    // }
                    // });
                    loader.setObjPosition(obj);
                }
            });
        }
        // 加载设备
        device.initDeviceOnSence(userData.id);
    },

    loadScene : function(userData) {
        loader.loadFile('img/server/' + userData.modelFile, function(obj) {
            obj.scale.multiplyScalar(1); // 缩放模型大小
            obj.position.set(0, 0, 0);
            obj.userData = userData;
            obj.uuid = userData.id;
            loader.setObjPosition(obj);
            // 加载设备
            device.initDeviceOnSence(userData.id);
            storage.set("scene", obj, function(result) {
            });
        });
    },

    initGUI : function(scene_) {
        var _this = this;
        _this.resetScene();
        var param = {
            "localStorageClear" : function() {
                $.dialog.confirm("您确定要清除场景缓存数据吗？", function(data) {
                    storage.clear('scene', function() {
                        alert('成功清除场景缓存数据！');
                    });
                    storage.clear('device', function() {
                        alert('成功清除设备缓存数据！');
                    });
                }, function() {
                });
            },
            "resetScene" : function() {
                _this.resetScene();
            },
            "autoRotate" : scene_.isAutoRotate, // 自动旋转
            'deviceRelation' : scene_.isDeviceRelation, // 设备关系
            'devicePanel' : scene_.isDevicePanel, // 设备列表
            'alarmPanel' : false, // 报警数据列表
            'labelDisplay' : scene_.isLabelDisplay, // 设备标签
            'statsDisplay' : scene_.isStatsDisplay, // 性能控件
            'deviceStatus' : scene_.isDeviceStatus, // 设备统计
            'userPosition' : scene_.isUserPosition,
            // 'shadowDisplay': scene_.isShadowDisplay, // 阴影
            "ambientLight" : ambientLight.color.getHex(),
            "directionalLight" : directionalLight.color.getHex(),
        };
        gui = new dat.GUI();
        gui.domElement.style = 'z-index:1000;';
        gui.close();
        gui.add(param, 'localStorageClear').name("清除缓存");
        gui.add(param, 'resetScene').name("还原场景");
        gui.add(param, 'autoRotate').name('自动旋转').onChange(function(val) {
            orbit.autoRotate = val;
        });
        gui.add(param, 'deviceRelation').name("设备关系").onChange(function(val) {
            deviceRelation.display(val);
        });
        gui.add(param, 'devicePanel').name('设备列表').onChange(function(val) {
            devicePanel.display(val);
        });
        gui.add(param, 'alarmPanel').name('报警列表').onChange(function(val) {
            alarmPanel.display(val);
        });
        gui.add(param, 'labelDisplay').name('标签显示').onChange(function(val) {
            label.display(val);
        });
        gui.add(param, 'statsDisplay').name('性能控件').onChange(function(val) {
            _this.statsDisplay(val);
        });
        gui.add(param, 'deviceStatus').name('设备统计').onChange(function(val) {
            deviceStatus.display(val);
        });
        gui.add(param, 'userPosition').name('人员定位').onChange(function(val) {
            userPosition.display(val);
        });

        // gui.add(param, 'shadowDisplay').name('显示阴影').onChange(function (val)
        // {
        // _this.shadowDisplay(val);
        // });

        var light = gui.addFolder('灯光');
        light.close();
        light.addColor(param, 'ambientLight').name('环境光源').onChange(function(val) {
            ambientLight.color.setHex(val);
            render();
        });

        light.addColor(param, 'directionalLight').name('平行光源').onChange(function(val) {
            directionalLight.color.setHex(val);
            render();
        });
    }
};
