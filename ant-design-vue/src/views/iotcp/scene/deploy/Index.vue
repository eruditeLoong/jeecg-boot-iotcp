<template>
    <div>
        <div id="container">
            <div class="load-mark" v-show="progressPercent <= 100">
                <div class="progress-box">
                    <div class="title">正在加载模型</div>
                    <a-progress :percent="progressPercent" :width="80" class="progress" type="circle" />
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { getAction } from '@api/manage';

import * as THREE from 'three';
import { CSS2DRenderer } from 'three/examples/jsm/renderers/CSS2DRenderer';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { TransformControls } from 'three/examples/jsm/controls/TransformControls';
import Stats from 'three/examples/jsm/libs/stats.module';
import { GUI } from 'three/examples/jsm/libs/dat.gui.module';

import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { DRACOLoader } from 'three/examples/jsm/loaders/DRACOLoader';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader';

import { Storage } from '@api/Storage';

export default {
    name: 'SceneDeploy',
    components: {},
    data() {
        return {
            title: '操作',
            visible: false,
            confirmLoading: false,
            fullscreen: true,
            switchFullscreen: true,

            progressPercent: 0,

            ambientLight: null,
            directionalLight: null,

            camera: null,
            scene: null,
            renderer: null,
            labelRenderer: null,
            mesh: null,
            container: null,
            orbit: null,
            control: null,
            mapControls: null,
            guiControls: null,
            stats: null,
            gui: null,
            storage: null,

            sceneConfig: {},
            schemeConfig: {},

            url: {
                getSceneConfig: '/scene/scene/getSceneConfig',
                getSchemeConfig: ''
            }
        };
    },
    onload() {
        alert(1);
    },
    created() {
        // alert(3)
    },
    methods: {
        aaaa(percent, successPercent) {
            if (percent < 100) {
                return '已加载<br>' + percent + '%';
            } else {
                return '加载完成<br>' + percent + '%';
            }
        },
        init: function() {
            this.container = document.getElementById('container');

            this.initRender();
            this.initCamera();
            this.initScene();
            this.initLight();
            this.initControls();
            this.initEvent();
            this.initStats();
            this.initGUI();
            this.initStorage();

            let grid = new THREE.GridHelper(100000, 50, 0x555555, 0xaaaaaa);
            grid.material.opacity = 0.6;
            grid.material.transparent = true;
            this.scene.add(grid);

            //坐标轴辅助
            let axes = new THREE.AxisHelper(100000);
            this.scene.add(axes);

            this.animation();
        },
        //1.渲染器
        initRender() {
            this.renderer = new THREE.WebGLRenderer();
            this.renderer.setClearColor(new THREE.Color(255, 255, 255));
            this.renderer.setPixelRatio(window.devicePixelRatio);
            this.renderer.setSize(this.container.clientWidth, this.container.clientHeight);
            this.renderer.shadowMap.enabled = true;
            this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
            this.container.appendChild(this.renderer.domElement);

            this.labelRenderer = new CSS2DRenderer();
            this.labelRenderer.setSize(this.container.clientWidth, this.container.clientHeight);
            this.labelRenderer.domElement.style.position = 'absolute';
            this.labelRenderer.domElement.style.top = 0;
            this.container.appendChild(this.labelRenderer.domElement);
        },
        // 2.场景
        initScene() {
            this.scene = new THREE.Scene();
            this.scene.background = new THREE.Color(0xf8f8f8);
        },
        // 3.相机
        initCamera() {
            this.camera = new THREE.PerspectiveCamera(45, this.container.clientWidth / this.container.clientHeight, 1, 10000);
            this.camera.position.set(100, 50, 100);
            this.camera.lookAt(0, 0, 0);
        },
        //4.事件
        initEvent() {
            // 添加鼠标点击事件，捕获点击时当前选中的物体
            window.addEventListener('resize', this.windowResize, false);
            // container.addEventListener('mousemove', onDocumentMouseMove, false);
            // container.addEventListener("mousedown", onDocumentMouseDown, false);
            // container.addEventListener("mouseup", onDocumentMouseUp, false);
        },
        //5.控制
        initControls() {
            const self = this;
            this.orbit = new OrbitControls(this.camera, this.labelRenderer.domElement);
            // orbit = new THREE.OrbitControls(camera, renderer.domElement);
            this.orbit.enableDamping = false;
            this.orbit.dampingFactor = 0;
            this.orbit.enableZoom = true;
            this.orbit.autoRotate = false;
            this.orbit.minDistance = 5;
            this.orbit.maxDistance = 100000;
            //是否开启右键拖拽
            this.orbit.enablePan = true;
            this.orbit.update();
            this.orbit.addEventListener('change', this.render);
            this.scene.add(this.orbit);

            this.control = new TransformControls(this.camera, this.labelRenderer.domElement);
            this.control.addEventListener('change', this.render);

            this.control.addEventListener('dragging-changed', function(event) {
                console.log(event.value ? '开始拖动' : '拖动结束');
                self.orbit.enabled = !event.value;
            });
            this.scene.add(this.control);
        },

        //6.光源
        initLight() {
            /* scene.add(new THREE.AmbientLight(0xaaaaaa));
                                      light = new THREE.DirectionalLight(0xffffff, 0.6);
                                      light.position.set(0, 200, 0);
                                      scene.add(light); */
            this.ambientLight = new THREE.AmbientLight(0xaaaaaa);
            this.ambientLight.position.set(0, 200, 0);
            this.scene.add(this.ambientLight); // 环境光源

            this.directionalLight = new THREE.DirectionalLight(0xffffff, 1); // 平行光源 例如太阳光
            // directionalLight.castShadow = true;
            this.directionalLight.position.set(500, 2000, 1000);
            this.directionalLight.position.multiplyScalar(1.3);
            this.directionalLight.castShadow = true;
            this.directionalLight.shadow.mapSize.width = 1024;
            this.directionalLight.shadow.mapSize.height = 1024;
            let d = 300;
            this.directionalLight.shadow.camera.left = -d;
            this.directionalLight.shadow.camera.right = d;
            this.directionalLight.shadow.camera.top = d;
            this.directionalLight.shadow.camera.bottom = -d;
            this.directionalLight.shadow.camera.far = 100000;
            this.scene.add(this.directionalLight);
        },
        // 7.性能工具
        initStats() {
            this.stats = new Stats();
            this.stats.domElement.style.position = 'absolute';
            this.stats.domElement.style.top = '0px';
            this.container.appendChild(this.stats.domElement);
        },
        /* 8.调试插件 */
        initGUI() {
            let params = {
                enableWind: true,
                showBall: false,
                togglePins: false
            };
            this.gui = new GUI();
            this.gui.domElement.style.position = 'absolute';
            this.gui.domElement.style.top = '0px';
            this.gui.domElement.style.right = '0px';
            this.container.appendChild(this.gui.domElement);

            this.gui.add(params, 'enableWind').name('Enable wind');
            this.gui.add(params, 'showBall').name('Show ball');
            this.gui.add(params, 'togglePins').name('Toggle pins');
        },
        initStorage() {
            const self = this;
            this.storage = new Storage();
            this.storage.init(function() {
                console.log('storage init end');

                self.getSceneConfig();
            });
        },

        getSceneConfig(sceneId) {
            const self = this;
            getAction(this.url.getSceneConfig, { id: sceneId }).then(res => {
                console.log('场景配置：', res);
                if (res.success) {
                    self.sceneConfig = res.result;
                    console.log(self.sceneConfig);
                    self.loadObject()
                }
            });
        },

        loadObject(){
            let modelFiles = this.sceneConfig.modelFiles.split(',');
            for (let i in modelFiles) {
                // 先从缓存中读取object

                console.log(this.storage.getBig(modelFiles[i]))

                let dbObj = this.storage.get(modelFiles[i], res=>{
                    console.log('缓存查询结果：', dbObj)
                    if(dbObj == undefined){
                        console.log('服务端加载模型：', modelFiles[i])
                        let type = modelFiles[i].substring(modelFiles[i].lastIndexOf('.') + 1).toLowerCase();
                        console.log(type);
                        // 根据文件类型加载场景模型
                        switch (type) {
                            case 'fbx':
                                this.loadFbx(modelFiles[i]);
                                break;
                            case 'json':
                                // this.loadJson(modelFiles[i])
                                break;
                            case 'obj':
                                // this.loadObj(modelFiles[i])
                                break;
                            case 'gltf':
                                // this.loadDracoGltf(modelFiles[i])
                                break;
                            default:
                                break;
                        }
                    } else{
                        console.log('浏览器缓存加载模型：', modelFiles[i])
                        this.scene.add(JSON.parse(dbObj))
                    }
                })
            }
        },

        // 加载压缩的gltf文件
        loadDracoGltf(file) {
            const self = this;
            let url = window._CONFIG['staticDomainURL'] + '/' + file;
            // 创建加载器
            let gltfLoader = new GLTFLoader();
            const dracoLoader = new DRACOLoader();
            dracoLoader.setDecoderPath('/public/static/js/draco/gltf/');
            dracoLoader.preload();
            gltfLoader.setDRACOLoader(dracoLoader);
            // 然后直接加载模型即可
            gltfLoader.load(url, object => self.initSceneObj(file, object.scene), self.onProgress);
        },
        loadObj(file) {
            let url = window._CONFIG['staticDomainURL'] + '/' + file;
        },
        loadJson(file) {
            let url = window._CONFIG['staticDomainURL'] + '/' + file;
        },
        loadFbx(file) {
            // model
            const self = this;
            let url = window._CONFIG['staticDomainURL'] + '/' + file;
            let loader = new FBXLoader();
            loader.load(url, object => self.initSceneObj(file, object), self.onProgress);
        },

        initSceneObj(key, object){
            const self = this;
            self.storage.setBig(key, JSON.stringify(object))
            object.traverse(function(obj) {
                if (obj.isMesh) {
                    obj.castShadow = true;
                    // console.log(obj);
                }
            });
            self.removeCameraByScene(object)
            self.scene.add(object);
        },

        /**
         * 移动摄像机，以适应整个场景大小
         */
        removeCameraByScene(object){
            let box = new THREE.Box3();
            let be = box.expandByObject(object);
            console.log('相机移动》》', 0 - be.max.x, Math.max(be.max.x, be.max.z), be.max.z);
            this.camera.position.set(0 - be.max.x * 1.5, Math.min(be.max.x, be.max.z), be.max.z * 1.5);
        },
        onProgress: function(xhr) {
            if (xhr.lengthComputable) {
                let percentComplete = (xhr.loaded / xhr.total) * 100;
                let percent = Math.round(percentComplete, 2);
                this.progressPercent = percent;
                if (percent == 100) {
                    setTimeout(() => {
                        this.progressPercent++;
                    }, 9000);
                }
            }
        },

        // cameraTween (obj, during) {
        //
        //     let box = new THREE.Box3();
        //     let be = box.expandByObject(obj);
        //
        //     let n = obj.position;
        //     let x = parseInt(be.max.z - be.min.z);
        //     let y = parseInt(be.max.z - be.min.z);
        //     let z = parseInt(be.max.z - be.min.z);
        //
        //     let sinDelta = Math.sin(THREE.Math.degToRad(obj.rotation.y * 180));
        //     let cosDelta = Math.cos(THREE.Math.degToRad(obj.rotation.y * 180));
        //
        //     new TWEEN.Tween(camera.position).to({
        //         x: x * (n.x * n.x * (1 - cosDelta) + cosDelta) + y * (n.x * n.y * (1 - cosDelta) - n.z * sinDelta) + z * (n.x * n.z * (1 - cosDelta) + n.y * sinDelta),
        //         y: x * (n.x * n.y * (1 - cosDelta) + n.z * sinDelta) + y * (n.y * n.y * (1 - cosDelta) + cosDelta) + z * (n.y * n.z * (1 - cosDelta) - n.x * sinDelta),
        //         z: x * (n.x * n.z * (1 - cosDelta) - n.y * sinDelta) + y * (n.y * n.z * (1 - cosDelta) + n.x * sinDelta) + z * (n.z * n.z * (1 - cosDelta) + cosDelta),
        //     }, during).start();
        // },

        // 帧循环、游戏循环
        animation() {
            this.render();
            //更新控制器
            this.orbit.update();
            // 动画
            //   if (TWEEN != null && typeof TWEEN != 'undefined') {
            //     TWEEN.update()
            //   }
            //更新性能插件
            this.stats.update();
            requestAnimationFrame(this.animation);

            this.renderer.render(this.scene, this.camera);
        },
        windowResize() {
            // 重置渲染器输出画布canvas尺寸
            this.renderer.setSize(this.container.clientWidth, this.container.clientHeight);
            this.labelRenderer.setSize(this.container.clientWidth, this.container.clientHeight);
            // 全屏情况下：设置观察范围长宽比aspect为窗口宽高比
            this.camera.aspect = this.container.clientWidth / this.container.clientHeight;
            // 渲染器执行render方法的时候会读取相机对象的投影矩阵属性projectionMatrix
            // 但是不会每渲染一帧，就通过相机的属性计算投影矩阵(节约计算资源)
            // 如果相机的一些属性发生了变化，需要执行updateProjectionMatrix ()方法更新相机的投影矩阵
            this.camera.updateProjectionMatrix();
        },
        windowInit() {
            setTimeout(() => {
                this.$message.success('窗口初始化完成');
                if (document.createEvent) {
                    let event = document.createEvent('HTMLEvents');
                    event.initEvent('resize', true, true);
                    window.dispatchEvent(event);
                } else if (document.createEventObject) {
                    window.fireEvent('onresize');
                }
            }, 500);
        },
        render() {
            this.renderer.render(this.scene, this.camera);
            this.labelRenderer.render(this.scene, this.camera);
        },
        handleCancel() {
            this.visible = false;
        },
        handleOk() {
            const self = this;
            // 触发表单验证
            this.form.validateFields((err, values) => {
                if (!err) {
                    self.confirmLoading = true;
                    let httpurl = '';
                    let method = '';
                    if (!this.model.id) {
                        httpurl += this.url.add;
                        method = 'post';
                    } else {
                        httpurl += this.url.edit;
                        method = 'put';
                    }
                    let formData = Object.assign(this.model, values);
                    console.log('表单提交数据', formData);
                    httpAction(httpurl, formData, method)
                        .then(res => {
                            if (res.success) {
                                console.log('====>', res.message);
                                self.$message.success(res.message);
                                self.$emit('ok');
                            } else {
                                self.$message.warning(res.message);
                            }
                        })
                        .finally(() => {
                            self.confirmLoading = false;
                            self.handleCancel();
                        });
                }
            });
        }
    },
    mounted() {
        this.init();
        this.windowInit();
    }
};
</script>

<style lang="less" scoped>
#container {
    position: relative;
    height: calc(100vh - 260px);
    padding: 0px;
    margin: 0px;

    .load-mark {
        position: absolute;
        z-index: 999999;
        top: 0;
        width: 100%;
        height: calc(100vh - 260px);
        background-color: rgba(0, 0, 0, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;

        .progress-box {
            width: 200px;
            top: 150px;
            padding: 30px;
            border-radius: 8px;
            background-color: rgba(0, 60, 200, 0.5);
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;

            .title {
                color: #f0f2f5;
                padding-bottom: 20px;
            }
        }
    }
}
</style>
