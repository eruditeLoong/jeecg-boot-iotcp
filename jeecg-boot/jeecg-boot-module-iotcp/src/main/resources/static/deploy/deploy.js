var deploy = {
    createDeploy: function () {
        let _this = this;
        toolbar.addBar('场景部署', 'codepen', function (e) {
            _this.showBar();
        });

        let deployBar = document.createElement('div');
        deployBar.id = 'deployBar';
        deployBar.style.position = 'absolute';
        deployBar.style.bottom = '10px';
        deployBar.style.width = '100%';
        deployBar.style.height = 'auto';
        deployBar.style.display = 'flex';
        deployBar.style.justifyContent = 'center';
        document.body.appendChild(deployBar);
        addButton('移动', 'arrows', function () {
            control.setMode('translate');
        });
        addButton('旋转', 'spinner', function () {
            control.setMode('rotate');
        });
        addButton('缩放', 'arrows-alt', function () {
            control.setMode('scale');
        });
        addButton('增大标尺', 'plus-square-o', function () {
            control.setSize(control.size + 0.1);
        });
        addButton('减小标尺', 'minus-square-o', function () {
            control.setSize(Math.max(control.size - 0.1, 0.1));
        });
        addButton('取消标尺', 'ban', function () {
            control.detach();
            attachObj = null;
            _this.hideBar();
        });

        function addButton(buttonName, faIcon, onclick) {
            let toolbar = document.getElementById('deployBar');
            let bar = document.createElement('a');
            bar.style.width = '24px';
            bar.style.height = '24px';
            bar.style.lineHeight = '24px';
            bar.style.border = '1px solid rgba(127, 255, 255, 0.8)';
            bar.style.color = 'rgb(127, 255, 255)';
            bar.style.backgroundColor = 'rgba(65,174,238,0.6)';
            bar.style.margin = '0 2px';
            bar.title = buttonName;
            bar.href = 'javascript:;';
            bar.onmouseenter = function (buttonName, bar) {
                layer.tips(buttonName, bar, {
                    tips: [1, '#01AAED'],//弹出方向 上下左右(1-4) 颜色
                    time: 10000,//3s后消失
                });
            };
            // bar.onmouseout = "layer.tips()";
            bar.onclick = onclick;
            bar.innerHTML = '<i class="fa fa-' + faIcon + '"></i>'
            toolbar.appendChild(bar);
        }
    },

    showBar: function () {
        // document.getElementById('deployBar').style.display = 'block';
        $('#deployBar').show();
        orbit.maxPolarAngle = Math.PI;
        container.style.cursor = 'auto';
    },
    hideBar: function () {
        $('#deployBar').hide();
        control.detach();
        // document.getElementById('deployBar').style.display = 'none';
    },


}