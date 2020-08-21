var toolbar = {

    create: function () {
        let toolbar = document.createElement("div");
        toolbar.id = 'toolbar';
        toolbar.style.position = 'absolute';
        toolbar.style.top = '10px';
        toolbar.style.width = '100%';
        toolbar.style.height = 'auto';
        toolbar.style.display = 'flex';
        toolbar.style.justifyContent = 'center';

        document.body.appendChild(toolbar);
    },

    addBar: function (barName, faIcon, onclick) {
        let toolbar = document.getElementById('toolbar');
        let bar = document.createElement('a');
        bar.style.width = '28px';
        bar.style.height = '28px';
        bar.style.border = '1px solid rgba(127, 255, 255, 0.8)';
        bar.style.color = 'rgb(127, 255, 255)';
        bar.style.backgroundColor = 'rgba(65,174,238,0.6)';
        bar.style.margin = '0 2px';
        // bar.style.borderRadius = '5px';
        bar.title = barName;
        bar.href = 'javascript:;';
        bar.onclick = onclick;
        bar.innerHTML = '<i class="fa fa-' + faIcon + '"></i>'
        toolbar.appendChild(bar);
    },

}