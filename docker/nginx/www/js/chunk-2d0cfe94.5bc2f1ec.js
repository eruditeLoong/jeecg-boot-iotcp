(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0cfe94"],{6675:function(t,e,a){"use strict";a.r(e);var s=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("a-drawer",{attrs:{title:"数据规则/按钮权限配置",width:"365",closable:!1,visible:t.visible},on:{close:t.onClose}},[a("a-tabs",{attrs:{defaultActiveKey:"1"}},[a("a-tab-pane",{key:"1",attrs:{tab:"数据规则"}},[t.dataruleList.length>0?a("a-checkbox-group",{model:{value:t.dataruleChecked,callback:function(e){t.dataruleChecked=e},expression:"dataruleChecked"}},[a("a-row",[t._l(t.dataruleList,(function(e,s){return a("a-col",{key:"dr"+s,attrs:{span:24}},[a("a-checkbox",{attrs:{value:e.id}},[t._v(t._s(e.ruleName))])],1)})),a("a-col",{attrs:{span:24}},[a("div",{staticStyle:{width:"100%","margin-top":"15px"}},[a("a-button",{attrs:{type:"primary",size:"small",icon:"save"},on:{click:t.saveDataruleForRole}},[t._v("点击保存")])],1)])],2)],1):a("div",[a("h3",[t._v("无配置信息!")])])],1)],1)],1)},i=[],n=(a("28a5"),a("290c")),r=a("da05"),c=a("0fea"),d={name:"DepartDataruleModal",components:{ACol:r["b"],ARow:n["a"]},data:function(){return{functionId:"",departId:"",visible:!1,tabList:[{key:"1",tab:"数据规则"},{key:"2",tab:"按钮权限"}],activeTabKey:"1",url:{datarule:"/sys/sysDepartPermission/datarule"},dataruleList:[],dataruleChecked:[]}},methods:{loadData:function(){var t=this;Object(c["c"])("".concat(this.url.datarule,"/").concat(this.functionId,"/").concat(this.departId)).then((function(e){if(e.success){t.dataruleList=e.result.datarule;var a=e.result.drChecked;a&&(t.dataruleChecked=a.split(","))}}))},saveDataruleForRole:function(){var t=this;this.dataruleChecked&&0!=this.dataruleChecked.length||this.$message.warning("请注意，现未勾选任何数据权限!");var e={permissionId:this.functionId,departId:this.departId,dataRuleIds:this.dataruleChecked.join(",")};Object(c["i"])(this.url.datarule,e).then((function(e){e.success?t.$message.success(e.message):t.$message.error(e.message)}))},show:function(t,e){this.onReset(),this.functionId=t,this.departId=e,this.visible=!0,this.loadData()},onClose:function(){this.visible=!1,this.onReset()},onTabChange:function(t){this.activeTabKey=t},onReset:function(){this.functionId="",this.departId="",this.dataruleList=[],this.dataruleChecked=[]}}},l=d,o=a("2877"),u=Object(o["a"])(l,s,i,!1,null,"73771a6a",null);e["default"]=u.exports}}]);