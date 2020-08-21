<#assign base=springMacroRequestContext.getContextUrl("")>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>3D场景部署</title>
</head>
<body>

<script language="javascript" type="text/javascript" src="${base}/js/jquery-3.5.0.min.js"></script>
<link rel="stylesheet" href="http://www.yanhuangxueyuan.com/js/element-ui/index.css">

<#--<script language="javascript" type="text/javascript" src="${base}/plug-in/echart/echarts.min.js"></script>-->

<#--<script language="javascript" type="text/javascript" src="${base}/deploy/scene.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/deploy/label.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/deploy/storage.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/deploy/device.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/deploy/loader.js"></script>-->
<#--<script language="43" type="text/javascript" src="${base}/deploy/scenePanel.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/deploy/toolbar.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/deploy/deploy.js"></script>-->

<#--<script language="javascript" type="text/javascript" src="${base}/webpage/com/jeecg/data/js/devicePanel.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/webpage/com/jeecg/data/js/deviceStatus.js"></script>-->
<#--<script language="javascript" type="text/javascript" src="${base}/webpage/com/jeecg/data/js/deviceRelation.js"></script>-->

<#--<script language="javascript" type="text/javascript" src="${base}/webpage/com/jeecg/position/js/fence-position.js"></script>-->

<div id="container">
    <div class="load-mark">
        <div class="progress-box">
            <div class="title">正在加载模型</div>
            <div><span id="progressPercent">0%</span></div>
            <div style="position: absolute;width:400px;" :style="{left: left+'px',top: top+'px'}">
                <el-progress :text-inside="true" :stroke-width="30" :percentage="loadedData"></el-progress>
            </div>
        </div>
    </div>
