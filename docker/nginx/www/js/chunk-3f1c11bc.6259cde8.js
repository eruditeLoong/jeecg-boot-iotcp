(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3f1c11bc","chunk-57bb7720","chunk-2d238446","chunk-2d0f05f3","chunk-2d0a3776","chunk-4d3747a6","chunk-5eea6bce","chunk-12711276","chunk-2d0df843","chunk-2d0df843"],{"01fe":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("j-editable-table",{attrs:{columns:e.columns,dataSource:e.dataSource,rowNumber:!0,actionButton:!0,rowSelection:!0,maxHeight:400},on:{valueChange:e.handleValueChange}})},n=[],r=a("e2e0"),u=a("7550"),i={name:"ThreeLinkage",components:{JEditableTable:u["default"]},data:function(){return{columns:[{title:"省/直辖市/自治区",key:"s1",type:r["a"].select,width:"240px",options:[],placeholder:"请选择${title}"},{title:"市",key:"s2",type:r["a"].select,width:"240px",options:[],placeholder:"请选择${title}"},{title:"县/区",key:"s3",type:r["a"].select,width:"240px",options:[],placeholder:"请选择${title}"}],dataSource:[],mockData:[{label:"北京市",value:"110000",parent:null},{label:"天津市",value:"120000",parent:null},{label:"河北省",value:"130000",parent:null},{label:"上海市",value:"310000",parent:null},{label:"北京市",value:"110100",parent:"110000"},{label:"天津市市",value:"120100",parent:"120000"},{label:"石家庄市",value:"130100",parent:"130000"},{label:"唐山市",value:"130200",parent:"130000"},{label:"秦皇岛市",value:"130300",parent:"130000"},{label:"上海市",value:"310100",parent:"310000"},{label:"东城区",value:"110101",parent:"110100"},{label:"西城区",value:"110102",parent:"110100"},{label:"朝阳区",value:"110105",parent:"110100"},{label:"和平区",value:"120101",parent:"120000"},{label:"河东区",value:"120102",parent:"120000"},{label:"河西区",value:"120103",parent:"120000"},{label:"黄浦区",value:"310101",parent:"310100"},{label:"徐汇区",value:"310104",parent:"310100"},{label:"长宁区",value:"310105",parent:"310100"},{label:"长安区",value:"130102",parent:"130100"},{label:"桥西区",value:"130104",parent:"130100"},{label:"新华区",value:"130105",parent:"130100"},{label:"路南区",value:"130202",parent:"130200"},{label:"路北区",value:"130203",parent:"130200"},{label:"古冶区",value:"130204",parent:"130200"},{label:"海港区",value:"130302",parent:"130300"},{label:"山海关区",value:"130303",parent:"130300"},{label:"北戴河区",value:"130304",parent:"130300"}]}},mounted:function(){this.columns[0].options=this.request(null)},methods:{request:function(e){return this.mockData.filter((function(t){return t.parent===e}))},handleValueChange:function(e){var t=e.type,a=e.row,l=e.column,n=e.value,u=e.target;t===r["a"].select&&("s1"===l.key?(this.columns[1].options=this.request(n),u.setValues([{rowKey:a.id,values:{s2:"",s3:""}}]),this.columns[2].options=[]):"s2"===l.key&&(this.columns[2].options=this.request(n),u.setValues([{rowKey:a.id,values:{s3:""}}])))}}},o=i,c=a("2877"),s=Object(c["a"])(o,l,n,!1,null,"bd50bcca",null);t["default"]=s.exports},"89f2":function(e,t,a){"use strict";a.d(t,"c",(function(){return r})),a.d(t,"a",(function(){return i})),a.d(t,"b",(function(){return o}));a("96cf"),a("28a5"),a("6b54"),a("ac4d"),a("8a81"),a("ac6a");var l=a("3b8d"),n=a("4ec3");a("0fea");function r(e){return u.apply(this,arguments)}function u(){return u=Object(l["a"])(regeneratorRuntime.mark((function e(t){var a;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(t){e.next=2;break}return e.abrupt("return","字典Code不能为空!");case 2:return e.next=4,Object(n["f"])(t);case 4:return a=e.sent,e.abrupt("return",a);case 6:case"end":return e.stop()}}),e)}))),u.apply(this,arguments)}function i(e,t){if(e instanceof Array){var a=!0,l=!1,n=void 0;try{for(var r,u=e[Symbol.iterator]();!(a=(r=u.next()).done);a=!0){var i=r.value;if(t===i.value)return i.text}}catch(o){l=!0,n=o}finally{try{a||null==u.return||u.return()}finally{if(l)throw n}}}return t}function o(e,t){if(0===t||"0"===t){var a=!0,l=!1,n=void 0;try{for(var r,u=e[Symbol.iterator]();!(a=(r=u.next()).done);a=!0){var i=r.value;if(t==i.value)return i.text}}catch(s){l=!0,n=s}finally{try{a||null==u.return||u.return()}finally{if(l)throw n}}}if(!t||!e||0==e.length)return"";var o="";t=t.toString();var c=t.split(",");return e.forEach((function(e){for(var t=0;t<c.length;t++)if(c[t]===e.value){o+=e.text+",";break}})),""==o?t:o.substring(0,o.length-1)}},"9ba5":function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("j-editable-table",{attrs:{columns:e.columns,dataSource:e.dataSource,rowNumber:!0,rowSelection:!0,maxHeight:400,disabled:!0}})},n=[],r=a("e2e0"),u=a("7550"),i={name:"ReadOnlyTable",components:{JEditableTable:u["default"]},data:function(){return{columns:[{title:"输入框",key:"input",type:r["a"].input,placeholder:"清输入"},{title:"下拉框",key:"select",type:r["a"].select,options:[{title:"String",value:"string"},{title:"Integer",value:"int"},{title:"Double",value:"double"},{title:"Boolean",value:"boolean"}],placeholder:"请选择"},{title:"多选框",key:"checkbox",type:r["a"].checkbox,customValue:[!0,!1]},{title:"日期",key:"datetime",type:r["a"].datetime}],dataSource:[{input:"hello",select:"int",checkbox:!0,datetime:"2019-6-17 14:50:48"},{input:"world",select:"string",checkbox:!1,datetime:"2019-6-16 14:50:48"},{input:"one",select:"double",checkbox:!0,datetime:"2019-6-17 15:50:48"},{input:"two",select:"boolean",checkbox:!1,datetime:"2019-6-14 14:50:48"},{input:"three",select:"",checkbox:!1,datetime:"2019-6-13 14:50:48"}]}},mounted:function(){}},o=i,c=a("2877"),s=Object(c["a"])(o,l,n,!1,null,"967c037a",null);t["default"]=s.exports},b020:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("a-card",{attrs:{bordered:!1}},[a("a-tabs",[a("a-tab-pane",{key:"1",attrs:{tab:"普通列表"}},[a("default-table")],1),a("a-tab-pane",{key:"2",attrs:{tab:"只读列表"}},[a("read-only-table")],1),a("a-tab-pane",{key:"3",attrs:{tab:"三级联动"}},[a("three-linkage")],1)],1)],1)},n=[],r=a("ff3c"),u=a("9ba5"),i=a("01fe"),o={name:"JeecgEditableTableExample",components:{DefaultTable:r["default"],ReadOnlyTable:u["default"],ThreeLinkage:i["default"]},data:function(){return{}},methods:{}},c=o,s=a("2877"),d=Object(s["a"])(c,l,n,!1,null,"7e88b5ec",null);t["default"]=d.exports},ff3c:function(e,t,a){"use strict";a.r(t);var l=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("a-button",{attrs:{type:"primary"},on:{click:e.handleTableCheck}},[e._v("表单验证")]),a("span",{staticStyle:{"padding-left":"8px"}}),a("a-tooltip",{attrs:{placement:"top",title:"获取值，忽略表单验证",autoAdjustOverflow:!0}},[a("a-button",{attrs:{type:"primary"},on:{click:e.handleTableGet}},[e._v("获取值")])],1),a("span",{staticStyle:{"padding-left":"8px"}}),a("a-tooltip",{attrs:{placement:"top",title:"模拟加载1000条数据",autoAdjustOverflow:!0}},[a("a-button",{attrs:{type:"primary"},on:{click:e.handleTableSet}},[e._v("设置值")])],1),a("j-editable-table",{ref:"editableTable",staticStyle:{"margin-top":"8px"},attrs:{loading:e.loading,columns:e.columns,dataSource:e.dataSource,rowNumber:!0,rowSelection:!0,actionButton:!0,dragSort:!0},on:{selectRowChange:e.handleSelectRowChange},scopedSlots:e._u([{key:"action",fn:function(t){return[a("a",{on:{click:function(a){return e.handleDelete(t)}}},[e._v(e._s(t.text))])]}}])})],1)},n=[],r=a("c1df"),u=a.n(r),i=a("e2e0"),o=a("ca00"),c=a("7550"),s={name:"DefaultTable",components:{JEditableTable:c["default"]},data:function(){return{loading:!1,columns:[{title:"字段名称",key:"dbFieldName",width:"300px",type:i["a"].input,defaultValue:"",placeholder:"请输入${title}",validateRules:[{required:!0,message:"请输入${title}"},{pattern:/^[a-z|A-Z][a-z|A-Z\d_-]{0,}$/,message:"${title}必须以字母开头，可包含数字、下划线、横杠"},{unique:!0,message:"${title}不能重复"},{handler:function(e,t,a,l,n,r){"blur"===e&&"abc"===t?n(!1,"${title}不能是abc"):n(!0)},message:"${title}默认提示"}]},{title:"文件域",key:"upload",type:i["a"].upload,width:"300px",placeholder:"点击上传",token:!0,responseName:"message",action:window._CONFIG["domianURL"]+"/sys/common/upload"},{title:"字段类型",key:"dbFieldType",width:"300px",type:i["a"].select,options:[{title:"String",value:"string"},{title:"Integer",value:"int"},{title:"Double",value:"double"},{title:"Boolean",value:"boolean"}],allowInput:!0,defaultValue:"",placeholder:"请选择${title}",validateRules:[{required:!0,message:"请选择${title}"}]},{title:"性别（字典）",key:"sex_dict",width:"300px",type:i["a"].select,options:[],dictCode:"sex",placeholder:"请选择${title}",validateRules:[{required:!0,message:"请选择${title}"}]},{title:"多选测试",key:"multipleSelect",width:"300px",type:i["a"].select,props:{mode:"multiple"},options:[{title:"String",value:"string"},{title:"Integer",value:"int"},{title:"Double",value:"double"},{title:"Boolean",value:"boolean"}],defaultValue:["int","boolean"],placeholder:"这里可以多选",validateRules:[{required:!0,message:"请选择${title}"}]},{title:"字段长度",key:"dbLength",width:"100px",type:i["a"].inputNumber,defaultValue:32,placeholder:"${title}",validateRules:[{required:!0,message:"请输入${title}"}]},{title:"日期",key:"datetime",width:"320px",type:i["a"].datetime,defaultValue:"2019-4-30 14:52:22",placeholder:"请选择${title}",validateRules:[{required:!0,message:"请选择${title}"}]},{title:"可以为空",key:"isNull",width:"100px",type:i["a"].checkbox,customValue:["Y","N"],defaultChecked:!1},{title:"操作",key:"action",width:"100px",type:i["a"].slot,slotName:"action",defaultValue:"删除"}],dataSource:[],selectedRowIds:[]}},mounted:function(){this.randomData(23,!1)},methods:{handleTableCheck:function(){var e=this;this.$refs.editableTable.getValues((function(t){0===t?e.$message.success("验证通过"):e.$message.error("验证未通过")}))},handleTableGet:function(){this.$refs.editableTable.getValues((function(e,t){}),!1),this.$message.info("获取值成功，请看控制台输出")},handleTableSet:function(){this.randomData(1e3,!0)},handleSelectRowChange:function(e){this.selectedRowIds=e},randomData:function(e){var t=this,a=arguments.length>1&&void 0!==arguments[1]&&arguments[1];a&&(this.loading=!0);for(var l=function(){var e=parseInt(Object(o["g"])(1e3,9999999999999));return u()(new Date(e)).format("YYYY-MM-DD HH:mm:ss")},n=Date.now(),r=[],i=0;i<e;i++)r.push({id:Object(o["i"])(),dbFieldName:"name_".concat(i+1),multipleSelect:["string",["int","double","boolean"][Object(o["g"])(0,2)]],dbFieldType:["string","int","double","boolean"][Object(o["g"])(0,3)],dbLength:Object(o["g"])(0,233),datetime:l(),isNull:["Y","N"][Object(o["g"])(0,1)]});this.dataSource=r;var c=Date.now(),s=c-n;a&&s<e&&setTimeout((function(){t.loading=!1}),e-s)},handleDelete:function(e){var t=e.rowId,a=e.target;a.removeRows(t)}}},d=s,p=a("2877"),b=Object(p["a"])(d,l,n,!1,null,"adf2922e",null);t["default"]=b.exports}}]);