(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-7d13f0a2"],{"7c93":function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a-modal",{attrs:{title:"导入EXCEL",width:600,visible:e.visible,confirmLoading:e.uploading},on:{cancel:e.handleClose}},[n("a-upload",{attrs:{name:"file",multiple:!0,accept:".xls,.xlsx",fileList:e.fileList,remove:e.handleRemove,beforeUpload:e.beforeUpload}},[n("a-button",[n("a-icon",{attrs:{type:"upload"}}),e._v("\n      选择导入文件\n    ")],1)],1),n("template",{slot:"footer"},[n("a-button",{on:{click:e.handleClose}},[e._v("关闭")]),n("a-button",{attrs:{type:"primary",disabled:0===e.fileList.length,loading:e.uploading},on:{click:e.handleImport}},[e._v("\n      "+e._s(e.uploading?"上传中...":"开始上传")+"\n    ")])],1)],2)},o=[],i=(n("ac6a"),n("75fc")),s=n("0fea"),r={name:"JImportModal",props:{url:{type:String,default:"",required:!1},biz:{type:String,default:"",required:!1}},data:function(){return{visible:!1,uploading:!1,fileList:[],uploadAction:"",foreignKeys:""}},watch:{url:function(e){e&&(this.uploadAction=window._CONFIG["domianURL"]+e)}},created:function(){this.uploadAction=window._CONFIG["domianURL"]+this.url},methods:{handleClose:function(){this.visible=!1},show:function(e){this.fileList=[],this.uploading=!1,this.visible=!0,this.foreignKeys=e},handleRemove:function(e){var t=this.fileList.indexOf(e),n=this.fileList.slice();n.splice(t,1),this.fileList=n},beforeUpload:function(e){return this.fileList=[].concat(Object(i["a"])(this.fileList),[e]),!1},handleImport:function(){var e=this,t=this.fileList,n=new FormData;this.biz&&n.append("isSingleTableImport",this.biz),this.foreignKeys&&this.foreignKeys.length>0&&n.append("foreignKeys",this.foreignKeys),t.forEach((function(e){n.append("files[]",e)})),this.uploading=!0,Object(s["i"])(this.uploadAction,n).then((function(t){e.uploading=!1,t.success?(e.$message.success(t.message),e.visible=!1,e.$emit("ok")):e.$message.warning(t.message)}))}}},_=r,c=n("2877"),l=Object(c["a"])(_,a,o,!1,null,"495e2a21",null);t["default"]=l.exports},"8d02":function(e,t,n){},9937:function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("a-card",{staticStyle:{height:"100%"},attrs:{bordered:!1}},[n("div",{staticClass:"table-page-search-wrapper"},[n("a-form",{attrs:{layout:"inline"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.searchByquery(t)}}},[e.queryInfo&&e.queryInfo.length>0?n("a-row",{attrs:{gutter:24}},[e._l(e.queryInfo,(function(t,a){return["1"===t.hidden?["datetime"==t.view?n("a-col",{directives:[{name:"show",rawName:"v-show",value:e.toggleSearchStatus,expression:"toggleSearchStatus"}],key:"query"+a,attrs:{md:12,sm:16}},[n("online-query-form-item",{attrs:{queryParam:e.queryParam,item:t,dictOptions:e.dictOptions}})],1):n("a-col",{directives:[{name:"show",rawName:"v-show",value:e.toggleSearchStatus,expression:"toggleSearchStatus"}],key:"query"+a,attrs:{md:6,sm:8}},[n("online-query-form-item",{attrs:{queryParam:e.queryParam,item:t,dictOptions:e.dictOptions}})],1)]:["datetime"==t.view?n("a-col",{key:"query"+a,attrs:{md:12,sm:16}},[n("online-query-form-item",{attrs:{queryParam:e.queryParam,item:t,dictOptions:e.dictOptions}})],1):n("a-col",{key:"query"+a,attrs:{md:6,sm:8}},[n("online-query-form-item",{attrs:{queryParam:e.queryParam,item:t,dictOptions:e.dictOptions}})],1)]]})),n("a-col",{attrs:{md:6,sm:8}},[n("span",{staticClass:"table-page-search-submitButtons",staticStyle:{float:"left",overflow:"hidden"}},[n("a-button",{attrs:{type:"primary",icon:"search"},on:{click:e.searchByquery}},[e._v("查询")]),n("a-button",{staticStyle:{"margin-left":"8px"},attrs:{type:"primary",icon:"reload"},on:{click:e.searchReset}},[e._v("重置")]),n("a",{staticStyle:{"margin-left":"8px"},on:{click:e.handleToggleSearch}},[e._v("\n              "+e._s(e.toggleSearchStatus?"收起":"展开")+"\n              "),n("a-icon",{attrs:{type:e.toggleSearchStatus?"up":"down"}})],1)],1)])],2):e._e()],1)],1),n("div",{staticClass:"table-operator"},[e.buttonSwitch.add?n("a-button",{attrs:{type:"primary",icon:"plus"},on:{click:e.handleAdd}},[e._v("新增")]):e._e(),e.buttonSwitch.import?n("a-button",{attrs:{type:"primary",icon:"upload"},on:{click:e.handleImportXls}},[e._v("导入")]):e._e(),e.buttonSwitch.export?n("a-button",{attrs:{type:"primary",icon:"download"},on:{click:e.handleExportXls}},[e._v("导出")]):e._e(),e._l(e.cgButtonList,(function(t,a){return e.cgButtonList&&e.cgButtonList.length>0?["js"==t.optType?n("a-button",{key:"cgbtn"+a,attrs:{type:"primary",icon:t.buttonIcon},on:{click:function(n){return e.cgButtonJsHandler(t.buttonCode)}}},[e._v("\n        "+e._s(t.buttonName)+"\n      ")]):"action"==t.optType?n("a-button",{key:"cgbtn"+a,attrs:{type:"primary",icon:t.buttonIcon},on:{click:function(n){return e.cgButtonActionHandler(t.buttonCode)}}},[e._v("\n        "+e._s(t.buttonName)+"\n      ")]):e._e()]:e._e()})),n("j-super-query",{ref:"superQuery",attrs:{fieldList:e.superQuery.fieldList,saveCode:e.$route.fullPath,loading:e.table.loading},on:{handleSuperQuery:e.handleSuperQuery}}),e.buttonSwitch.batch_delete?n("a-button",{directives:[{name:"show",rawName:"v-show",value:e.table.selectedRowKeys.length>0,expression:"table.selectedRowKeys.length > 0"}],attrs:{ghost:"",type:"primary",icon:"delete"},on:{click:e.handleDelBatch}},[e._v("批量删除")]):e._e()],2),n("div",[n("div",{staticClass:"ant-alert ant-alert-info",staticStyle:{"margin-bottom":"16px"}},[n("i",{staticClass:"anticon anticon-info-circle ant-alert-icon"}),e._v("\n      已选择 "),n("a",{staticStyle:{"font-weight":"600"}},[e._v(e._s(e.table.selectedRowKeys.length))]),e._v("项  \n      "),n("a",{staticStyle:{"margin-left":"24px"},on:{click:e.onClearSelected}},[e._v("清空")])]),n("a-table",{ref:"cgformAutoList",staticStyle:{"min-height":"300px"},attrs:{bordered:"",size:"middle",rowKey:"id",columns:e.table.columns,dataSource:e.table.dataSource,pagination:e.table.pagination,loading:e.table.loading,rowSelection:e.rowSelectionConfig,scroll:e.table.scroll},on:{change:e.handleTableChange},scopedSlots:e._u([e._l(e.fieldHrefSlots,(function(t){return{key:t.slotName,fn:function(a,o){return[n("a",{on:{click:function(n){return e.handleClickFieldHref(t,o)}}},[e._v(e._s(a))])]}}})),{key:"dateSlot",fn:function(t){return[n("span",[e._v(e._s(e.getFormatDate(t)))])]}},{key:"htmlSlot",fn:function(t){return[n("div",{domProps:{innerHTML:e._s(t)}})]}},{key:"imgSlot",fn:function(t){return[t?n("img",{staticStyle:{"max-width":"80px","font-size":"12px","font-style":"italic"},attrs:{src:e.getImgView(t),height:"25px",alt:"图片不存在"}}):n("span",{staticStyle:{"font-size":"12px","font-style":"italic"}},[e._v("无图片")])]}},{key:"fileSlot",fn:function(t){return[t?n("a-button",{attrs:{ghost:!0,type:"primary",icon:"download",size:"small"},on:{click:function(n){return e.downloadRowFile(t)}}},[e._v("\n          下载\n        ")]):n("span",{staticStyle:{"font-size":"12px","font-style":"italic"}},[e._v("无文件")])]}},{key:"action",fn:function(t,a){return n("span",{},[e.hasBpmStatus?["1"==a.bpm_status||""==a.bpm_status||null==a.bpm_status?[e.buttonSwitch.update?[n("a",{on:{click:function(t){return e.handleEdit(a)}}},[e._v("编辑")]),n("a-divider",{attrs:{type:"vertical"}})]:e._e()]:e._e()]:[e.buttonSwitch.update?[n("a",{on:{click:function(t){return e.handleEdit(a)}}},[e._v("编辑")]),n("a-divider",{attrs:{type:"vertical"}})]:e._e()],n("a-dropdown",[n("a",{staticClass:"ant-dropdown-link"},[e._v("\n            更多 "),n("a-icon",{attrs:{type:"down"}})],1),n("a-menu",{attrs:{slot:"overlay"},slot:"overlay"},[e.buttonSwitch.detail?n("a-menu-item",[n("a",{attrs:{href:"javascript:;"},on:{click:function(t){return e.handleDetail(a)}}},[e._v("详情")])]):e._e(),e.hasBpmStatus?["1"==a.bpm_status||""==a.bpm_status||null==a.bpm_status?[e.buttonSwitch.delete?n("a-menu-item",[n("a-popconfirm",{attrs:{title:"确定删除吗?"},on:{confirm:function(){return e.handleDeleteOne(a)}}},[n("a",[e._v("删除")])])],1):e._e()]:e._e()]:[e.buttonSwitch.delete?n("a-menu-item",[n("a-popconfirm",{attrs:{title:"确定删除吗?"},on:{confirm:function(){return e.handleDeleteOne(a)}}},[n("a",[e._v("删除")])])],1):e._e()],e._l(e.cgButtonLinkList,(function(t,o){return e.cgButtonLinkList&&e.cgButtonLinkList.length>0?[e.showLinkButton(t,a)?n("a-menu-item",{key:"cgbtnLink"+o},[n("a",{attrs:{href:"javascript:void(0);"},on:{click:function(n){return e.cgButtonLinkHandler(a,t.buttonCode,t.optType)}}},[t.buttonIcon?n("a-icon",{attrs:{type:t.buttonIcon}}):e._e(),e._v("\n                  "+e._s(t.buttonName)+"\n                ")],1)]):e._e()]:e._e()}))],2)],1)],2)}}],null,!0)}),n("onl-cgform-auto-modal",{ref:"modal",attrs:{code:e.code},on:{success:e.handleFormSuccess,schema:e.handleGetSchema}}),n("j-import-modal",{ref:"importModal",attrs:{url:e.getImportUrl()},on:{ok:e.importOk}}),n("a-modal",e._g(e._b({},"a-modal",e.hrefComponent.model,!1),e.hrefComponent.on),[n(e.hrefComponent.is,e._b({tag:"component"},"component",e.hrefComponent.params,!1))],1)],1)])},o=[],i=n("c03d"),s=i["a"],r=(n("ca2d"),n("ee8f"),n("2877")),_=Object(r["a"])(s,a,o,!1,null,"0f23c040",null);t["default"]=_.exports},"9c08":function(e,t,n){},c03d:function(module,__webpack_exports__,__webpack_require__){"use strict";var core_js_modules_es7_object_get_own_property_descriptors__WEBPACK_IMPORTED_MODULE_0__=__webpack_require__("8e6e"),core_js_modules_es7_object_get_own_property_descriptors__WEBPACK_IMPORTED_MODULE_0___default=__webpack_require__.n(core_js_modules_es7_object_get_own_property_descriptors__WEBPACK_IMPORTED_MODULE_0__),core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_1__=__webpack_require__("456d"),core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_1___default=__webpack_require__.n(core_js_modules_es6_object_keys__WEBPACK_IMPORTED_MODULE_1__),core_js_modules_es6_number_constructor__WEBPACK_IMPORTED_MODULE_2__=__webpack_require__("c5f6"),core_js_modules_es6_number_constructor__WEBPACK_IMPORTED_MODULE_2___default=__webpack_require__.n(core_js_modules_es6_number_constructor__WEBPACK_IMPORTED_MODULE_2__),_Users_zhouwenrong_IdeaProjects_jeecg_boot_ant_design_vue_node_modules_babel_runtime_corejs2_helpers_esm_toConsumableArray__WEBPACK_IMPORTED_MODULE_3__=__webpack_require__("75fc"),core_js_modules_es6_regexp_replace__WEBPACK_IMPORTED_MODULE_4__=__webpack_require__("a481"),core_js_modules_es6_regexp_replace__WEBPACK_IMPORTED_MODULE_4___default=__webpack_require__.n(core_js_modules_es6_regexp_replace__WEBPACK_IMPORTED_MODULE_4__),core_js_modules_es6_string_starts_with__WEBPACK_IMPORTED_MODULE_5__=__webpack_require__("f559"),core_js_modules_es6_string_starts_with__WEBPACK_IMPORTED_MODULE_5___default=__webpack_require__.n(core_js_modules_es6_string_starts_with__WEBPACK_IMPORTED_MODULE_5__),_Users_zhouwenrong_IdeaProjects_jeecg_boot_ant_design_vue_node_modules_babel_runtime_corejs2_helpers_esm_defineProperty__WEBPACK_IMPORTED_MODULE_6__=__webpack_require__("bd86"),core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_7__=__webpack_require__("ac4d"),core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_7___default=__webpack_require__.n(core_js_modules_es7_symbol_async_iterator__WEBPACK_IMPORTED_MODULE_7__),core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_8__=__webpack_require__("8a81"),core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_8___default=__webpack_require__.n(core_js_modules_es6_symbol__WEBPACK_IMPORTED_MODULE_8__),core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_9__=__webpack_require__("ac6a"),core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_9___default=__webpack_require__.n(core_js_modules_web_dom_iterable__WEBPACK_IMPORTED_MODULE_9__),core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_10__=__webpack_require__("6762"),core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_10___default=__webpack_require__.n(core_js_modules_es7_array_includes__WEBPACK_IMPORTED_MODULE_10__),_mixins_OnlAutoListMixin__WEBPACK_IMPORTED_MODULE_11__=__webpack_require__("b5ef"),_api_manage__WEBPACK_IMPORTED_MODULE_12__=__webpack_require__("0fea"),_components_dict_JDictSelectUtil__WEBPACK_IMPORTED_MODULE_13__=__webpack_require__("89f2"),_utils_util__WEBPACK_IMPORTED_MODULE_14__=__webpack_require__("ca00"),_components_jeecg_JImportModal__WEBPACK_IMPORTED_MODULE_15__=__webpack_require__("7c93"),_comp_jeecg_JSuperQuery__WEBPACK_IMPORTED_MODULE_16__=__webpack_require__("8c6e");function ownKeys(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function _objectSpread(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?ownKeys(Object(n),!0).forEach((function(t){Object(_Users_zhouwenrong_IdeaProjects_jeecg_boot_ant_design_vue_node_modules_babel_runtime_corejs2_helpers_esm_defineProperty__WEBPACK_IMPORTED_MODULE_6__["a"])(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):ownKeys(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}__webpack_exports__["a"]={name:"OnlCgFormAutoList",mixins:[_mixins_OnlAutoListMixin__WEBPACK_IMPORTED_MODULE_11__["a"]],components:{JSuperQuery:_comp_jeecg_JSuperQuery__WEBPACK_IMPORTED_MODULE_16__["default"],JImportModal:_components_jeecg_JImportModal__WEBPACK_IMPORTED_MODULE_15__["default"]},data:function(){return{code:"",description:"在线报表功能测试页面",currentTableName:"",url:{getQueryInfo:"/online/cgform/api/getQueryInfo/",getColumns:"/online/cgform/api/getColumns/",getData:"/online/cgform/api/getData/",optPre:"/online/cgform/api/form/",exportXls:"/online/cgform/api/exportXls/",buttonAction:"/online/cgform/api/doButton"},flowCodePre:"onl_",isorter:{column:"createTime",order:"desc"},dictOptions:{},cgButtonLinkList:[],cgButtonList:[],queryInfo:[],queryParamsMap:{},toggleSearchStatus:!1,table:{loading:!0,scroll:{x:!1},columns:[],dataSource:[],selectedRowKeys:[],selectionRows:[],pagination:{}},metaPagination:{current:1,pageSize:10,pageSizeOptions:["10","20","30"],showTotal:function(e,t){return t[0]+"-"+t[1]+" 共"+e+"条"},showQuickJumper:!0,showSizeChanger:!0,total:0},actionColumn:{title:"操作",dataIndex:"action",scopedSlots:{customRender:"action"},fixed:"right",align:"center",width:150},formTemplate:"99",EnhanceJS:"",hideColumns:[],buttonSwitch:{add:!0,update:!0,delete:!0,batch_delete:!0,import:!0,export:!0,detail:!0},hasBpmStatus:!1,checkboxFlag:!1,superQuery:{fieldList:[],params:"",matchType:"and"}}},created:function(){this.initAutoList()},mounted:function(){this.cgButtonJsHandler("mounted")},watch:{$route:function(){this.initAutoList()}},computed:{rowSelectionConfig:function(){return this.checkboxFlag?{fixed:!0,selectedRowKeys:this.table.selectedRowKeys,onChange:this.handleChangeInTableSelect}:null},queryParam:{get:function(){return this.queryParamsMap[this.code]},set:function(e){this.$set(this.queryParamsMap,this.code,e)}}},methods:{hasBpmStatusFilter:function(){var e=this.table.columns,t=[],n=!0,a=!1,o=void 0;try{for(var i,s=e[Symbol.iterator]();!(n=(i=s.next()).done);n=!0){var r=i.value;t.push(r.dataIndex)}}catch(_){a=!0,o=_}finally{try{n||null==s.return||s.return()}finally{if(a)throw o}}t.includes("bpm_status")||t.includes("BPM_STATUS")?this.hasBpmStatus=!0:this.hasBpmStatus=!1},initQueryInfo:function(){var e=this;Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["c"])("".concat(this.url.getQueryInfo).concat(this.code)).then((function(t){t.success?e.queryInfo=t.result:e.$message.warning(t.message)}))},initAutoList:function(){var e=this;if(!this.$route.params.code)return!1;this.superQuery.params="",this.$refs.superQuery&&this.$refs.superQuery.handleReset(),this.table.loading=!0,this.code=this.$route.params.code,this.queryParam||(this.queryParam={}),Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["c"])("".concat(this.url.getColumns).concat(this.code)).then((function(t){if(t.success){"Y"==t.result.checkboxFlag?e.checkboxFlag=!0:e.checkboxFlag=!1,"Y"==t.result.paginationFlag?e.table.pagination=_objectSpread({},e.metaPagination):e.table.pagination=!1,e.fieldHrefSlots=t.result.fieldHrefSlots,e.dictOptions=t.result.dictOptions,e.formTemplate=t.result.formTemplate,e.description=t.result.description,e.currentTableName=t.result.currentTableName,e.initCgButtonList(t.result.cgButtonList),e.initCgEnhanceJs(t.result.enhanceJs),e.initButtonSwitch(t.result.hideColumns);for(var n=t.result.columns,a=0;a<n.length;a++)n[a].customRender&&function(){var t=n[a].customRender,o="_replace_text_";if(t.startsWith(o)){var i=t.replace(o,"");n[a].customRender=function(e,t){return t[i]}}else n[a].customRender=function(n){return Object(_components_dict_JDictSelectUtil__WEBPACK_IMPORTED_MODULE_13__["b"])(e.dictOptions[t],n)}}();1==t.result.scrollFlag?e.table.scroll={x:"115%"}:e.table.scroll={x:!1},n.push(e.actionColumn),e.table.columns=Object(_Users_zhouwenrong_IdeaProjects_jeecg_boot_ant_design_vue_node_modules_babel_runtime_corejs2_helpers_esm_toConsumableArray__WEBPACK_IMPORTED_MODULE_3__["a"])(n),e.hasBpmStatusFilter(),e.loadData(),e.initQueryInfo(),e.table.selectedRowKeys=[]}else e.$message.warning(t.message)}))},loadData:function(e){var t=this;if(this.table.pagination){1==e&&(this.table.pagination.current=1),this.table.loading=!0;var n=this.getQueryParams();Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["c"])("".concat(this.url.getData).concat(this.code),n).then((function(e){if(e.success){var n=e.result;Number(n.total)>0?(t.table.pagination.total=Number(n.total),t.table.dataSource=n.records):(t.table.pagination.total=0,t.table.dataSource=[])}else t.$message.warning(e.message)})).finally((function(){t.table.loading=!1}))}else this.loadDataNoPage()},loadDataNoPage:function(){var e=this;this.table.loading=!0;var t=this.getQueryParams();t["pageSize"]=-521,Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["c"])("".concat(this.url.getData).concat(this.code),Object(_utils_util__WEBPACK_IMPORTED_MODULE_14__["d"])(t)).then((function(t){if(t.success){var n=t.result;Number(n.total)>0?e.table.dataSource=n.records:e.table.dataSource=[]}else e.$message.warning(t.message)})).finally((function(){e.table.loading=!1}))},getQueryParams:function(){var e=Object.assign({},this.queryParam,this.isorter);return e.pageNo=this.table.pagination.current,e.pageSize=this.table.pagination.pageSize,e.superQueryMatchType=this.superQuery.matchType,e.superQueryParams=encodeURIComponent(this.superQuery.params),Object(_utils_util__WEBPACK_IMPORTED_MODULE_14__["d"])(e)},handleChangeInTableSelect:function(e,t){this.table.selectedRowKeys=e,this.table.selectionRows=t,this.selectedRowKeys=e},handleTableChange:function(e,t,n){Object.keys(n).length>0&&(this.isorter.column=n.field,this.isorter.order="ascend"==n.order?"asc":"desc"),this.table.pagination=e,this.loadData()},handleAdd:function(){this.cgButtonJsHandler("beforeAdd"),this.$refs.modal.add(this.formTemplate)},handleImportXls:function(){this.$refs.importModal.show()},importOk:function(){this.loadData(1)},handleExportXls2:function(){var e=this.queryParam;this.table.selectedRowKeys&&this.table.selectedRowKeys.length>0&&(e["selections"]=this.table.selectedRowKeys.join(","));var t=encodeURI(JSON.stringify(e)),n=window._CONFIG["domianURL"]+this.url.exportXls+this.code+"?paramsStr="+t;window.location.href=n},handleExportXls:function(){var e=this,t=this.queryParam;this.table.selectedRowKeys&&this.table.selectedRowKeys.length>0&&(t["selections"]=this.table.selectedRowKeys.join(","));var n=JSON.stringify(Object(_utils_util__WEBPACK_IMPORTED_MODULE_14__["d"])(t));Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["b"])(this.url.exportXls+this.code,{paramsStr:n}).then((function(t){if(t)if("undefined"!==typeof window.navigator.msSaveBlob)window.navigator.msSaveBlob(new Blob([t]),e.description+".xls");else{var n=window.URL.createObjectURL(new Blob([t])),a=document.createElement("a");a.style.display="none",a.href=n,a.setAttribute("download",e.description+".xls"),document.body.appendChild(a),a.click(),document.body.removeChild(a),window.URL.revokeObjectURL(n)}else e.$message.warning("文件下载失败")}))},handleEdit:function(e){this.cgButtonLinkHandler(e,"beforeEdit","js"),this.$refs.modal.edit(this.formTemplate,e.id)},showLinkButton:function(e,t){var n=new ButtonExpHandler(e.exp,t);return n.show},handleDetail:function(e){this.$refs.modal.detail(this.formTemplate,e.id)},handleDeleteOne:function(e){this.cgButtonLinkHandler(e,"beforeDelete","js"),this.handleDelete(e.id)},handleDelete:function(e){var t=this;Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["a"])(this.url.optPre+this.code+"/"+e).then((function(e){e.success?(t.$message.success(e.message),t.loadData()):t.$message.warning(e.message)}))},handleFormSuccess:function(){this.loadData()},handleGetSchema:function(e){if(e&&e.properties){var t=function(e,t){var n=t.type||"string";n="inputNumber"===n?"number":n,e.push({type:n,value:t.key,text:t.title,dictCode:t.dictCode,dictTable:t.dictTable,dictText:t.dictText,options:t.enum||t.options,order:t.order})},n=[];for(var a in e.properties)if(e.properties.hasOwnProperty(a)){var o=e.properties[a];if("tab"===o.view){var i={type:"sub-table",value:o.key,text:o.describe,children:[]},s=!0,r=!1,_=void 0;try{for(var c,l=o.columns[Symbol.iterator]();!(s=(c=l.next()).done);s=!0){var u=c.value;t(i.children,u)}}catch(f){r=!0,_=f}finally{try{s||null==l.return||l.return()}finally{if(r)throw _}}n.push(i)}else o.key=a,t(n,o)}for(var d=0;d<n.length;d++)for(var h=d+1;h<n.length;h++){var p=n[d],m=n[h];p.order>m.order&&(n[d]=m,n[h]=p)}this.superQuery.fieldList=n}},onClearSelected:function(){this.table.selectedRowKeys=[],this.table.selectionRows=[]},getImgView:function(e){return e&&e.indexOf(",")>0&&(e=e.substring(0,e.indexOf(","))),window._CONFIG["staticDomainURL"]+"/"+e},downloadRowFile:function(e){e?(e.indexOf(",")>0&&(e=e.substring(0,e.indexOf(","))),window.open(window._CONFIG["staticDomainURL"]+"/"+e)):this.$message.warning("未知的文件")},handleDelBatch:function(){if(this.table.selectedRowKeys.length<=0)return this.$message.warning("请选择一条记录！"),!1;var e="",t=this;t.table.selectedRowKeys.forEach((function(t){e+=t+","})),t.$confirm({title:"确认删除",content:"是否删除选中数据?",onOk:function(){t.handleDelete(e),t.onClearSelected()}})},searchByquery:function(){this.loadData(1)},searchReset:function(){this.queryParam={},this.loadData(1)},handleToggleSearch:function(){this.toggleSearchStatus=!this.toggleSearchStatus},getFormatDate:function(e){if(!e)return"";var t=e;return t.length>10&&(t=t.substring(0,10)),t},getImportUrl:function(){return"/online/cgform/api/importXls/"+this.code},initCgEnhanceJs:function initCgEnhanceJs(enhanceJs){if(enhanceJs){var Obj=eval("("+enhanceJs+")");this.EnhanceJS=new Obj(_api_manage__WEBPACK_IMPORTED_MODULE_12__["c"],_api_manage__WEBPACK_IMPORTED_MODULE_12__["i"],_api_manage__WEBPACK_IMPORTED_MODULE_12__["a"]),this.cgButtonJsHandler("created")}else this.EnhanceJS=""},initCgButtonList:function(e){var t=[],n=[];if(e&&e.length>0)for(var a=0;a<e.length;a++){var o=e[a];"button"==o.buttonStyle?n.push(o):"link"==o.buttonStyle&&t.push(o)}this.cgButtonLinkList=[].concat(t),this.cgButtonList=[].concat(n)},cgButtonJsHandler:function(e){this.EnhanceJS[e]&&this.EnhanceJS[e](this)},cgButtonActionHandler:function(e){var t=this;if(!this.table.selectedRowKeys||0==this.table.selectedRowKeys.length)return this.$message.warning("请先选中一条记录"),!1;if(this.table.selectedRowKeys.length>1)return this.$message.warning("请只选中一条记录"),!1;var n={formId:this.code,buttonCode:e,dataId:this.table.selectedRowKeys[0]};Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["i"])(this.url.buttonAction,n).then((function(e){e.success?(t.loadData(),t.$message.success("处理完成!")):t.$message.warning("处理失败!")}))},cgButtonLinkHandler:function(e,t,n){var a=this;if("js"==n)this.EnhanceJS[t]&&this.EnhanceJS[t](this,e);else if("action"==n){var o={formId:this.code,buttonCode:t,dataId:e.id};Object(_api_manage__WEBPACK_IMPORTED_MODULE_12__["i"])(this.url.buttonAction,o).then((function(e){e.success?(a.loadData(),a.$message.success("处理完成!")):a.$message.warning("处理失败!")}))}},initButtonSwitch:function(e){var t=this;Object.keys(this.buttonSwitch).forEach((function(e){t.buttonSwitch[e]=!0})),e&&e.length>0&&Object.keys(this.buttonSwitch).forEach((function(n){e.indexOf(n)>=0&&(t.buttonSwitch[n]=!1)}))},handleSuperQuery:function(e,t){e&&0!==e.length?this.superQuery.params=JSON.stringify(e):this.superQuery.params="",this.superQuery.matchType=t,this.loadData()}}}},ca2d:function(e,t,n){"use strict";var a=n("9c08"),o=n.n(a);o.a},ee8f:function(e,t,n){"use strict";var a=n("8d02"),o=n.n(a);o.a}}]);