let SceneConf = function (editor) {
    let scope = this;

    let scene = editor.scene;

};

SceneConf.prototype = {

    /**
     * @callback
     */
    get: function (callback) {
        console.log(editor.TOKEN, editor.SCENE_ID);
        let conf = '0000';
        $.ajax({
            headers: {
                "X-Access-Token": editor.TOKEN//此处放置请求到的用户token
            },
            type: "GET",
            url: editor.BASE_URL + "/scene/manage/getSceneConfig?id=" + editor.SCENE_ID,//请求url
            contentType: "application/json;charset=UTF-8",
            success: (res) => {
                console.log('场景配置：', res)
                if (res.success) {
                    let conf = res.result;
                    conf.type = 'scene';
                    editor.SCENE_ID = conf.id;
                    callback({success: true, msg: 'success', conf: conf});
                } else {
                    callback({success: false, msg: res.message});
                }
            },
            error: (err) => {
                callback({success: false, msg: err.message});
            }
        })
    },

    save: function () {
        console.log('save scene config ： '+scene);
        // $.post(this.BASE_URL + '/scene/manage/saveConf', {id: this.SCENE_ID}, res => {
        //
        // });
    },

};

export {SceneConf};
