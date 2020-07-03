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

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="实例设备">
                    <a-input placeholder="请输入实例设备" v-decorator="['instanceDeviceBy', validatorRules.instanceDeviceBy]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上报数据">
                    <a-input placeholder="请输入上报数据" v-decorator="['data', validatorRules.data]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="录入方式">
                    <j-dict-select-tag :trigger-change="true" dictCode="dm_type" placeholder="请选择录入方式" type="radio" v-decorator="['inputMode', validatorRules.inputMode]"/>
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
    import JDictSelectTag from '@/components/dict/JDictSelectTag'

    export default {
        name: 'DataReportModal',
        components: {
            JDictSelectTag
        },
        data() {
            return {
                form: this.$form.createForm(this),
                title: '操作',
                width: 800,
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
                    instanceDeviceBy: {
                        rules: [
                            { required: true, message: '请输入实例设备!' }
                        ]
                    },
                    data: {
                        rules: [
                            { required: true, message: '请输入上报数据!' }
                        ]
                    },
                    inputMode: {
                        rules: [
                            { required: true, message: '请输入录入方式!' }
                        ]
                    }
                },
                url: {
                    add: '/message/data/add',
                    edit: '/message/data/edit'
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
                    this.form.setFieldsValue(pick(this.model, 'instanceDeviceBy', 'data', 'createTime', 'inputMode'))
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
            handleCancel() {
                this.close()
            },
            popupCallback(row) {
                this.form.setFieldsValue(pick(row, 'instanceDeviceBy', 'data', 'createTime', 'inputMode'))
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