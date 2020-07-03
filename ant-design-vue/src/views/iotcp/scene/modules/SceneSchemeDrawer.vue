<template>
    <a-drawer
        :closable="true"
        :title="title"
        :visible="visible"
        :width="width"
        @close="close"
    >
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="场景">
                    <a-input disabled v-decorator="[ 'sceneBy']"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="方案名称">
                    <a-input placeholder="请输入实例名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="方案状态">
                    <a-switch
                        :checked="model.status"
                        checkedChildren="有效"
                        unCheckedChildren="无效"
                        v-decorator="[ 'status']"
                        @change="val=>model.status=val"
                    />
                    {{model.status==1}}
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="方案配置">
                    <j-code-editor
                        language="javascript"
                        v-decorator="['config', {initialValue: model.config}]"
                        :fullScreen="true"
                        style="min-height: 100px"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="说明">
                    <a-textarea placeholder="请输入说明" rows="4" v-decorator="['description', validatorRules.description]"/>
                </a-form-item>
            </a-form>
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

    import pick from 'lodash.pick'
    import { validateDuplicateValue } from '@/utils/util'
    import { getAction, httpAction } from '@api/manage'
    import JCodeEditor from '../../../../components/jeecg/JCodeEditor'

    export default {
        name: 'SceneSchemeDrawer',
        components: { JCodeEditor },
        data() {
            return {
                form: this.$form.createForm(this),
                title: '操作',
                width: 500,
                visible: false,
                showParentBy: false,
                extendParamsConf: [],
                model: {},
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 5 }
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                confirmLoading: false,
                validatorRules: {
                    name: {
                        rules: [
                            { required: true, message: '请输入实例名称!' }
                        ]
                    },
                    config: {
                        rules: [
                            { required: true, message: '请输入设备模型!' }
                        ]
                    },
                    status: {
                        rules: [
                            { required: true, message: '请输入所属机构!' }
                        ]
                    },
                    description: {
                        rules: []
                    }
                },
                url: {
                    add: '/scene/sceneScheme/add',
                    edit: '/scene/sceneScheme/edit',
                }
            }
        },
        methods: {

            add(sceneId) {
                let record = {
                    sceneBy: sceneId,
                    status: false
                }
                this.edit(record)
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'sceneBy', 'name', 'config', 'status', 'description'))
                })
            },
            close() {
                this.model = {}
                this.form.resetFields()
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
            }
        }
    }
</script>
