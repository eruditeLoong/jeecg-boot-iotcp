<template>
    <a-modal
        :confirmLoading="confirmLoading"
        :title="title"
        :visible="visible"
        :width="width"
        @cancel="handleCancel"
        @ok="handleOk"
        cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属机构">
                    <j-search-select-tag
                        :trigger-change="true"
                        dict="iot_org,name,id"
                        v-decorator="['orgBy', validatorRules.orgBy]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上级实例">
                    <j-search-select-tag
                        :trigger-change="true"
                        @change="parentByChange"
                        dict="iot_device_instance,name,id"
                        ref="parentBy"
                        v-decorator="['parentBy', validatorRules.parentBy]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="设备模型">
                    <j-search-select-tag
                        :trigger-change="true"
                        dict="iot_device_model,name,id"
                        v-decorator="['modelBy', validatorRules.modelBy]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="实例标识">
                    <a-input placeholder="请输入实例标识" v-decorator="[ 'code', validatorRules.code]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="实例名称">
                    <a-input placeholder="请输入实例名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="说明">
                    <a-textarea placeholder="请输入说明" rows="4" v-decorator="['description', validatorRules.description]"/>
                </a-form-item>

            </a-form>
        </a-spin>
    </a-modal>
</template>

<script>

    import { httpAction } from '@/api/manage'
    import pick from 'lodash.pick'
    import { validateDuplicateValue } from '@/utils/util'
    import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

    export default {
        name: 'DeviceInstanceModal',
        components: {
            JSearchSelectTag
        },
        watch: {},
        data() {
            return {
                form: this.$form.createForm(this),
                title: '操作',
                width: 800,
                visible: false,
                showParentBy: false,
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
                    parentBy: {
                        rules: []
                    },
                    code: {
                        rules: [
                            { required: true, message: '请输入实例标识!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_device_instance', 'code', value, this.model.id, callback) }
                        ]
                    },
                    name: {
                        rules: [
                            { required: true, message: '请输入实例名称!' }
                        ]
                    },
                    modelBy: {
                        rules: [
                            { required: true, message: '请输入设备模型!' }
                        ]
                    },
                    orgBy: {
                        rules: [
                            { required: true, message: '请输入所属机构!' }
                        ]
                    },
                    description: {
                        rules: []
                    }
                },
                url: {
                    add: '/device/deviceInstance/add',
                    edit: '/device/deviceInstance/edit'
                }
            }
        },
        created() {
        },
        methods: {
            parentByChange(val) {
                console.log(this.model)
                if (val == this.model.id) {
                    this.$nextTick(() => {
                        this.model.parentBy = ''
                        this.form.setFieldsValue(pick(this.model, 'parentBy'))
                    })
                    this.$message.error('不能选择自己作为父级设备，请重新选择')
                }
                console.log(this.model)
            },
            add() {
                this.edit({})
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'parentBy', 'code', 'name', 'modelBy', 'orgBy', 'createTime', 'description'))
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
                this.form.setFieldsValue(pick(row, 'parentBy', 'code', 'name', 'modelBy', 'orgBy', 'createTime', 'description'))
            }


        }
    }
</script>