</div>
<script type="module">

    import * as THREE from '${base}/threejs/build/three.module.js';
    import {Toolbar} from '${base}/threejs/editor/js/Toolbar.js';
    import {Storage} from '${base}/threejs/editor/js/Storage.js';

    import {CSS2DRenderer} from '${base}/threejs/examples/jsm/renderers/CSS2DRenderer.js';
    import {OrbitControls} from '${base}/threejs/examples/jsm/controls/OrbitControls.js';
    import {TransformControls} from '${base}/threejs/examples/jsm/controls/TransformControls.js';
    import Stats from '${base}/threejs/examples/jsm/libs/stats.module.js';
    import {GUI} from '${base}/threejs/examples/jsm/libs/dat.gui.module.js';

    import {GLTFLoader} from '${base}/threejs/examples/jsm/loaders/GLTFLoader.js';
    import {OBJLoader} from '${base}/threejs/examples/jsm/loaders/OBJLoader.js';
    import {DRACOLoader} from '${base}/threejs/examples/jsm/loaders/DRACOLoader.js';
    import {FBXLoader} from '${base}/threejs/examples/jsm/loaders/FBXLoader.js';

    //设置全局变量
    const token = '${token}';
    let scene, camera, ambientLight, directionalLight, renderer, labelRenderer, orbit, control;
    let stats, gui;
    let storage;

    let SCREEN_WIDTH = window.innerWidth, SCREEN_HEIGHT = window.innerHeight;
    let VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 1, FAR = 100000;

    let mouse, raycaster;
    let rollOverMesh, rollOverMaterial, isDeploy = false;
    let rollOverGeo;
    let attachObj;
    let objects = [];
    let selectObj;
    let container = document.getElementById("container");
    let leftOpen = true, rightOpen = true;
    let mouseDownBoxHelper, mouseMoveBoxHelper;

    init();

    function init() {
        console.log('-- 场景初始化 --')
        initRender()
        initCamera()
        initScene()
        initLight()
        initControls()
        initEvent()
        initStats()
        initGUI()
        initStorage()

        let grid = new THREE.GridHelper(100000, 50, 0x555555, 0xaaaaaa)
        grid.material.opacity = 0.6
        grid.material.transparent = true
        scene.add(grid)

        //坐标轴辅助
        let axes = new THREE.AxisHelper(100000)
        scene.add(axes)

        animation()
    }

    //1.渲染器
    function initRender() {
        renderer = new THREE.WebGLRenderer()
        renderer.setClearColor(new THREE.Color(255, 255, 255))
        renderer.setPixelRatio(window.devicePixelRatio)
        renderer.setSize(container.clientWidth, container.clientHeight)
        renderer.shadowMap.enabled = true
        renderer.shadowMap.type = THREE.PCFSoftShadowMap
        container.appendChild(renderer.domElement)

        labelRenderer = new CSS2DRenderer()
        labelRenderer.setSize(container.clientWidth, container.clientHeight)
        labelRenderer.domElement.style.position = 'absolute'
        labelRenderer.domElement.style.top = 0
        container.appendChild(labelRenderer.domElement)
    }

    // 2.场景
    function initScene() {
        scene = new THREE.Scene()
        scene.background = new THREE.Color(0xf8f8f8)
    }

    // 3.相机
    function initCamera() {
        camera = new THREE.PerspectiveCamera(45, container.clientWidth / container.clientHeight, 1, 10000)
        camera.position.set(100, 50, 100)
        camera.lookAt(0, 0, 0)
    }

    //4.事件
    function initEvent() {
        // 添加鼠标点击事件，捕获点击时当前选中的物体
        window.addEventListener('resize', windowResize, false)
        // container.addEventListener('mousemove', onDocumentMouseMove, false);
        // container.addEventListener("mousedown", onDocumentMouseDown, false);
        // container.addEventListener("mouseup", onDocumentMouseUp, false);
    }

    //5.控制
    function initControls() {
        orbit = new OrbitControls(camera, labelRenderer.domElement)
        // orbit = new THREE.OrbitControls(camera, renderer.domElement);
        orbit.enableDamping = false
        orbit.dampingFactor = 0
        orbit.enableZoom = true
        orbit.autoRotate = false
        orbit.minDistance = 5
        orbit.maxDistance = 100000
        //是否开启右键拖拽
        orbit.enablePan = true
        orbit.update()
        orbit.addEventListener('change', render)
        scene.add(orbit)

        control = new TransformControls(camera, labelRenderer.domElement)
        control.addEventListener('change', render)

        control.addEventListener('dragging-changed', function (event) {
            console.log(event.value ? '开始拖动' : '拖动结束')
            self.orbit.enabled = !event.value
        })
        scene.add(control)
    }

    //6.光源
    function initLight() {
        /* scene.add(new THREE.AmbientLight(0xaaaaaa));
                                  light = new THREE.DirectionalLight(0xffffff, 0.6);
                                  light.position.set(0, 200, 0);
                                  scene.add(light); */
        ambientLight = new THREE.AmbientLight(0xaaaaaa)
        ambientLight.position.set(0, 200, 0)
        scene.add(ambientLight) // 环境光源

        directionalLight = new THREE.DirectionalLight(0xffffff, 1) // 平行光源 例如太阳光
        // directionalLight.castShadow = true;
        directionalLight.position.set(500, 2000, 1000)
        directionalLight.position.multiplyScalar(1.3)
        directionalLight.castShadow = true
        directionalLight.shadow.mapSize.width = 1024
        directionalLight.shadow.mapSize.height = 1024
        let d = 300
        directionalLight.shadow.camera.left = -d
        directionalLight.shadow.camera.right = d
        directionalLight.shadow.camera.top = d
        directionalLight.shadow.camera.bottom = -d
        directionalLight.shadow.camera.far = 100000
        scene.add(directionalLight)
    }

    // 7.性能工具
    function initStats() {
        stats = new Stats()
        stats.domElement.style.position = 'absolute'
        stats.domElement.style.top = '0px'
        container.appendChild(stats.domElement)
    }

    /* 8.调试插件 */
    function initGUI() {
        let params = {
            enableWind: true,
            showBall: false,
            togglePins: false
        }
        gui = new GUI()
        gui.domElement.style.position = 'absolute'
        gui.domElement.style.top = '0px'
        gui.domElement.style.right = '0px'
        container.appendChild(gui.domElement)

        gui.add(params, 'enableWind').name('Enable wind')
        gui.add(params, 'showBall').name('Show ball')
        gui.add(params, 'togglePins').name('Toggle pins')
    }

    function initStorage() {
        storage = new Storage()
        storage.init(function () {
            console.log('storage init end')
            getSceneConfig()
        })
    }

    function getSceneConfig(sceneId) {
        console.log(sceneId)
        $.ajax({
            headers: {
                "X-Access-Token":token//此处放置请求到的用户token
            },
            type: "GET",
            url: "${base}/scene/manage/getSceneConfig?id=" + sceneId,//请求url
            contentType: "application/json;charset=UTF-8",
            success: (res) => {
                console.log('场景配置：', res)
                if (res.success) {
                    let sceneConfig = res.result
                    console.log(sceneConfig)
                    loadObject(sceneConfig)
                } else {
                    alert(res.message);
                }
            },
            error: (err) => {
            }
        })
    }

    function loadObject(sceneConfig) {
        let modelFiles = sceneConfig.modelFiles.split(',')
        for (let i in modelFiles) {
            // 先从缓存中读取object
            /*storage.get( function ( state ) {
                if ( isLoadingFromHash ) return;
                if ( state !== undefined ) {
                    editor.fromJSON( state );
                }
                var selected = editor.config.getKey( 'selected' );
                if ( selected !== undefined ) {
                    editor.selectByUuid( selected );
                }
            } );*/
            storage.get(res => {
                if (res == undefined || res.length <= 0) {
                    console.log('服务端加载模型：', modelFiles[i])
                    let type = modelFiles[i].substring(modelFiles[i].lastIndexOf('.') + 1).toLowerCase()
                    console.log(type)
                    // 根据文件类型加载场景模型
                    switch (type) {
                        case 'fbx':
                            loadFbx(modelFiles[i])
                            break
                        case 'json':
                            loadJson(modelFiles[i])
                            break
                        case 'obj':
                            // loadObj(modelFiles[i])
                            break
                        case 'gltf':
                            loadDracoGltf(modelFiles[i])
                            break
                        default:
                            break
                    }
                } else {
                    console.log('浏览器缓存加载模型：', modelFiles[i])
                    console.log(res.scene)
                    let loader = new THREE.ObjectLoader()
                    loader.parse(res.scene, function (scene) {
                        scene.add(scene);
                    });
                }
            })
        }
    }

    function setScene (scene) {

        this.scene.uuid = scene.uuid;
        this.scene.name = scene.name;

        this.scene.background = (scene.background !== null) ? scene.background.clone() : null;

        if (scene.fog !== null) this.scene.fog = scene.fog.clone();

        this.scene.userData = JSON.parse(JSON.stringify(scene.userData));

        this.signals.sceneGraphChanged.active = false;

        while (scene.children.length > 0) {
            this.addObject(scene.children[0]);
        }
    }

    // 加载压缩的gltf文件
    function loadDracoGltf(file) {
        const self = this
        let url = window._CONFIG['staticDomainURL'] + '/' + file
        // 创建加载器
        let gltfLoader = new GLTFLoader()
        const dracoLoader = new DRACOLoader()
        dracoLoader.setDecoderPath('/static/js/draco/gltf/')
        dracoLoader.setDecoderConfig({type: 'js'})
        dracoLoader.preload()
        gltfLoader.setDRACOLoader(dracoLoader)
        // 然后直接加载模型即可
        gltfLoader.load(url, object => self.initSceneObj(file, object.scene), self.onProgress)
    }

    function loadObj(file) {
        let url = window._CONFIG['staticDomainURL'] + '/' + file
    }

    function loadJson(file) {
        let url = window._CONFIG['staticDomainURL'] + '/' + file
        let loader = new OBJLoader()
        loader.load(url, object => self.initSceneObj(file, object), self.onProgress)
    }

    function loadFbx(file) {
        // model
        const self = this
        let url = window._CONFIG['staticDomainURL'] + '/' + file
        let loader = new FBXLoader()
        loader.load(url, object => self.initSceneObj(file, object), self.onProgress)
    }

    function initSceneObj(key, object) {
        const self = this
        // self.storage.setBig(key, JSON.stringify(object))
        /* object.traverse(function(obj) {
            if (obj.isMesh) {
                obj.castShadow = true
                // console.log(obj);
            }
        })*/
        self.removeCameraByScene(object)
        self.scene.add(object)
    }

    /**
     * 移动摄像机，以适应整个场景大小
     */
    function removeCameraByScene(object) {
        let box = new THREE.Box3()
        let be = box.expandByObject(object)
        console.log('相机移动》》', 0 - be.max.x, Math.max(be.max.x, be.max.z), be.max.z)
        camera.position.set(0 - be.max.x * 1.5, Math.min(be.max.x, be.max.z), be.max.z * 1.5)
    }

    function onProgress(xhr) {
        if (xhr.lengthComputable) {
            let percentComplete = (xhr.loaded / xhr.total) * 100
            let percent = Math.round(percentComplete, 2)
            document.getElementById('progressPercent').text = percent;
            if (percent == 100) {
                setTimeout(() => {
                    document.getElementById('progressPercent').hidden = true
                }, 9000)
            }
        }
    }

    // 帧循环、游戏循环
    function animation() {
        render()
        //更新控制器
        orbit.update()
        // 动画
        //   if (TWEEN != null && typeof TWEEN != 'undefined') {
        //     TWEEN.update()
        //   }
        //更新性能插件
        stats.update()
        requestAnimationFrame(animation)

        renderer.render(scene, camera)
    }

    function windowResize() {
        // 重置渲染器输出画布canvas尺寸
        renderer.setSize(container.clientWidth, container.clientHeight)
        labelRenderer.setSize(container.clientWidth, container.clientHeight)
        // 全屏情况下：设置观察范围长宽比aspect为窗口宽高比
        camera.aspect = container.clientWidth / container.clientHeight
        // 渲染器执行render方法的时候会读取相机对象的投影矩阵属性projectionMatrix
        // 但是不会每渲染一帧，就通过相机的属性计算投影矩阵(节约计算资源)
        // 如果相机的一些属性发生了变化，需要执行updateProjectionMatrix ()方法更新相机的投影矩阵
        camera.updateProjectionMatrix()
    }

    function windowInit() {
        setTimeout(() => {
            $message.success('窗口初始化完成')
            if (document.createEvent) {
                let event = document.createEvent('HTMLEvents')
                event.initEvent('resize', true, true)
                window.dispatchEvent(event)
            } else if (document.createEventObject) {
                window.fireEvent('onresize')
            }
        }, 500)
    }

    function render() {
        renderer.render(scene, camera)
        labelRenderer.render(scene, camera)
    }

</script>
</body>
</html>

<style>
    #container {
        position: relative;
        height: calc(100vh);
        padding: 0px;
        margin: 0px;
    }

    #container .title {
        color: #f0f2f5;
        padding-bottom: 20px;
    }

    #container #progressPercent{
        color: #f0f2f5;
        border: 3px dodgerblue solid;
        width: 50px;
        height: 50px;
        border-radius: 50px;
        font-size: 26px;
        padding: 10px;
        text-align: center;
        vertical-align: middle;
        horiz-align: center;
    }

    #container .progress-box {
        width: 200px;
        top: 150px;
        padding: 30px;
        border-radius: 8px;
        background-color: rgba(0, 60, 200, 0.5);
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
    }

    #container .load-mark {
        position: absolute;
        z-index: 999999;
        top: 0;
        width: 100%;
        height: calc(100vh);
        background-color: rgba(0, 0, 0, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;
    }
</style>
