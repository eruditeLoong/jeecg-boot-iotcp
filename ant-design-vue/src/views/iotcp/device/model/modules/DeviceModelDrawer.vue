<template>
    <a-drawer
        :closable="false"
        :title="title"
        :visible="visible"
        :width="width"
        @close="close"
        placement="right"
    >
        <a-spin :spinning="confirmLoading">
            <!-- 主表单区域 -->
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="编码">
                    <a-input placeholder="请输入编码" v-decorator="[ 'code', validatorRules.code]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称">
                    <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="类型">
                    <j-dict-select-tag
                        :trigger-change="true"
                        dictCode="dm_type"
                        placeholder="请选择类型"
                        type="radio"
                        v-decorator="['type', validatorRules.type]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="模型文件">
                    <j-upload
                        :trigger-change="true"
                        v-decorator="['threeModelFile', validatorRules.threeModelFile]"
                    ></j-upload>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="传输协议">
                    <j-dict-select-tag
                        :trigger-change="true"
                        dictCode="iot_protocol_link,name,id"
                        placeholder="请选择传输协议"
                        type="list"
                        v-decorator="['linkProtocolBy', validatorRules.linkProtocolBy]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据协议">
                    <j-dict-select-tag
                        :trigger-change="true"
                        dictCode="iot_protocol_data,name,id"
                        placeholder="请选择数据协议"
                        style="width: 80%"
                        type="list"
                        v-decorator="['dataProtocolBy', validatorRules.dataProtocolBy]"/>
<!--                    <a-button slot="addonAfter" @click="addDataProtocol">+</a-button>-->
                    <a-button @click="addDataProtocol">+</a-button>
                </a-form-item>

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述">
                    <a-textarea placeholder="请输入描述" rows="4" v-decorator="['description', validatorRules.description]"/>
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
    import { httpAction } from '@/api/manage'
    import { validateDuplicateValue } from '@/utils/util'
    import JUpload from '@/components/jeecg/JUpload'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'

    export default {
        name: 'DeviceModelDrawer',
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
                confirmLoading: false,
                labelCol: {
                    span: 6
                },
                wrapperCol: {
                    span: 16
                },
                validatorRules: {
                    code: {
                        rules: [
                            { required: true, message: '请输入编码!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_device_model', 'code', value, this.model.id, callback) }
                        ]
                    },
                    name: {
                        rules: [
                            { required: true, message: '请输入名称!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_device_model', 'name', value, this.model.id, callback) }
                        ]
                    },
                    type: {
                        rules: [
                            { required: true, message: '请输入类型!' }
                        ]
                    },
                    threeModelFile: {
                        rules: []
                    },
                    linkProtocolBy: {
                        rules: []
                    },
                    dataProtocolBy: {
                        rules: []
                    },
                    description: {
                        rules: []
                    }
                },
                url: {
                    add: '/device/deviceModel/add',
                    edit: '/device/deviceModel/edit'
                }
            }
        },
        methods: {
            close() {
                this.$emit('close')
                this.visible = false
            },
            addDataProtocol() {
                this.$router.push({ path: '/protocol/protocolData' })
            },
            add() {
                this.edit({})
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                let fieldval = pick(this.model, 'code', 'name', 'type', 'threeModelFile', 'linkProtocolBy', 'dataProtocolBy', 'createTime', 'description')
                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldval)
                })
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
                this.form.setFieldsValue(pick(row, 'code', 'name', 'type', 'threeModelFile', 'linkProtocolBy', 'dataProtocolBy', 'createTime', 'description'))
            }
        }
    }
</script>

<style scoped>
</style>