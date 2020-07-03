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

                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标识">
                    <a-input placeholder="请输入标识" v-decorator="[ 'code', validatorRules.code]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称">
                    <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="服务类型">
                    <j-dict-select-tag :trigger-change="true" @change="(val)=>typeChange(val)"
                                       dictCode="ns_type" placeholder="请选择服务类型" type="list" v-decorator="['type', validatorRules.type]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="图标">
                    <j-image-upload :trigger-change="true" v-decorator="['icon', validatorRules.topology]"></j-image-upload>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="HOST" v-show="show.host">
                    <a-input placeholder="请输入HOST" v-decorator="[ 'host', validatorRules.host]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="端口号" v-show="show.port">
                    <a-input-number placeholder="请输入端口号" style="width: 100%" v-decorator="[ 'port', validatorRules.port]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="线程数" v-show="show.threadNum">
                    <a-input-number placeholder="请输入线程数" style="width: 100%" v-decorator="[ 'threadNum', validatorRules.threadNum]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="同步处理" v-show="show.isSync">
                    <j-dict-select-tag :trigger-change="true" dictCode="activiti_sync" placeholder="是否同步处理" type="radio" v-decorator="['isSync', validatorRules.isSync]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="解析方式" v-show="show.resolMethod">
                    <a-input placeholder="请输入解析方式" v-decorator="[ 'resolMethod', validatorRules.resolMethod]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="用户名" v-show="show.username">
                    <a-input placeholder="请输入用户名" v-decorator="[ 'username', validatorRules.username]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="密码" v-show="show.password">
                    <a-input placeholder="请输入密码" v-decorator="[ 'password', validatorRules.password]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="订阅主题" v-show="show.topics">
                    <a-input placeholder="请输入订阅主题" v-decorator="[ 'topics', validatorRules.topics]"></a-input>
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
            <a-button @click="handleOk" type="primary" :style="{ marginRight: '10px' }">确定</a-button>
            <a-button @click="handleCancel" type="">取消</a-button>
        </div>
    </a-drawer>
</template>

<script>

    import { httpAction } from '@/api/manage'
    import pick from 'lodash.pick'
    import { validateDuplicateValue } from '@/utils/util'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'
    import JImageUpload from '../../../../components/jeecg/JImageUpload'

    export default {
        name: 'NetworkServiceModal',
        components: {
            JImageUpload,
            JDictSelectTag
        },
        data() {
            return {
                form: this.$form.createForm(this),
                title: '操作',
                width: 500,
                visible: false,
                model: {
                    host: window._CONFIG['domianURL'],
                    port: '8888',
                    icon: [],
                    isSync: 0,
                    threadNum: 1
                },
                show: {
                    host: false,
                    port: false,
                    threadNum: false,
                    isSync: false,
                    resolMethod: false,
                    topics: false,
                    password: false,
                    username: false,
                    clientId: false
                },

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
                            { required: true, message: '请输入标识!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_network_service', 'code', value, this.model.id, callback) }
                        ]
                    },
                    name: {
                        rules: [
                            { required: true, message: '请输入名称!' }
                        ]
                    },
                    type: {
                        rules: [
                            { required: true, message: '请输入服务类型!' }
                        ]
                    },
                    icon: {
                        rules: []
                    },
                    host: {
                        rules: []
                    },
                    port: {
                        rules: [
                            { required: true, message: '请输入端口号!' },
                            { pattern: /^-?\d+$/, message: '请输入整数!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_network_service', 'port', value, this.model.id, callback) }
                        ]
                    },
                    threadNum: {
                        rules: [
                            { required: false, message: '请输入线程数!' },
                            { pattern: /^-?\d+$/, message: '请输入整数!' }
                        ]
                    },
                    isSync: {
                        rules: [
                            { required: false, message: '请输入是否同步处理!' }
                        ]
                    },
                    resolMethod: {
                        rules: []
                    },
                    username: {
                        rules: []
                    },
                    password: {
                        rules: []
                    },
                    topics: {
                        rules: []
                    },
                    description: {
                        rules: []
                    }
                },
                url: {
                    add: '/network/networkService/add',
                    edit: '/network/networkService/edit'
                }
            }
        },
        created() {
            // alert("drawer created...");
        },
        computed: {
            serviceType() {
                return this.model.type
            }
        },
        watch: {
            serviceType(val, oval) {
                this.typeChange(val)
            }
        },
        methods: {
            typeChange(val) {
                let that = this
                switch (val) {
                    case 'tcp-server':
                        that.show.host = true
                        that.show.port = true
                        that.show.resolMethod = false
                        this.show.threadNum = false
                        this.show.isSync = false
                        this.show.topics = false
                        this.show.password = false
                        this.show.username = false
                        break
                    case 'mqtt-server':
                        that.show.host = true
                        that.show.port = true
                        this.show.threadNum = false
                        this.show.isSync = false
                        this.show.resolMethod = false
                        this.show.topics = false
                        this.show.password = false
                        this.show.username = false
                        break
                    case 'tcp-client':
                        that.show.host = true
                        that.show.port = true
                        this.show.threadNum = false
                        this.show.isSync = false
                        this.show.resolMethod = false
                        this.show.topics = false
                        this.show.password = false
                        this.show.username = false
                        break
                    case 'mqtt-client':
                        that.show.host = true
                        that.show.port = true
                        this.show.threadNum = false
                        this.show.isSync = false
                        this.show.resolMethod = false
                        this.show.topics = true
                        this.show.password = true
                        this.show.username = true
                        break
                    case 'default':
                        this.show.host = false
                        this.show.port = false
                        this.show.threadNum = false
                        this.show.isSync = false
                        this.show.resolMethod = false
                        this.show.topics = false
                        this.show.password = false
                        this.show.username = false
                        break
                }
            },
            add() {
                this.edit(this.model)
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'code', 'name', 'type', 'icon', 'host', 'port', 'threadNum', 'isSync', 'resolMethod', 'clientId', 'username', 'password', 'topics', 'createTime', 'description'))
                })
            },
            close() {
                this.$emit('close')
                this.edit({})
                this.show.host = false
                this.show.port = false
                this.show.threadNum = false
                this.show.isSync = false
                this.show.resolMethod = false
                this.show.topics = false
                this.show.password = false
                this.show.username = false
                this.show.clientId = false
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
                        /* 隐藏的内容置空 */
                        for (let key in formData) {
                            if (that.show[key] != undefined && !that.show[key]) {
                                console.log(key)
                                formData[key] = ''
                            }
                        }
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
                this.form.setFieldsValue(pick(row, 'code', 'name', 'type', 'icon', 'host', 'port', 'threadNum', 'isSync', 'resolMethod', 'username', 'password', 'topics', 'createTime', 'description'))
            }

        }
    }
</script>

<style lang="less" scoped>
</style>