(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-c2f3f39e","chunk-2d21dfe7","chunk-2d0b1e33"],{"200d":function(e,t,a){"use strict";var s=a("57d5"),i=a.n(s);i.a},2285:function(e,t,a){"use strict";a.r(t);var s=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("a-drawer",{attrs:{title:"数据规则/按钮权限配置",width:"365",closable:!1,visible:e.visible},on:{close:e.onClose}},[a("a-tabs",{attrs:{defaultActiveKey:"1"}},[a("a-tab-pane",{key:"1",attrs:{tab:"数据规则"}},[e.dataruleList.length>0?a("a-checkbox-group",{model:{value:e.dataruleChecked,callback:function(t){e.dataruleChecked=t},expression:"dataruleChecked"}},[a("a-row",[e._l(e.dataruleList,(function(t,s){return a("a-col",{key:"dr"+s,attrs:{span:24}},[a("a-checkbox",{attrs:{value:t.id}},[e._v(e._s(t.ruleName))])],1)})),a("a-col",{attrs:{span:24}},[a("div",{staticStyle:{width:"100%","margin-top":"15px"}},[a("a-button",{attrs:{type:"primary",size:"small",icon:"save"},on:{click:e.saveDataruleForRole}},[e._v("点击保存")])],1)])],2)],1):a("div",[a("h3",[e._v("无配置信息!")])])],1)],1)],1)},i=[],n=(a("28a5"),a("290c")),l=a("da05"),c=a("0fea"),r={name:"RoleDataruleModal",components:{ACol:l["b"],ARow:n["a"]},data:function(){return{functionId:"",roleId:"",visible:!1,tabList:[{key:"1",tab:"数据规则"},{key:"2",tab:"按钮权限"}],activeTabKey:"1",url:{datarule:"/sys/role/datarule"},dataruleList:[],dataruleChecked:[]}},methods:{loadData:function(){var e=this;Object(c["c"])("".concat(this.url.datarule,"/").concat(this.functionId,"/").concat(this.roleId)).then((function(t){if(t.success){e.dataruleList=t.result.datarule;var a=t.result.drChecked;a&&(e.dataruleChecked=a.split(","))}}))},saveDataruleForRole:function(){var e=this;this.dataruleChecked&&0!=this.dataruleChecked.length||this.$message.warning("请注意，现未勾选任何数据权限!");var t={permissionId:this.functionId,roleId:this.roleId,dataRuleIds:this.dataruleChecked.join(",")};Object(c["i"])(this.url.datarule,t).then((function(t){t.success?e.$message.success(t.message):e.$message.error(t.message)}))},show:function(e,t){this.onReset(),this.functionId=e,this.roleId=t,this.visible=!0,this.loadData()},onClose:function(){this.visible=!1,this.onReset()},onTabChange:function(e){this.activeTabKey=e},onReset:function(){this.functionId="",this.roleId="",this.dataruleList=[],this.dataruleChecked=[]}}},o=r,d=a("2877"),u=Object(d["a"])(o,s,i,!1,null,"5fa38486",null);t["default"]=u.exports},"57d5":function(e,t,a){},d456:function(e,t,a){"use strict";a.r(t);var s=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("a-drawer",{attrs:{title:"数据规则/按钮权限配置",width:"365",closable:!1,visible:e.visible},on:{close:e.onClose}},[a("a-tabs",{attrs:{defaultActiveKey:"1"}},[a("a-tab-pane",{key:"1",attrs:{tab:"数据规则"}},[e.dataruleList.length>0?a("a-checkbox-group",{model:{value:e.dataruleChecked,callback:function(t){e.dataruleChecked=t},expression:"dataruleChecked"}},[a("a-row",[e._l(e.dataruleList,(function(t,s){return a("a-col",{key:"dr"+s,attrs:{span:24}},[a("a-checkbox",{attrs:{value:t.id}},[e._v(e._s(t.ruleName))])],1)})),a("a-col",{attrs:{span:24}},[a("div",{staticStyle:{width:"100%","margin-top":"15px"}},[a("a-button",{attrs:{type:"primary",size:"small",icon:"save"},on:{click:e.saveDataruleForRole}},[e._v("点击保存")])],1)])],2)],1):a("div",[a("h3",[e._v("无配置信息!")])])],1)],1)],1)},i=[],n=(a("28a5"),a("290c")),l=a("da05"),c=a("0fea"),r={name:"DeptRoleDataruleModal",components:{ACol:l["b"],ARow:n["a"]},data:function(){return{departId:"",functionId:"",roleId:"",visible:!1,tabList:[{key:"1",tab:"数据规则"},{key:"2",tab:"按钮权限"}],activeTabKey:"1",url:{datarule:"/sys/sysDepartRole/datarule"},dataruleList:[],dataruleChecked:[]}},methods:{loadData:function(){var e=this;Object(c["c"])("".concat(this.url.datarule,"/").concat(this.functionId,"/").concat(this.departId,"/").concat(this.roleId)).then((function(t){if(t.success){e.dataruleList=t.result.datarule;var a=t.result.drChecked;a&&(e.dataruleChecked=a.split(","))}}))},saveDataruleForRole:function(){var e=this;this.dataruleChecked&&0!=this.dataruleChecked.length||this.$message.warning("请注意，现未勾选任何数据权限!");var t={permissionId:this.functionId,roleId:this.roleId,dataRuleIds:this.dataruleChecked.join(",")};Object(c["i"])(this.url.datarule,t).then((function(t){t.success?e.$message.success(t.message):e.$message.error(t.message)}))},show:function(e,t,a){this.onReset(),this.departId=t,this.functionId=e,this.roleId=a,this.visible=!0,this.loadData()},onClose:function(){this.visible=!1,this.onReset()},onTabChange:function(e){this.activeTabKey=e},onReset:function(){this.functionId="",this.roleId="",this.dataruleList=[],this.dataruleChecked=[]}}},o=r,d=a("2877"),u=Object(d["a"])(o,s,i,!1,null,"776b339a",null);t["default"]=u.exports},dc4b:function(e,t,a){"use strict";a.r(t);var s=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("a-drawer",{staticStyle:{height:"calc(100% - 55px)",overflow:"auto","padding-bottom":"53px"},attrs:{title:e.title,maskClosable:!0,width:"650",placement:"right",closable:!0,visible:e.visible},on:{close:e.close}},[a("a-form",[a("a-form-item",{attrs:{label:"所拥有的部门权限"}},[e.treeData.length>0?a("a-tree",{attrs:{checkable:"",checkedKeys:e.checkedKeys,treeData:e.treeData,selectedKeys:e.selectedKeys,expandedKeys:e.expandedKeysss,checkStrictly:e.checkStrictly},on:{check:e.onCheck,expand:e.onExpand,select:e.onTreeNodeSelect},scopedSlots:e._u([{key:"hasDatarule",fn:function(t){var s=t.slotTitle,i=t.ruleFlag;return a("span",{},[e._v("\n          "+e._s(s)),i?a("a-icon",{staticStyle:{"margin-left":"5px",color:"red"},attrs:{type:"align-left"}}):e._e()],1)}}],null,!1,2869785871)}):a("div",[a("h3",[e._v("无可配置部门权限!")])])],1)],1),a("div",{staticClass:"drawer-bootom-button"},[a("a-dropdown",{staticStyle:{float:"left"},attrs:{trigger:["click"],placement:"topCenter"}},[a("a-menu",{attrs:{slot:"overlay"},slot:"overlay"},[a("a-menu-item",{key:"3",on:{click:e.checkALL}},[e._v("全部勾选")]),a("a-menu-item",{key:"4",on:{click:e.cancelCheckALL}},[e._v("取消全选")]),a("a-menu-item",{key:"5",on:{click:e.expandAll}},[e._v("展开所有")]),a("a-menu-item",{key:"6",on:{click:e.closeAll}},[e._v("合并所有")])],1),a("a-button",[e._v("\n        树操作 "),a("a-icon",{attrs:{type:"up"}})],1)],1),a("a-popconfirm",{attrs:{title:"确定放弃编辑？",okText:"确定",cancelText:"取消"},on:{confirm:e.close}},[a("a-button",{staticStyle:{"margin-right":".8rem"}},[e._v("取消")])],1),a("a-button",{staticStyle:{"margin-right":"0.8rem"},attrs:{type:"primary",loading:e.loading,ghost:""},on:{click:function(t){return e.handleSubmit(!1)}}},[e._v("仅保存")]),a("a-button",{attrs:{type:"primary",loading:e.loading},on:{click:function(t){return e.handleSubmit(!0)}}},[e._v("保存并关闭")])],1),a("dept-role-datarule-modal",{ref:"datarule"})],1)},i=[],n=(a("ac4d"),a("8a81"),a("ac6a"),a("75fc")),l=a("4ec3"),c=a("2285"),r=a("d456"),o={name:"DeptRoleAuthModal",components:{DeptRoleDataruleModal:r["default"],RoleDataruleModal:c["default"]},data:function(){return{departId:"",roleId:"",treeData:[],defaultCheckedKeys:[],checkedKeys:[],halfCheckedKeys:[],expandedKeysss:[],allTreeKeys:[],autoExpandParent:!0,checkStrictly:!1,title:"部门角色权限配置",visible:!1,loading:!1,selectedKeys:[]}},methods:{onTreeNodeSelect:function(e){e&&e.length>0&&(this.selectedKeys=e),this.$refs.datarule.show(this.selectedKeys[0],this.departId,this.roleId)},onCheck:function(e,t){var a=t.halfCheckedKeys;this.checkedKeys=e,this.halfCheckedKeys=a},show:function(e,t){this.departId=t,this.roleId=e,this.visible=!0},close:function(){this.reset(),this.$emit("close"),this.visible=!1},onExpand:function(e){this.expandedKeysss=e,this.autoExpandParent=!1},reset:function(){this.expandedKeysss=[],this.checkedKeys=[],this.defaultCheckedKeys=[],this.loading=!1},expandAll:function(){this.expandedKeysss=this.allTreeKeys},closeAll:function(){this.expandedKeysss=[]},checkALL:function(){this.checkedKeys=this.allTreeKeys},cancelCheckALL:function(){this.checkedKeys=[]},handleCancel:function(){this.close()},handleSubmit:function(e){var t=this,a=this,s=[].concat(Object(n["a"])(a.checkedKeys),Object(n["a"])(a.halfCheckedKeys)),i=s.join(","),c={roleId:a.roleId,permissionIds:i,lastpermissionIds:a.defaultCheckedKeys.join(",")};a.loading=!0,Object(l["Q"])(c).then((function(s){s.success?(a.$message.success(s.message),a.loading=!1,e&&a.close()):(a.$message.error(s.message),a.loading=!1,e&&a.close()),t.loadData()}))},convertTreeListToKeyLeafPairs:function(e){var t=arguments.length>1&&void 0!==arguments[1]?arguments[1]:[],a=!0,s=!1,i=void 0;try{for(var n,l=e[Symbol.iterator]();!(a=(n=l.next()).done);a=!0){var c=n.value,r=c.key,o=c.isLeaf,d=c.children;t.push({key:r,isLeaf:o}),d&&d.length>0&&this.convertTreeListToKeyLeafPairs(d,t)}}catch(u){s=!0,i=u}finally{try{a||null==l.return||l.return()}finally{if(s)throw i}}return t},loadData:function(){var e=this;Object(l["K"])({departId:this.departId}).then((function(t){e.treeData=t.result.treeList,e.allTreeKeys=t.result.ids;var a=e.convertTreeListToKeyLeafPairs(e.treeData);Object(l["D"])({roleId:e.roleId}).then((function(t){var s=Object(n["a"])(t.result).filter((function(e){var t=a.filter((function(t){return t.key===e}))[0];return t&&t.isLeaf})),i=Object(n["a"])(t.result).filter((function(e){var t=a.filter((function(t){return t.key===e}))[0];return t&&!t.isLeaf}));e.checkedKeys=Object(n["a"])(s),e.halfCheckedKeys=Object(n["a"])(i),e.defaultCheckedKeys=[].concat(Object(n["a"])(i),Object(n["a"])(s)),e.expandedKeysss=e.allTreeKeys}))}))}},watch:{visible:function(){this.visible&&this.loadData()}}},d=o,u=(a("200d"),a("2877")),h=Object(u["a"])(d,s,i,!1,null,"ffb1d9c6",null);t["default"]=h.exports}}]);