<template>
    <a-drawer
        :closable="true"
        :title="title"
        :visible="visible"
        :width="width"
        @close="close"
    >
        <a-spin :spinning="confirmLoading">
            <a-tree
                :auto-expand-parent="autoExpandParent"
                :expanded-keys="expandedKeys"
                :selected-keys="selectedKeys"
                :tree-data="treeData"
                @expand="onExpand"
                @select="onSelect"
                checkable
                v-model="checkedKeys"
            />
        </a-spin>
        <div :style="{
          position: 'absolute',
          right: 0,
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'right',
          zIndex: 1,
        }"
        >
            <a-button :style="{ marginRight: '10px' }" @click="handleOk" type="primary">确定</a-button>
            <a-button @click="close" type="">取消</a-button>
        </div>
    </a-drawer>
</template>

<script>
    const treeData = [
        {
            title: '0-0',
            key: '0-0',
            children: [
                {
                    title: '0-0-0',
                    key: '0-0-0',
                    children: [
                        { title: '0-0-0-0', key: '0-0-0-0' },
                        { title: '0-0-0-1', key: '0-0-0-1' },
                        { title: '0-0-0-2', key: '0-0-0-2' }
                    ]
                },
                {
                    title: '0-0-1',
                    key: '0-0-1',
                    children: [
                        { title: '0-0-1-0', key: '0-0-1-0' },
                        { title: '0-0-1-1', key: '0-0-1-1' },
                        { title: '0-0-1-2', key: '0-0-1-2' }
                    ]
                },
                {
                    title: '0-0-2',
                    key: '0-0-2'
                }
            ]
        },
        {
            title: '0-1',
            key: '0-1',
            children: [
                { title: '0-1-0-0', key: '0-1-0-0' },
                { title: '0-1-0-1', key: '0-1-0-1' },
                { title: '0-1-0-2', key: '0-1-0-2' }
            ]
        },
        {
            title: '0-2',
            key: '0-2'
        }
    ]
    import { getAction, httpAction } from '@api/manage'
    import pick from 'lodash.pick'
    export default {
        name: 'SchemeDeviceDrawer',

        data() {
            return {
                title: '操作',
                width: 500,
                visible: false,
                extendParamsConf: [],
                model: {},
                confirmLoading: false,

                expandedKeys: ['0-0-0', '0-0-1'],
                autoExpandParent: true,
                checkedKeys: ['0-0-0'],
                selectedKeys: [],
                treeData,

                url: {
                    add: '/scene/sceneScheme/add',
                    edit: '/scene/sceneScheme/edit',
                    list: '/scene/sceneScheme/listInsDeviceByOrg'
                }
            }
        },
        watch: {
            checkedKeys(val) {
                console.log('onCheck', val)
            }
        },
        methods: {
            deviceTree(scheme) {
                this.visible = true
                this.loadInsDevice()
            },
            loadInsDevice() {
                const self = this;
                this.loading = true
                this.treeData = []
                getAction(this.url.list).then((res) => {
                    if (res.success) {
                        self.treeData = res.result
                    }
                }).finally(() => {
                    self.loading = false
                })
            },
            close() {
                this.model = {}
                this.visible = false
            },
            handleOk() {
                const that = this
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        that.confirmLoading = true
                        let httpurl = ''
                        let method = ''
                        if (!this.model.id) {
                            httpurl += this.url.add
                            method = 'post'
                        } else {
                            httpurl += this.url.edit
                            method = 'put'
                        }
                        let formData = Object.assign(this.model, values)
                        console.log('表单提交数据', formData)
                        httpAction(httpurl, formData, method).then((res) => {
                            if (res.success) {
                                console.log('====>', res.message)
                                that.$message.success(res.message)
                                that.$emit('ok')
                            } else {
                                that.$message.warning(res.message)
                            }
                        }).finally(() => {
                            that.confirmLoading = false
                            that.close()
                        })
                    }

                })
            },
            onExpand(expandedKeys) {
                console.log('onExpand', expandedKeys)
                // if not set autoExpandParent to false, if children expanded, parent can not collapse.
                // or, you can remove all expanded children keys.
                this.expandedKeys = expandedKeys
                this.autoExpandParent = false
            },
            onCheck(checkedKeys) {
                console.log('onCheck', checkedKeys)
                this.checkedKeys = checkedKeys
            },
            onSelect(selectedKeys, info) {
                console.log('onSelect', info)
                this.selectedKeys = selectedKeys
            }
        }
    }
</script>

<style scoped>

</style>
