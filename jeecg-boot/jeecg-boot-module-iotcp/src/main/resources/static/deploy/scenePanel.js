var scenePanel = {
	top: '0px',
	left: '0px',
	width: '320px',
	height: '100px',
	z_index: 1000,
	isValid: true,

	create: function (sceneId) {
		var panel = document.createElement('div');
		panel.id = "scenePanel";
		panel.style.position = 'absolute';
		panel.style.top = this.top;
		panel.style.left = this.left;
		panel.style.width = this.width;
		panel.style['z-index'] = this.z_index;
		panel.style.height = (window.innerHeight) + 'px';
		panel.style.border = '1';
		panel.style['border-width'] = '2px';
		panel.style['border-color'] = 'rgba(130, 150, 100, 0.5)';
		panel.style.background = 'rgba(30, 50, 100, 0.6)';
		document.body.appendChild(panel);
		this.initTreegrid(panel, sceneId);
	},

	display: function (isValid) {
		if (isValid) {
			$('#scenePanel').show();
		} else {
			$('#scenePanel').hide();
		}
	},

	initTreegrid: function (panel, sceneId) {
		var _this = this;

		var table = document.createElement('table');
		table.name = 'treegrid';
		table.id = 'treegrid';
		table.style['overflow-y'] = 'auto';
		table.style.cssText = "width:100%; background-color:transparent;";
		panel.appendChild(table);

		$('#treegrid').treegrid({
			title: '设备列表',
			height: (window.innerHeight - 10) + 'px',
			fit: true,
			border: false,
			idField: 'id',
			treeField: 'name',
			parentField: 'parentBy',
			rownumbers: false,
			columns: [[{
				field: 'name',
				title: '名称',
				align: 'left',
			}, {
				field: 'typeName',
				title: '类型',
				align: 'left',
			}, {
				field: 'opt',
				title: '操作',
			}]],
			onClickRow: function () {
				$('#treegrid').datagrid('clearSelections');
			},
		});
		this.getDeviceList(sceneId);
	},

	getDeviceList: function (sceneId) {
		var _this = this;
		$.getJSON("sceneController.do?getSceneDeviceDeployTree", function (result) {
			if (result.success) {
				var list = result.obj;
				var data = {
					total: list.length,
					rows: []
				};
				for (var i in list) {
					var d = list[i];
					var type = '', icon = '', opt = '';
					switch (d.type) {
						case 'gateway':
							type = '网关';
							icon = 'fa fa-codepen';
							opt = '<a href="javascript:;" class="fa fa-map-marker" title="定位" onclick="scenePanel.searchDeviceById(\'' + d.id + '\');"></a>';
							break;
						case 'terminal':
							type = '终端';
							icon = 'fa fa-codepen';
							opt = '<a href="javascript:;" class="fa fa-map-marker" title="定位" onclick="scenePanel.searchDeviceById(\'' + d.id + '\');"></a>';
							break;
						case 'nsdevice':
							type = '非设备';
							icon = 'fa fa-cube';
							opt = '<a href="javascript:;" class="fa fa-map-marker" title="定位" onclick="scenePanel.searchDeviceById(\'' + d.id + '\');"></a>';
							break;
						case 'scene':
							type = '场景';
							icon = 'fa fa-sitemap';
							opt = '<a href="javascript:;" class="fa fa-pencil-square-o" title="切换" onclick="scenePanel.changeScene(\'' + d.id + '\');"></a>';
							break;
						default:
							break;
					}
					var o = {
						id: d.id,
						state: (sceneId == d.id) || (sceneId == d.pid) ? "open" : "closed",
						iconCls: icon,
						name: d.name,
						parentBy: d.pid,
						type: d.type,
						typeName: type,
						_parentId: d.pid === '' ? null : d.pid,
						opt: opt
					}
					data.rows.push(o);
				}
				$('#treegrid').datagrid('loadData', data);
			}
		});
	},

	changeScene: function (sceneId) {
		// 根据场景id获取场景3D部署文件的路径
		$.getJSON("sceneController.do?getDeployFileUrl&sceneId=" + sceneId, function(result) {
			console.log(result);
			if (result.success) {
				let scene_ = {
					id: result.obj.id,
					name: result.obj.name,
					threeData: result.obj.threeData,
					modelFile3D: result.obj.scene3d
				};
				sceneObj.initScene(scene_);
			}
		});
	},

	searchDeviceById: function (id) {
		var _this = this;
		for (var i in objects) {
			if (objects[i].uuid === id) {
				_this.changeCameraPosition(objects[i]);
				if (control) {
					control.attach(objects[i]);
				}
				break;
			}
		}
		// alert("未找到设备！");
	},

	// 改变相机视角
	changeCameraPosition: function (obj) {
		var _this = this;
		var box = new THREE.Box3();
		var be = box.expandByObject(obj);
		new TWEEN.Tween(orbit.target).to({
			x: obj.position.x,
			y: obj.position.y,
			z: obj.position.z
		}, 500).onComplete(function () {
			// 描边
			if (mouseDownBoxHelper != undefined) {
				scene.remove(mouseDownBoxHelper);
			}
			mouseDownBoxHelper = new THREE.BoxHelper(obj, '#FF0000');
			scene.add(mouseDownBoxHelper);
			new TWEEN.Tween(camera.position).to({
				x: parseInt(be.max.x - be.min.x + be.max.y - be.min.y + be.max.z - be.min.z) * Math.sin(obj.rotation.y) + obj.position.x,
				y: parseInt(be.max.y - be.min.y + be.max.y - be.min.y + be.max.z - be.min.z) + obj.position.y,
				z: parseInt(be.max.z - be.min.z + be.max.y - be.min.y + be.max.z - be.min.z) * Math.cos(obj.rotation.y) + obj.position.z
			}, 500).start();
			// _this.cameraTween(obj, 500);
		}).start();
	},

	/*
	 * camera:相机, during：动画执行的时间
	 */
	cameraTween: function (obj, during) {

		var box = new THREE.Box3();
		var be = box.expandByObject(obj);

		var n = obj.position;
		var x = parseInt(be.max.z - be.min.z);
		var y = parseInt(be.max.z - be.min.z);
		var z = parseInt(be.max.z - be.min.z);

		var sinDelta = Math.sin(THREE.Math.degToRad(obj.rotation.y * 180));
		var cosDelta = Math.cos(THREE.Math.degToRad(obj.rotation.y * 180));

		new TWEEN.Tween(camera.position).to({
			x: x * (n.x * n.x * (1 - cosDelta) + cosDelta) + y * (n.x * n.y * (1 - cosDelta) - n.z * sinDelta) + z * (n.x * n.z * (1 - cosDelta) + n.y * sinDelta),
			y: x * (n.x * n.y * (1 - cosDelta) + n.z * sinDelta) + y * (n.y * n.y * (1 - cosDelta) + cosDelta) + z * (n.y * n.z * (1 - cosDelta) - n.x * sinDelta),
			z: x * (n.x * n.z * (1 - cosDelta) - n.y * sinDelta) + y * (n.y * n.z * (1 - cosDelta) + n.x * sinDelta) + z * (n.z * n.z * (1 - cosDelta) + cosDelta),
		}, during).start();
	}
};