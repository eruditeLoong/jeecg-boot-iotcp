<template>
    <a-drawer :afterVisibleChange="visible=>getInputParamsOptions(visible)"
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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="功能类型">
                    <j-dict-select-tag
                        :trigger-change="true"
                        @change="(val)=>this.model.type=val"
                        dictCode="dm_func_type"
                        type="radio"
                        v-decorator="['type', validatorRules.type]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否同步">
                    <a-switch :disabled="model.type=='back'" checkedChildren="同步" unCheckedChildren="异步" v-model="model.isSync"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="{span:'24'}" label="">
                    <a-card hoverable title="输入参数">
                        <template slot="extra">
                            <a-button @click="addRowParam" icon="plus"></a-button>
                        </template>
                        <a-row style="margin-bottom:10px" type="flex">
                            <a-col :span="9">参数名称</a-col>
                            <a-col :span="9">输入方式</a-col>
                            <a-col :span="6">操作</a-col>
                        </a-row>
                        <a-row :gutter="10" :key="index" style="margin:0px;" type="flex" v-for="(param, index) in model.inputParams">
                            <a-col :span="9">
                                <a-form-item>
                                    <a-select :allowClear="true"
                                              :options="inputParamsOptions"
                                              mode="default"
                                              placeholder="请选择输入参数"
                                              style="width: 100%"
                                              v-decorator="['inputParams['+index+'].dataId',
                                                { rules:[{required: true, message: '请选择输入参数'}]}]"
                                    >
                                    </a-select>
                                </a-form-item>
                            </a-col>
                            <a-col :span="9">
                                <a-form-item>
                                    <j-multi-select-tag
                                        :triggerChange="true"
                                        dictCode="dm_input_mode"
                                        placeholder="选择输入方式"
                                        v-decorator="['inputParams['+index+'].inputMode',
                                                                  {rules: [{ required:true, message: '请选择输入方式!' }]}]"/>
                                </a-form-item>
                            </a-col>
                            <a-col :span="6">
                                <a-form-item>
                                    <a-button @click="delRowParam(index)" icon="minus"></a-button>
                                </a-form-item>
                            </a-col>
                        </a-row>
                    </a-card>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="返回数据" v-show="model.isSync">
                    <j-search-select-tag
                        :dictOptions="backDataOptions"
                        :triggerChange="true"
                        placeholder="请输入用户性别"
                        type="list"
                        v-decorator="['outputData', {rules:[{required: false, message: '请选择返回数据'}]}]"/>
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
                backDataOptions: [],
                inputParamsOptions: [],
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
                    add: '/device/deviceModel/addFunc',
                    edit: '/device/deviceModel/editFunc',
                    listInputParams: '/device/deviceModel/listInputParams',
                    validateCode: '/device/deviceModel/validateFuncCode',
                    validateName: '/device/deviceModel/validateFuncName',
                }
            }
        },
        computed: {
            funcType() {
                return this.model.type
            }
        },
        watch: {
            funcType(val, oval) {
                console.log('watch-type：', oval + '=>' + val)
                if (val === 'front') {  // 前端操控：可同步，可异步

                } else {        // 后台执行：只异步
                    this.model.isSync = false
                    this.$nextTick(() => {
                        this.form.setFieldsValue(pick(this.model, 'isSync'))
                    })
                }
            },
            funcIsSync(val, oval) {
                console.log('watch-isSync：', typeof val, oval + '=>' + val)
            }
        },
        created() {
            this.getSelectOptions()
        },
        methods: {
            validateDuplicateCode(modelId, funcId='', funcCode='', callback) {
                console.log("22222");
                getAction(this.url.validateCode, { deviceModelId: modelId, funcId:funcId, funcCode: funcCode }).then(res => {
                    res['success'] ? callback() : callback(res['message'])
                })
            },
            validateDuplicateName(modelId, funcId='', funcName='', callback){
                console.log("5555");
                getAction(this.url.validateName, { deviceModelId: modelId, funcId:funcId, funcName:funcName }).then(res => {
                    res['success'] ? callback() : callback(res['message'])
                })
            },
            addRowParam() {
                this.model.inputParams.push({})
                this.$forceUpdate()
            },
            delRowParam(index) {
                console.log(index)
                this.model.inputParams.splice(index, 1)
                this.$forceUpdate()
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
                let fieldval = pick(this.model, 'id', 'deviceModelBy', 'code', 'name', 'type', 'isSync', 'inputParams', 'outputData', 'description')
                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldval)
                })
                // this.getSelectOptions();
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

                        formData['inputParams'] = JSON.stringify(formData['inputParams'])
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
            getInputParamsOptions(visible) {
                if (!visible) return
                let that = this
                getAction('/sys/dict/getDictItems/iot_device_data,name,id,rw_author!=\'r\' and device_model_by=' + this.deviceModelId, null).then(res => {
                    console.log(res)
                    if (res.success) {
                        that.inputParamsOptions = res.result
                    }
                })
            },
            getSelectOptions() {
                let that = this
                getAction('/sys/dict/getDictItems/iot_device_data,name,id,rw_author!=\'w\' and device_model_by=' + this.deviceModelId, null).then(res => {
                    console.log(res)
                    if (res.success) {
                        that.backDataOptions = res.result
                    }
                })
            }
        }

    }
</script>

<style lang="less" scoped>

</style>