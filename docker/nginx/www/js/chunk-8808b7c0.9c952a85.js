(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-8808b7c0","chunk-8808b7c0"],{"5df3":function(e,t,a){"use strict";var l=a("02f4")(!0);a("01f9")(String,"String",(function(e){this._t=String(e),this._i=0}),(function(){var e,t=this._t,a=this._i;return a>=t.length?{value:void 0,done:!0}:(e=l(t,a),this._i+=e.length,{value:e,done:!1})}))},"88bc":function(e,t,a){(function(t){var a=1/0,l=9007199254740991,i="[object Arguments]",r="[object Function]",o="[object GeneratorFunction]",n="[object Symbol]",s="object"==typeof t&&t&&t.Object===Object&&t,d="object"==typeof self&&self&&self.Object===Object&&self,c=s||d||Function("return this")();function u(e,t,a){switch(a.length){case 0:return e.call(t);case 1:return e.call(t,a[0]);case 2:return e.call(t,a[0],a[1]);case 3:return e.call(t,a[0],a[1],a[2])}return e.apply(t,a)}function p(e,t){var a=-1,l=e?e.length:0,i=Array(l);while(++a<l)i[a]=t(e[a],a,e);return i}function f(e,t){var a=-1,l=t.length,i=e.length;while(++a<l)e[i+a]=t[a];return e}var h=Object.prototype,v=h.hasOwnProperty,m=h.toString,y=c.Symbol,b=h.propertyIsEnumerable,g=y?y.isConcatSpreadable:void 0,w=Math.max;function C(e,t,a,l,i){var r=-1,o=e.length;a||(a=j),i||(i=[]);while(++r<o){var n=e[r];t>0&&a(n)?t>1?C(n,t-1,a,l,i):f(i,n):l||(i[i.length]=n)}return i}function x(e,t){return e=Object(e),$(e,t,(function(t,a){return a in e}))}function $(e,t,a){var l=-1,i=t.length,r={};while(++l<i){var o=t[l],n=e[o];a(n,o)&&(r[o]=n)}return r}function k(e,t){return t=w(void 0===t?e.length-1:t,0),function(){var a=arguments,l=-1,i=w(a.length-t,0),r=Array(i);while(++l<i)r[l]=a[t+l];l=-1;var o=Array(t+1);while(++l<t)o[l]=a[l];return o[t]=r,u(e,this,o)}}function j(e){return T(e)||R(e)||!!(g&&e&&e[g])}function O(e){if("string"==typeof e||B(e))return e;var t=e+"";return"0"==t&&1/e==-a?"-0":t}function R(e){return F(e)&&v.call(e,"callee")&&(!b.call(e,"callee")||m.call(e)==i)}var T=Array.isArray;function D(e){return null!=e&&V(e.length)&&!L(e)}function F(e){return S(e)&&D(e)}function L(e){var t=q(e)?m.call(e):"";return t==r||t==o}function V(e){return"number"==typeof e&&e>-1&&e%1==0&&e<=l}function q(e){var t=typeof e;return!!e&&("object"==t||"function"==t)}function S(e){return!!e&&"object"==typeof e}function B(e){return"symbol"==typeof e||S(e)&&m.call(e)==n}var P=k((function(e,t){return null==e?{}:x(e,p(C(t,1),O))}));e.exports=P}).call(this,a("c8ba"))},cf74:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("a-upload",{attrs:{name:"file",multiple:!0,action:e.uploadAction,headers:e.headers,data:{biz:e.bizPath},fileList:e.fileList,beforeUpload:e.beforeUpload,disabled:e.disabled,returnUrl:e.returnUrl},on:{change:e.handleChange}},[a("a-button",[a("a-icon",{attrs:{type:"upload"}}),e._v(e._s(e.text)+"\n  ")],1)],1)},i=[],r=(a("7f7f"),a("28a5"),a("a481"),a("3b2b"),a("2b0e")),o=a("9fb0"),n=a("0fea"),s="all",d="image",c="file",u=function(){return"-"+parseInt(1e4*Math.random()+1,10)},p=function(e){if(e.lastIndexOf("\\")>=0){var t=new RegExp("\\\\","g");e=e.replace(t,"/")}return e.substring(e.lastIndexOf("/")+1)},f={name:"JUpload",data:function(){return{uploadAction:window._CONFIG["domianURL"]+"/sys/common/upload",urlDownload:window._CONFIG["staticDomainURL"],headers:{},fileList:[],newFileList:[]}},props:{text:{type:String,required:!1,default:"点击上传"},fileType:{type:String,required:!1,default:s},bizPath:{type:String,required:!1,default:"temp"},value:{type:[String,Array],required:!1},disabled:{type:Boolean,required:!1,default:!1},triggerChange:{type:Boolean,required:!1,default:!1},returnUrl:{type:Boolean,required:!1,default:!0}},watch:{value:function(e){e instanceof Array?this.returnUrl?this.initFileList(e.join(",")):this.initFileListArr(e):this.initFileList(e)}},created:function(){var e=r["default"].ls.get(o["a"]);this.headers={"X-Access-Token":e}},methods:{initFileListArr:function(e){if(e&&0!=e.length){for(var t=[],a=0;a<e.length;a++)t.push({uid:u(),name:e[a].fileName,status:"done",url:e[a].filePath,response:{status:"history",message:e[a].filePath}});this.fileList=t}else this.fileList=[]},initFileList:function(e){if(e&&0!=e.length){for(var t=[],a=e.split(","),l=0;l<a.length;l++){var i=Object(n["d"])(a[l],this.urlDownload,"http");t.push({uid:u(),name:p(a[l]),status:"done",url:i,response:{status:"history",message:a[l]}})}this.fileList=t}else this.fileList=[]},handlePathChange:function(){var e=this.fileList,t="";e&&0!=e.length||(t="");for(var a=[],l=0;l<e.length;l++)a.push(e[l].response.message);a.length>0&&(t=a.join(",")),this.$emit("change",t)},beforeUpload:function(e){var t=e.type;if(t===d){if(t.indexOf("image")<0)return this.$message.warning("请上传图片"),!1}else if(t===c&&t.indexOf("image")>=0)return this.$message.warning("请上传文件"),!1;return!0},handleChange:function(e){var t=this,a=e.fileList;"done"===e.file.status?e.file.response.success&&(a=a.map((function(e){if(e.response){var a=e.response.message;e.url=Object(n["d"])(a,t.urlDownload,"http")}return e}))):"error"===e.file.status?this.$message.error("".concat(e.file.name," 上传失败.")):"removed"===e.file.status&&this.handleDelete(e.file),this.fileList=a,"done"!==e.file.status&&"removed"!==e.file.status||(this.returnUrl?this.handlePathChange():a=a.filter((function(e){return!!e.response&&!0===e.response.success})).map((function(e){var a={fileName:e.name,filePath:e.response.message,fileSize:e.size};t.newFileList.push(a),t.$emit("change",t.newFileList)})))},handleDelete:function(e){}},model:{prop:"value",event:"change"}},h=f,v=a("2877"),m=Object(v["a"])(h,l,i,!1,null,"793ad1c0",null);t["default"]=m.exports},d512:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("a-modal",{attrs:{title:e.title,width:1200,visible:e.visible,maskClosable:!1,confirmLoading:e.confirmLoading},on:{ok:e.handleOk,cancel:e.handleCancel}},[a("a-spin",{attrs:{spinning:e.confirmLoading}},[a("a-form",{attrs:{form:e.form}},[a("a-row",[a("a-col",{attrs:{span:12}},[a("a-form-item",{attrs:{label:"编码",labelCol:e.labelCol,wrapperCol:e.wrapperCol}},[a("a-input",{directives:[{name:"decorator",rawName:"v-decorator",value:["code",e.validatorRules.code],expression:"[ 'code', validatorRules.code]"}],attrs:{placeholder:"请输入编码"}})],1)],1),a("a-col",{attrs:{span:12}},[a("a-form-item",{attrs:{label:"名称",labelCol:e.labelCol,wrapperCol:e.wrapperCol}},[a("a-input",{directives:[{name:"decorator",rawName:"v-decorator",value:["name",e.validatorRules.name],expression:"[ 'name', validatorRules.name]"}],attrs:{placeholder:"请输入名称"}})],1)],1),a("a-col",{attrs:{span:12}},[a("a-form-item",{attrs:{label:"类型",labelCol:e.labelCol,wrapperCol:e.wrapperCol}},[a("j-dict-select-tag",{directives:[{name:"decorator",rawName:"v-decorator",value:["type",e.validatorRules.type],expression:"['type', validatorRules.type]"}],attrs:{type:"radio","trigger-change":!0,dictCode:"",placeholder:"请选择类型"}})],1)],1),a("a-col",{attrs:{span:12}},[a("a-form-item",{attrs:{label:"模型文件",labelCol:e.labelCol,wrapperCol:e.wrapperCol}},[a("j-upload",{directives:[{name:"decorator",rawName:"v-decorator",value:["threeModelFile",e.validatorRules.threeModelFile],expression:"['threeModelFile', validatorRules.threeModelFile]"}],attrs:{"trigger-change":!0}})],1)],1),a("a-col",{attrs:{span:12}},[a("a-form-item",{attrs:{label:"传输协议",labelCol:e.labelCol,wrapperCol:e.wrapperCol}},[a("j-dict-select-tag",{directives:[{name:"decorator",rawName:"v-decorator",value:["linkProtocolBy",e.validatorRules.linkProtocolBy],expression:"['linkProtocolBy', validatorRules.linkProtocolBy]"}],attrs:{type:"list","trigger-change":!0,dictCode:"",placeholder:"请选择传输协议"}})],1)],1),a("a-col",{attrs:{span:12}},[a("a-form-item",{attrs:{label:"数据协议",labelCol:e.labelCol,wrapperCol:e.wrapperCol}},[a("j-dict-select-tag",{directives:[{name:"decorator",rawName:"v-decorator",value:["dataProtocolBy",e.validatorRules.dataProtocolBy],expression:"['dataProtocolBy', validatorRules.dataProtocolBy]"}],attrs:{type:"list","trigger-change":!0,dictCode:"",placeholder:"请选择数据协议"}})],1)],1),a("a-col",{attrs:{span:12}},[a("a-form-item",{attrs:{label:"状态",labelCol:e.labelCol,wrapperCol:e.wrapperCol}},[a("j-dict-select-tag",{directives:[{name:"decorator",rawName:"v-decorator",value:["status",e.validatorRules.status],expression:"['status', validatorRules.status]"}],attrs:{type:"radio","trigger-change":!0,dictCode:"",placeholder:"请选择状态"}})],1)],1),a("a-col",{attrs:{span:24}},[a("a-form-item",{attrs:{label:"描述",labelCol:e.labelCol2,wrapperCol:e.wrapperCol2}},[a("a-textarea",{directives:[{name:"decorator",rawName:"v-decorator",value:["description",e.validatorRules.description],expression:"['description', validatorRules.description]"}],attrs:{rows:"4",placeholder:"请输入描述"}})],1)],1)],1)],1),a("a-tabs",{on:{change:e.handleChangeTabs},model:{value:e.activeKey,callback:function(t){e.activeKey=t},expression:"activeKey"}},[a("a-tab-pane",{key:e.refKeys[0],attrs:{tab:"数据节点",forceRender:!0}},[a("j-editable-table",{ref:e.refKeys[0],attrs:{loading:e.deviceDataTable.loading,columns:e.deviceDataTable.columns,dataSource:e.deviceDataTable.dataSource,maxHeight:300,rowNumber:!0,rowSelection:!0,actionButton:!0}})],1),a("a-tab-pane",{key:e.refKeys[1],attrs:{tab:"功能定义",forceRender:!0}},[a("j-editable-table",{ref:e.refKeys[1],attrs:{loading:e.deviceFunctionsTable.loading,columns:e.deviceFunctionsTable.columns,dataSource:e.deviceFunctionsTable.dataSource,maxHeight:300,rowNumber:!0,rowSelection:!0,actionButton:!0}})],1),a("a-tab-pane",{key:e.refKeys[2],attrs:{tab:"事件定义",forceRender:!0}},[a("j-editable-table",{ref:e.refKeys[2],attrs:{loading:e.deviceEventTable.loading,columns:e.deviceEventTable.columns,dataSource:e.deviceEventTable.dataSource,maxHeight:300,rowNumber:!0,rowSelection:!0,actionButton:!0}})],1),a("a-tab-pane",{key:e.refKeys[3],attrs:{tab:"数据标签",forceRender:!0}},[a("j-editable-table",{ref:e.refKeys[3],attrs:{loading:e.deviceLabelTable.loading,columns:e.deviceLabelTable.columns,dataSource:e.deviceLabelTable.dataSource,maxHeight:300,rowNumber:!0,rowSelection:!0,actionButton:!0}})],1)],1)],1)],1)},i=[],r=(a("8e6e"),a("456d"),a("bd86")),o=(a("ac6a"),a("5df3"),a("88bc")),n=a.n(o),s=a("e2e0"),d=a("7550"),c=a("0fea"),u={components:{JEditableTable:d["default"]},data:function(){return{title:"操作",visible:!1,form:this.$form.createForm(this),confirmLoading:!1,model:{},labelCol:{xs:{span:24},sm:{span:6}},wrapperCol:{xs:{span:24},sm:{span:18}}}},methods:{getAllTable:function(){var e=this;if(!(this.refKeys instanceof Array))throw this.throwNotArray("refKeys");var t=this.refKeys.map((function(t){return Object(s["c"])(e,t)}));return Promise.all(t)},eachAllTable:function(e){this.getAllTable().then((function(t){t.forEach((function(t,a){"function"===typeof e&&e(t,a)}))}))},add:function(){"function"===typeof this.addBefore&&this.addBefore();var e=this.addDefaultRowNum;"number"!==typeof e&&(e=1),this.eachAllTable((function(t){t.add(e)})),"function"===typeof this.addAfter&&this.addAfter(this.model),this.edit({})},edit:function(e){"function"===typeof this.editBefore&&this.editBefore(e),this.visible=!0,this.activeKey=this.refKeys[0],this.form.resetFields(),this.model=Object.assign({},e),"function"===typeof this.editAfter&&this.editAfter(this.model)},close:function(){this.visible=!1,this.eachAllTable((function(e){e.initialize()})),this.$emit("close")},requestSubTableData:function(e,t,a,l){a.loading=!0,Object(c["c"])(e,t).then((function(e){a.dataSource=e.result||[],"function"===typeof l&&l(e)})).finally((function(){a.loading=!1}))},request:function(e){var t=this,a=this.url.add,l="post";this.model.id&&(a=this.url.edit,l="put"),this.confirmLoading=!0,Object(c["h"])(a,e,l).then((function(e){e.success?(t.$message.success(e.message),t.$emit("ok"),t.close()):t.$message.warning(e.message)})).finally((function(){t.confirmLoading=!1}))},handleChangeTabs:function(e){Object(s["c"])(this,e).then((function(e){e.resetScrollTop()}))},handleCancel:function(){this.close()},handleOk:function(){var e=this;this.getAllTable().then((function(t){return Object(s["d"])(e.form,t)})).then((function(t){if("function"!==typeof e.classifyIntoFormData)throw e.throwNotFunction("classifyIntoFormData");var a=e.classifyIntoFormData(t);return e.request(a)})).catch((function(t){t.error===s["b"]&&(e.activeKey=null==t.index?e.activeKey:e.refKeys[t.index])}))},throwNotFunction:function(e){return"".concat(e," 未定义或不是一个函数")},throwNotArray:function(e){return"".concat(e," 未定义或不是一个数组")}}},p=a("ca00"),f=a("cf74"),h=a("7b16");function v(e,t){var a=Object.keys(e);if(Object.getOwnPropertySymbols){var l=Object.getOwnPropertySymbols(e);t&&(l=l.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),a.push.apply(a,l)}return a}function m(e){for(var t=1;t<arguments.length;t++){var a=null!=arguments[t]?arguments[t]:{};t%2?v(Object(a),!0).forEach((function(t){Object(r["a"])(e,t,a[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(a)):v(Object(a)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(a,t))}))}return e}var y={name:"DeviceModelModal",mixins:[u],components:{JUpload:f["default"],JDictSelectTag:h["default"]},data:function(){var e=this;return{labelCol:{span:6},wrapperCol:{span:16},labelCol2:{span:3},wrapperCol2:{span:20},addDefaultRowNum:1,validatorRules:{code:{rules:[{required:!0,message:"请输入编码!"},{validator:function(t,a,l){return Object(p["m"])("device_model","code",a,e.model.id,l)}}]},name:{rules:[{required:!0,message:"请输入名称!"},{validator:function(t,a,l){return Object(p["m"])("device_model","name",a,e.model.id,l)}}]},type:{rules:[{required:!0,message:"请输入类型!"}]},threeModelFile:{rules:[]},linkProtocolBy:{rules:[]},dataProtocolBy:{rules:[]},status:{rules:[{required:!0,message:"请输入状态!"}]},description:{rules:[]}},refKeys:["deviceData","deviceFunctions","deviceEvent","deviceLabel"],tableKeys:["deviceData","deviceFunctions","deviceEvent","deviceLabel"],activeKey:"deviceData",deviceDataTable:{loading:!1,dataSource:[],columns:[{title:"标识",key:"code",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"名称",key:"name",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"数据类型",key:"type",type:s["a"].sel_search,dictCode:"",width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"数值范围",key:"rangeMax",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"正常数值范围",key:"rangeSafety",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"精度",key:"accuracy",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"布尔值",key:"boolValue",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"单位",key:"unit",type:s["a"].sel_search,dictCode:"",width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"是否只读",key:"readonly",type:s["a"].select,dictCode:"",width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"描述",key:"description",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""}]},deviceFunctionsTable:{loading:!1,dataSource:[],columns:[{title:"设备模型",key:"deviceModelBy",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"标识",key:"code",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"功能名称",key:"name",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"是否异步",key:"isAsync",type:s["a"].list_multi,dictCode:"",width:"250px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"输入类型",key:"inputType",type:s["a"].sel_search,dictCode:"",width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"输入参数",key:"inputParamsBy",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"输出数据",key:"outputData",type:s["a"].select,dictCode:"",width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"描述",key:"description",type:s["a"].list_multi,dictCode:"",width:"250px",placeholder:"请输入${title}",defaultValue:""}]},deviceEventTable:{loading:!1,dataSource:[],columns:[{title:"标识",key:"code",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"名称",key:"name",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"事件类型",key:"type",type:s["a"].select,dictCode:"",width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"事件级别",key:"level",type:s["a"].select,dictCode:"",width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"输出数据",key:"outputDataBy",type:s["a"].select,dictCode:"device_data,name,id",width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"描述",key:"description",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""}]},deviceLabelTable:{loading:!1,dataSource:[],columns:[{title:"标识",key:"code",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"名称",key:"name",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"数据节点",key:"deviceDataBy",type:s["a"].sel_search,dictCode:"device_data,name,id",width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"条件规则",key:"rules",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"规则数值",key:"rulesValue",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:""},{title:"颜色标识",key:"color",type:s["a"].input,width:"200px",placeholder:"请输入${title}",defaultValue:"",validateRules:[{required:!0,message:"${title}不能为空"}]},{title:"描述",key:"description",type:s["a"].list_multi,dictCode:"",width:"250px",placeholder:"请输入${title}",defaultValue:""}]},url:{add:"/device/deviceModel/add",edit:"/device/deviceModel/edit",deviceData:{list:"/device/deviceModel/queryDeviceDataByMainId"},deviceFunctions:{list:"/device/deviceModel/queryDeviceFunctionsByMainId"},deviceEvent:{list:"/device/deviceModel/queryDeviceEventByMainId"},deviceLabel:{list:"/device/deviceModel/queryDeviceLabelByMainId"}}}},methods:{getAllTable:function(){var e=this,t=this.tableKeys.map((function(t){return Object(s["c"])(e,t)}));return Promise.all(t)},editAfter:function(){var e=this,t=n()(this.model,"code","name","type","threeModelFile","linkProtocolBy","dataProtocolBy","createTime","status","description");if(this.$nextTick((function(){e.form.setFieldsValue(t)})),this.model.id){var a={id:this.model.id};this.requestSubTableData(this.url.deviceData.list,a,this.deviceDataTable),this.requestSubTableData(this.url.deviceFunctions.list,a,this.deviceFunctionsTable),this.requestSubTableData(this.url.deviceEvent.list,a,this.deviceEventTable),this.requestSubTableData(this.url.deviceLabel.list,a,this.deviceLabelTable)}},classifyIntoFormData:function(e){var t=Object.assign(this.model,e.formValue);return m({},t,{deviceDataList:e.tablesValue[0].values,deviceFunctionsList:e.tablesValue[1].values,deviceEventList:e.tablesValue[2].values,deviceLabelList:e.tablesValue[3].values})},validateError:function(e){this.$message.error(e)},popupCallback:function(e){this.form.setFieldsValue(n()(e,"code","name","type","threeModelFile","linkProtocolBy","dataProtocolBy","createTime","status","description"))}}},b=y,g=a("2877"),w=Object(g["a"])(b,l,i,!1,null,"2d3998dc",null);t["default"]=w.exports}}]);