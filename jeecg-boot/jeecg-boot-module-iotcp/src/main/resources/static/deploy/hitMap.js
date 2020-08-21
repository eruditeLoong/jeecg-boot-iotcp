function getHitMapDate() {
	var datas = [];
	var maxvalue = 0;
	// 模拟数据
	for (i = 40; i < 600; i += 40) {
		for (j = 20; j < 300; j += 20) {
			var value = Math.floor(Math.random() * 40);
			if (value > maxvalue) {
				maxvalue = value;
			}
			if (i < 500 || j < 200) {
				datas.push({
					x : i,
					y : j,
					value : value
				});
			}
		}
	}
	data = {
		max : 45,
		data : datas
	}
	if (sucFunc) {
		sucFunc(data);
	}
	return data;
}

function initHitMap() {
	var buildbub = parseInt(_obj.name.split("_")[2]);
	var floorNub = 0;
	if (_obj.name.split("_")[3]) {
		floorNub = parseInt(_obj.name.split("_")[3]);
	}
	// 此处获取热力图数据
	_this.gethitMapData(buildbub, floorNub, function(data) {
		var postion = _this.hitMapToABPosition["h" + buildbub];
		var roomname = "m1_room_" + buildbub + "_" + floorNub;
		if (floorNub == 0) {
			roomname = "m1_room_" + buildbub;
		}
		var room = w3DShowObj.commonFunc.findObject(roomname);
		var position = {};
		position.x = room.position.x + postion.x;
		position.y = room.position.y + postion.y + 10;
		position.z = room.position.z + postion.z;
		var pageposition = indexPage.getMSGPositionToIframe();
		var p = new THREE.Vector3(pageposition.x, pageposition.y, -1)
				.unproject(w3DShowObj.camera)
		_this.addLinkLine(position, p);
		indexPage.showDetailBtn();
		_this.setHeatMap(buildbub, floorNub, data, function(hitmapobj) {
			hitmapobj.visible = true;
			indexPage.setbackground(hitmapobj.canvas, buildbub, floorNub);
		});
	});
}

function createHeatMapModels(name, postion, scale) {
	var modeljson = {
		show : true,
		name : name,
		showSortNub : 10000,
		id : "",
		objType : "CloudChart",
		size : {
			x : 600,
			y : 300,
			z : 0
		},
		position : postion,
		scale : scale,
		rotation : {
			x : -Math.PI / 2,
			y : 0,
			z : 0
		},
		pictype : "rectangle", // arc圆 rectangle矩形 triangle三角形
		side : 1,
		opacity : 1,
		maxValue : 45,
		minValue : 0,
		materialType : "basic",
		panelColor : 0x00ffff,
		background : {
			color : 0xffffff,
			size : {
				x : 100,
				y : 100,// 画布大小
				r : 50,// 圆半径
				start : 0,
				end : 360,
				angle_r : 10,
				x1 : 0,
				y1 : 0,
				x2 : 100,
				y2 : 100,
				x3 : 100,
				y3 : 0
			},
			imgurl : "",
			type : "arc", // arc圆 rectangle矩形 triangle三角形
			border : {
				color : 0xff0000,
				width : 1
			}
		},
		values : [ {
			value : 10,
			x : 1,
			y : 1
		}, {
			value : 30,
			x : 600,
			y : 300
		}, ],
	};
	this.hiteMapObj[name] = w3DShowObj.InitAddObject(modeljson);
}