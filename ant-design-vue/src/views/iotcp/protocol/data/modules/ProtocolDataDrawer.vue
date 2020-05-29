<template>
    <a-drawer
        :closable="false"
        :title="title"
        :visible="visible"
        :width="width"
        @close="close"
        placement="right">

        <a-spin :spinning="confirmLoading">
            <a-form :form="form">

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="协议标识">
                    <a-input placeholder="请输入协议标识" v-decorator="[ 'code', validatorRules.code]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="协议名称">
                    <a-input placeholder="请输入协议名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="解析类型">
                    <j-dict-select-tag
                        :trigger-change="true"
                        dictCode="pd_type"
                        placeholder="请选择解析类型"
                        type="radio"
                        v-decorator="['type', validatorRules.type]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="解析包">
                    <j-upload :trigger-change="true" v-decorator="['packagePath', validatorRules.packagePath]"></j-upload>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="包名">
                    <a-input placeholder="请输入描述" v-decorator="[ 'classPath', validatorRules.classPath]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述">
                    <a-textarea placeholder="请输入描述" rows="4" v-decorator="[ 'description', validatorRules.description]"></a-textarea>
                </a-form-item>

            </a-form>
        </a-spin>
        <a-button @click="handleOk" type="primary">确定</a-button>
        <a-button @click="handleCancel" type="">取消</a-button>
    </a-drawer>
</template>

<script>

    import { httpAction } from '@/api/manage'
    import pick from 'lodash.pick'
    import { validateDuplicateValue } from '@/utils/util'
    import JUpload from '@/components/jeecg/JUpload'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'

    export default {
        name: 'ProtocolDataModal',
        components: {
            JUpload,
            JDictSelectTag
        },
        data() {
            return {
                form: this.$form.createForm(this),
                title: '操作',
                width: 500,
                visible: false,
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
                    code: {
                        rules: [
                            { required: true, message: '请输入协议标识!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_protocol_data', 'code', value, this.model.id, callback) }
                        ]
                    },
                    name: {
                        rules: [
                            { required: true, message: '请输入协议名称!' }
                        ]
                    },
                    type: {
                        rules: [
                            { required: true, message: '请输入解析类型!' }
                        ]
                    },
                    packagePath: {
                        rules: [
                            { required: true, message: '请输入解析包!' }
                        ]
                    },
                    classPath: {
                        rules: [
                            { required: true, message: '请输入包名!' }
                        ]
                    },
                    description: {
                        rules: []
                    }
                },
                url: {
                    add: '/protocol/protocolData/add',
                    edit: '/protocol/protocolData/edit'
                }
            }
        },
        created() {
        },
        methods: {
            add() {
                this.edit({})
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'code', 'name', 'type', 'packagePath', 'classPath', 'createTime', 'state', 'description'))
                })
            },
            close() {
                this.$emit('close')
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
            popupCallback(row) {
                this.form.setFieldsValue(pick(row, 'code', 'name', 'type', 'path', 'createTime', 'state', 'description'))
            }

        }
    }
</script>

<style lang="less" scoped>
    /** Button按钮间距 */
    .ant-btn {
        margin-left: 30px;
        margin-bottom: 30px;
        float: right;
    }
</style>