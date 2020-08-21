/**
 *
 */
var label = {
    display: function (isValid) {
        $('.device-label').each(function (index, label) {
            if (isValid) {
                $(label).show();
            } else {
                $(label).hide();
            }
        });
    },

    /**
     * 拿到模型对象的标签dom
     */
    getLabel: function (obj) {

    },

    /**
     * [getLabel 创建对象的悬浮标签]
     *
     * @param {[Object]}
     *            obj [对象]
     */
    createLabel: function (obj) {
        if (obj.userData.type === 'nsdevice') {
            return false;
        }
        var box = new THREE.Box3();
        var be = box.expandByObject(obj);
        var labelDiv = document.createElement('div');
        labelDiv.id = "label-" + obj.uuid;
        labelDiv.className  = "device-label";
        labelDiv.style.cssText = 'border: 1px solid rgba(30, 255, 222, 0.75);border-radius:5px;background-color:rgba(26, 47, 79, 0.5);color:#FFF;padding: 0px 5px;marginTop:-1em';
        labelDiv.style.display = sceneObj.conf.isLabelDisplay?"block":"none";
        labelDiv.textContent = obj.name + "[" + obj.userData.code + "]";
        var label = new THREE.CSS2DObject(labelDiv);
        var height = parseInt(be.max.y - be.min.y);
        label.position.set(obj.position.x, obj.position.y + height * 1.2, obj.position.z);
        return label;
    },

    deleteLabel: function (objId) {
        $("label-" + objId).remove();
    },

    deleteAllLabel: function () {
        $('.device-label').each(function (i, l) {
            $(this).remove();
        });
    },
}