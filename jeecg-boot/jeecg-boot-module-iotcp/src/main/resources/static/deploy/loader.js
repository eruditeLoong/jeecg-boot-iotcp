var loader = {
	loadFile: function (filePath, callback) {
		var extension = filePath.split('.').pop().toLowerCase();
		extension = extension.indexOf(',') != -1 ? extension.substring(0, extension.indexOf(',')) : extension;
		switch (extension) {
			case '3ds':
				break;
			case 'dae':
				break;
			case 'fbx':
				var FBXLoader = new THREE.FBXLoader();
				FBXLoader.load(filePath, function (object) {
//				object.traverse(function(child) {
//					if (child.isMesh) {
//						child.castShadow = true;
//						child.receiveShadow = true;
//					}
//				});
					callback(object);
				});
				break;
			case 'glb':
				break;
			case 'gltf':
				var GLTFLoader = new THREE.GLTFLoader();
				GLTFLoader.load(filePath, function (gltf) {
					// gltf.scene.traverse(function (child) {
					// });
					callback(gltf.scene);
				}, undefined, function (e) {
					console.error(e);
				});
				break;
			case 'js':
			case 'json':
			case '3geo':
			case '3mat':
			case '3obj':
			case '3scn':
				var objectLoader = new THREE.ObjectLoader();
				objectLoader.load(filePath, function (object) {
					var mixer = new THREE.AnimationMixer(object);
//				object.traverse(function(child) {
//					if (child.isMesh) {
//						child.castShadow = true;
//						child.receiveShadow = true;
//					}
//				});
					callback(object);
				});
				break;
			case 'obj':
				break;
			case 'stl':
				break;
			case 'svg':
				break;
			case 'zip':
				break;
			default:
				break;
		}
	},

	/** 设置位置信息 */
	setObjPosition: function (obj) {
		var p = obj.userData.position;
		if (p == undefined) {
			return false;
		}
// 		console.log(obj);
		obj.position.set(parseFloat(p.x), parseFloat(p.y), parseFloat(p.z));
		obj.scale.x = parseFloat(p.s);
		obj.scale.y = parseFloat(p.s);
		obj.scale.z = parseFloat(p.s);
		// obj.scale.multiplyScalar(parseFloat(p.sx));
		obj.rotation.x = parseFloat(p.rx);
		obj.rotation.y = parseFloat(p.ry);
		obj.rotation.z = parseFloat(p.rz);
		objects.push(obj);
		scene.add(obj);
	}
}