/**
 * @author mrdoob / http://mrdoob.com/
 */

import * as THREE from '../../build/three.module.js';
import {ColladaLoader} from '../../examples/jsm/loaders/ColladaLoader.js';
import {DRACOLoader} from '../../examples/jsm/loaders/DRACOLoader.js';
import {FBXLoader} from '../../examples/jsm/loaders/FBXLoader.js';
import {GLTFLoader} from '../../examples/jsm/loaders/GLTFLoader.js';
import {MTLLoader} from '../../examples/jsm/loaders/MTLLoader.js';
import {OBJLoader} from '../../examples/jsm/loaders/OBJLoader.js';

import {AddObjectCommand} from './commands/AddObjectCommand.js';
import {SetSceneCommand} from './commands/SetSceneCommand.js';

import {JSZip} from '../../examples/jsm/libs/jszip.module.min.js';

var LoaderRemote = function (editor) {

    let scope = this;

    this.texturePath = '';

    this.loadFiles = function (sceneConf) {
        let fileName = sceneConf['modelFiles'].split(',');
        let timeout;
        clearTimeout(timeout);
        timeout = setTimeout(function () {
            timeout = setTimeout(function () {
                // 场景模型
                console.log('服务器加载场景模型：', fileName[0]);
                scope.loadFile(fileName[0], sceneConf || {}, 'scene');

                // 设备模型
                const deviceInstances = sceneConf['deviceInstances'] || [];
                console.log('设备配置：', deviceInstances)
                for (const device of deviceInstances) {
                    console.log('服务器加载设备模型：', device['modelFiles']);
                    scope.loadFile(device['modelFiles'], device, device.type);
                }
            }, 100);
        }, 1000);
    };

    this.onProgress = function (xhr) {
        if (xhr.lengthComputable) {
            let size = '(' + Math.floor(xhr.total / 1000).format() + ' KB)';
            let progress = Math.floor((xhr.loaded / event.total) * 100) + '%';
            console.log('Loading ==> ', size, progress);
        }
    };

    this.loadFile = function (url, config, type) {

        console.log('loadRemoteFile: ', url);
        editor.config.setKey('autosave', true);

        let extension = url.split('.').pop().toLowerCase();
        let loader;
        url = editor.BASE_URL + '/sys/common/static/' + url
        switch (extension) {
            case 'dae':
                loader = new ColladaLoader();
                loader.load(url, object => {
                    editor.configObj(object, config, type);
                    editor.execute(new AddObjectCommand(editor, object));
                }, scope.onProgress)
                break;
            case 'fbx':
                loader = new FBXLoader();
                loader.load(url, object => {
                    editor.configObj(object, config, type);
                    editor.execute(new AddObjectCommand(editor, object));
                }, scope.onProgress)
                break;
            case 'gltf':
                var dracoLoader = new DRACOLoader();
                dracoLoader.setDecoderPath('../examples/js/libs/draco/gltf/');

                loader = new GLTFLoader();
                loader.setDRACOLoader(dracoLoader);

                loader.load(url, object => {
                    editor.configObj(object, config, type);
                    editor.execute(new AddObjectCommand(editor, scene));
                }, scope.onProgress);
                break;
            case 'json':
            case '3obj':
                var objectLoader = new THREE.ObjectLoader();
                objectLoader.load(url, function (object) {
                    editor.configObj(object, config, type);
                    editor.execute(new AddObjectCommand(editor, object));
                }, scope.onProgress);
                break;

            default:
                break;
        }

    };

    function handleJSON(data) {

        if (data.metadata === undefined) { // 2.0
            data.metadata = {
                type: 'Geometry'
            };
        }

        if (data.metadata.type === undefined) { // 3.0
            data.metadata.type = 'Geometry';
        }

        if (data.metadata.formatVersion !== undefined) {
            data.metadata.version = data.metadata.formatVersion;
        }

        switch (data.metadata.type.toLowerCase()) {

            case 'buffergeometry':

                var loader = new THREE.BufferGeometryLoader();
                var result = loader.parse(data);

                var mesh = new THREE.Mesh(result);

                editor.execute(new AddObjectCommand(editor, mesh));

                break;

            case 'geometry':

                console.error('Loader: "Geometry" is no longer supported.');

                break;

            case 'object':

                var loader = new THREE.ObjectLoader();
                loader.setResourcePath(scope.texturePath);

                loader.parse(data, function (result) {

                    if (result.isScene) {

                        editor.execute(new SetSceneCommand(editor, result));

                    } else {

                        editor.execute(new AddObjectCommand(editor, result));

                    }

                });

                break;

            case 'app':

                editor.fromJSON(data);

                break;

        }

    }

    function handleZIP(contents) {

        var zip = new JSZip(contents);

        // Poly

        if (zip.files['model.obj'] && zip.files['materials.mtl']) {

            var materials = new MTLLoader().parse(zip.file('materials.mtl').asText());
            var object = new OBJLoader().setMaterials(materials).parse(zip.file('model.obj').asText());
            editor.execute(new AddObjectCommand(editor, object));

        }

        //

        zip.filter(function (path, file) {

            var manager = new THREE.LoadingManager();
            manager.setURLModifier(function (url) {

                var file = zip.files[url];

                if (file) {

                    console.log('Loading', url);

                    var blob = new Blob([file.asArrayBuffer()], {
                        type: 'application/octet-stream'
                    });
                    return URL.createObjectURL(blob);

                }

                return url;

            });

            var extension = file.name.split('.').pop().toLowerCase();
            switch (extension) {
                case 'fbx':
                    var loader = new FBXLoader(manager);
                    var object = loader.parse(file.asArrayBuffer());

                    editor.execute(new AddObjectCommand(editor, object));

                    break;

                case 'glb':
                    var loader = new GLTFLoader();
                    loader.parse(file.asArrayBuffer(), '', function (result) {
                        var scene = result.scene;
                        editor.addAnimation(scene, result.animations);
                        editor.execute(new AddObjectCommand(editor, scene));
                    });

                    break;
                case 'gltf':
                    var loader = new GLTFLoader(manager);
                    loader.parse(file.asText(), '', function (result) {
                        var scene = result.scene;
                        editor.addAnimation(scene, result.animations);
                        editor.execute(new AddObjectCommand(editor, scene));
                    });
                    break;

            }

        });

    }

    function isGLTF1(contents) {

        var resultContent;

        if (typeof contents === 'string') {

            // contents is a JSON string
            resultContent = contents;

        } else {

            var magic = THREE.LoaderUtils.decodeText(new Uint8Array(contents, 0, 4));

            if (magic === 'glTF') {

                // contents is a .glb file; extract the version
                var version = new DataView(contents).getUint32(4, true);

                return version < 2;

            } else {

                // contents is a .gltf file
                resultContent = THREE.LoaderUtils.decodeText(new Uint8Array(contents));

            }

        }

        var json = JSON.parse(resultContent);

        return (json.asset != undefined && json.asset.version[0] < 2);

    }

};

export {
    LoaderRemote
};
