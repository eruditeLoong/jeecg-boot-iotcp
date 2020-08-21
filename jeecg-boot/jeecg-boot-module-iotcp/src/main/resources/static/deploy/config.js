var Config = function(){
    //设置全局变量
    var scene, camera, renderer, labelRenderer, orbit;
    var stats, gui;

    var SCREEN_WIDTH = window.innerWidth, SCREEN_HEIGHT = window.innerHeight;
    var VIEW_ANGLE = 45, ASPECT = SCREEN_WIDTH / SCREEN_HEIGHT, NEAR = 1, FAR = 100000;
    var mouse, raycaster;
    var objects = [];
    var container = document.getElementById("container");
    var mouseMoveBoxHelper;
    var ambientLight;
    var directionalLight;


    //1.渲染器
    function initRender() {
        renderer = new THREE.WebGLRenderer();
        renderer.setPixelRatio(window.devicePixelRatio);
        renderer.setSize(window.innerWidth, window.innerHeight);
        document.body.appendChild(renderer.domElement);
        // container.appendChild(renderer.domElement);
        renderer.shadowMap.enabled = true;
        renderer.shadowMap.type = THREE.PCFSoftShadowMap;
        labelRenderer = new THREE.CSS2DRenderer();

        labelRenderer.setSize(window.innerWidth, window.innerHeight);
        labelRenderer.domElement.style.position = 'absolute';
        labelRenderer.domElement.style.top = 0;
        container.appendChild(labelRenderer.domElement);
    }

    function initScene() {
        scene = new THREE.Scene();
        scene.background = new THREE.Color(0x000000);
        // 2.场景
        scene.background = new THREE.CubeTextureLoader().setPath('webpage/com/jeecg/data/img/skyboxsun25deg/').load(['px.jpg', 'nx.jpg', 'py.jpg', 'ny.jpg', 'pz.jpg', 'nz.jpg']);
    }

    //2.相机
    function initCamera() {
        camera = new THREE.PerspectiveCamera(VIEW_ANGLE, ASPECT, NEAR, FAR);
        camera.position.set(1000, 500, 1000);
        camera.lookAt(0, 0, 0);
        //scene.add(camera);
    }

    //5.控制
    function initControls() {
        orbit = new THREE.OrbitControls(camera, labelRenderer.domElement, renderer.domElement);
        orbit.enableDamping = false;
        orbit.dampingFactor = 0;
        orbit.enableZoom = true;
        orbit.autoRotate = false;
        orbit.minDistance = 5;
        orbit.maxDistance = 100000;
        //orbit.minPolarAngle = Math.PI / 4;
        orbit.maxPolarAngle = Math.PI / 2;
        //是否开启右键拖拽
        orbit.enablePan = true;
        orbit.update();
    }

    //6.光源
    function initLight() {
        ambientLight = new THREE.AmbientLight(0xaaaaaa)
        scene.add(ambientLight); // 环境光源
        directionalLight = new THREE.DirectionalLight(0xffffff, 1); // 平行光源 例如太阳光
        directionalLight.position.set(50, 200, 100);
        directionalLight.position.multiplyScalar(1.3);
        // directionalLight.castShadow = true;
        directionalLight.shadow.mapSize.width = 100000;
        directionalLight.shadow.mapSize.height = 100000;
        var d = 300;
        directionalLight.shadow.camera.left = -d;
        directionalLight.shadow.camera.right = d;
        directionalLight.shadow.camera.top = d;
        directionalLight.shadow.camera.bottom = -d;
        directionalLight.shadow.camera.far = 100000;
        scene.add(directionalLight);
    }

    // 7.性能工具
    function initStats() {
        stats = new Stats();
        stats.domElement.style.position = 'absolute';
        stats.domElement.style.left = '';
        stats.domElement.style.right = '280px';
        stats.domElement.style.top = '0px';
        stats.domElement.style.width = '80px';
        stats.domElement.id = 'stats';
        container.appendChild(stats.domElement);
    }

    //初始化函数
    function init() {
        console.log('init...');
        initRender();
        initCamera();
        initScene();
        initLight();
        initControls();
        initStats();
        animation();
    }

    // 帧循环、游戏循环
    function animation() {
        render();
        //更新控制器
        orbit.update();
        //更新性能插件
        stats.update();
        // 动画
        if (TWEEN != null && typeof (TWEEN) != 'undefined') {
            TWEEN.update();
        }
        requestAnimationFrame(animation);

    }

    // 窗口大小改变
    function onWindowResize() {
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(window.innerWidth, window.innerHeight);
        labelRenderer.setSize(window.innerWidth, window.innerHeight);
        renderer.render(scene, camera);
    }

    function render() {
        //根据当前的位置计算与z轴负方向的夹角，即为正北方方向。如果使用camera的rotation.y你会发现得出的弧度制范围是-90到90，指南针就不能旋转360度了。
        var dir = new THREE.Vector3(-camera.position.x, 0, -camera.position.z).normalize();
        var theta = Math.atan2(-dir.x, -dir.z);
        //指南针旋转
        $('#imgCompass').rotate(THREE.Math.radToDeg(theta));
        renderer.render(scene, camera);
        labelRenderer.render(scene, camera);
    }
}

Config.prototype = function () {

}