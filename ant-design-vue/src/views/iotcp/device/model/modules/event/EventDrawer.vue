<template>
    <a-drawer
        :afterVisibleChange="visible=>getDataOptions(visible)"
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
                <a-input v-model="model.id" v-show="false"></a-input>
                <a-input v-model="model.deviceModelBy" v-show="false"></a-input>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标识">
                    <a-input placeholder="请输入标识" v-decorator="[ 'code', validatorRules.code]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称">
                    <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="事件来源">
                    <a-radio-group name="radioGroup" :defaultValue="'data'" v-model="model.type">
                        <a-radio value="data">数据提交</a-radio>
                        <a-radio value="event">事件上报</a-radio>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="事件等级">
                    <a-radio-group :defaultValue="1" size="small" v-model="model.level">
                        <a-radio-button :value="1" style="color:#b3d4fc;">普通</a-radio-button>
                        <a-radio-button :value="2" style="color:#f3af90;">警告</a-radio-button>
                        <a-radio-button :value="3" style="color:#ff1e00;">紧急</a-radio-button>
                    </a-radio-group>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="通知类型">
                    <j-multi-select-tag
                        :triggerChange="true"
                        dictCode="msgType"
                        placeholder="请选择通知模板"
                        v-decorator="['notifyType', {rules: [{ required:true, message: '请选择通知模板!' }]}]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据选择">
                    <a-select :allowClear="true"
                              :options="dataOptions"
                              mode="default"
                              placeholder="请选择数据节点"
                              style="width: 100%"
                              v-decorator="['dataBy', { rules:[{required: true, message: '请选择数据节点'}]}]"
                    >
                    </a-select>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="规则条件" v-show="model.type=='data'">
                    <j-search-select-tag
                        dict="rule_conditions"
                        :triggerChange="true"
                        placeholder="请选择规则条件"
                        type="list"
                        v-decorator="['dataRule', {rules:[{required: false, message: '请选择规则条件'}]}]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="规则值" v-show="model.type=='data'">
                    <a-input placeholder="请输入规则值"
                             v-decorator="[ 'ruleValue', {rules:[{required: this.model.type=='data', message: '请输入规则值'}]}]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述">
                    <a-textarea allowClear placeholder="请输入描述" rows="3" v-decorator="['description', validatorRules.description]"/>
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
    import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'
    import { getAction, httpAction } from '@/api/manage'
    import JMultiSelectTag from '@/components/dict/JMultiSelectTag'

    export default {
        name: 'FuncDrawer',
        components: {
            JSearchSelectTag,
            JDictSelectTag,
            JMultiSelectTag
        },
        props: ['deviceModelId'],
        data() {
            return {
                form: this.$form.createForm(this),
                title: '',
                width: 500,
                visible: false,
                model: {},
                dataOptions: [],
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
                            { required: true, message: '请输入标识!' },
                            { min: 2, max: 20, message: '标识长度：2-20个字符'},
                            {
                                validator: (rule, value, callback) => this.validateDuplicateCode(this.model.deviceModelBy, this.model.id, value, callback)
                            }
                        ]
                    },
                    name: {
                        rules: [
                            { required: true, message: '请输入名称!' },
                            { min: 2, max: 20, message: '名称长度：2-20个字符'},
                            { validator: (rule, value, callback) => this.validateDuplicateName(this.model.deviceModelBy, this.model.id, value, callback) }
                        ]
                    },
                    type: { rules: [{ required: true, message: '请选择功能类型!' }] },
                    description: { rules: [] }
                },
                url: {
                    add: '/device/deviceModel/addEvent',
                    edit: '/device/deviceModel/editEvent',
                    listInputParams: '/device/deviceModel/listInputParams',
                    validateCode: '/device/deviceModel/validateEventCode',
                    validateName: '/device/deviceModel/validateEventName',
                }
            }
        },
        computed: {
            type(){
                return this.model.type
            }
        },
        watch: {
            type(val, oval) {
                console.log('watch-type：', oval + '=>' + val)
                if (val === 'data') {  //

                } else {        //
                    this.model.isSync = false
                    this.$nextTick(() => {
                        this.form.setFieldsValue(pick(this.model, 'isSync'))
                    })
                }
            },
        },
        created() {
        },
        methods: {
            validateDuplicateCode(modelId, eventId='', eventCode='', callback) {
                getAction(this.url.validateCode, { deviceModelId: modelId, eventId:eventId, eventCode: eventCode }).then(res => {
                    res['success'] ? callback() : callback(res['message'])
                })
            },
            validateDuplicateName(modelId, eventId='', eventName='', callback){
                getAction(this.url.validateName, { deviceModelId: modelId, eventId:eventId, eventName:eventName }).then(res => {
                    res['success'] ? callback() : callback(res['message'])
                })
            },
            close() {
                this.$emit('close')
                this.confirmLoading = false
                this.visible = false
            },
            add(record) {
                this.edit(record)
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                console.log(this.model)
                let fieldval = pick(this.model, 'id', 'deviceModelBy', 'code', 'name', 'type', 'level', 'notifyType', 'dataBy', 'dataRule', 'ruleValue', 'description')
                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldval)
                })
            },
            handleOk() {
                const that = this
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    console.log(err, values)
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
                            console.log(res)
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
            getDataOptions(visible) {
                if (!visible) return
                let that = this
                getAction('/sys/dict/getDictItems/iot_device_data,name,id,rw_author!=\'w\' and device_model_by=' + this.deviceModelId, null).then(res => {
                    console.log(res)
                    if (res.success) {
                        that.dataOptions = res.result
                    }
                })
            }
        }

    }
</script>

<style lang="less" scoped>

</style>