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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="实例标识">
                    <a-input placeholder="请输入实例标识" v-decorator="[ 'code', validatorRules.code]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="实例名称">
                    <a-input placeholder="请输入实例名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="设备模型">
                    <j-search-select-tag
                        :trigger-change="true"
                        @change="val=>this.model.modelBy=val"
                        dict="iot_device_model,name,id"
                        v-decorator="['modelBy', validatorRules.modelBy]"
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
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属机构">
                    <j-select-depart :multi="false" v-decorator="['orgBy', validatorRules.orgBy]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="说明">
                    <a-textarea placeholder="请输入说明" rows="4" v-decorator="['description', validatorRules.description]"/>
                </a-form-item>
                <template v-if="extendParamsConf.length > 0">
                    <div :key="funcIndex" v-for="(func, funcIndex) in extendParamsConf">
                        <a-card :title="'功能定义：'+func.name" hoverable style="margin-bottom: 10px">
                            <div :key="paramIndex" v-for="(param, paramIndex) in func.inputParams">
                                <a-form-item :label="param.name" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                    <template v-if="'string'.indexOf(param.valueType.type)!=-1">
                                        <a-input :placeholder="'请输入'+param.name"
                                                 v-decorator="[ 'extendParams.'+func.code+'.'+param.code, {
                                                     initialValue: model.extendParams[func.code][param.code],
                                                     rules:[
                                                         {required:true, message:'请输入'+param.name},
                                                         {max: param.valueType.strLength-0 , message:'超出字符串最大长度：'+param.valueType.strLength},
                                                     ]
                                                 }]"
                                        ></a-input>
                                    </template>
                                    <template v-if="'ip'.indexOf(param.valueType.type)!=-1">
                                        <a-input :placeholder="'请输入'+param.name"
                                                 v-decorator="[ 'extendParams.'+func.code+'.'+param.code, {
                                                     initialValue: model.extendParams[func.code][param.code],
                                                     rules:[
                                                         {required:true, message:'请输入'+param.name},
                                                         {pattern: '^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$', message:'请正确输入IP地址'},
                                                     ]
                                                 }]"
                                        ></a-input>
                                    </template>
                                    <template v-if="'address'.indexOf(param.valueType.type)!=-1">
                                        <a-input :placeholder="'请输入'+param.name"
                                                 v-decorator="[ 'extendParams.'+func.code+'.'+param.code, {
                                                     initialValue: model.extendParams[func.code][param.code],
                                                     rules:[
                                                         {required:true, message:'请输入'+param.name},
                                                         {pattern: '^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\:\\d{2,6}$', message:'请正确输入TCP地址[IP:PORT]'},
                                                     ]
                                                 }]"
                                        ></a-input>
                                    </template>
                                    <template v-if="'int'.indexOf(param.valueType.type)!=-1">
                                        <a-input-number
                                            :max="param.valueType.maxValue"
                                            :min="param.valueType.minValue"
                                            :placeholder="'请输入'+param.name"
                                            v-decorator="[ 'extendParams.'+func.code+'.'+param.code, {
                                                 initialValue: model.extendParams[func.code][param.code],
                                                 rules:[
                                                     {required:true, message:'请输入'+param.name}
                                                     ]
                                             }]"
                                        ></a-input-number>
                                        &nbsp;<a-tag color="blue">{{param.valueType.unit}}</a-tag>
                                    </template>
                                    <template v-if="'float,double'.indexOf(param.valueType.type)!=-1">
                                        <a-input-number
                                            :max="param.valueType.maxValue"
                                            :min="param.valueType.minValue"
                                            :placeholder="'请输入'+param.name"
                                            :precision="param.valueType.accuracy"
                                            v-decorator="[ 'extendParams.'+func.code+'.'+param.code, {
                                                 initialValue: model.extendParams[func.code][param.code],
                                                 rules:[
                                                     {required:true, message:'请输入'+param.name}
                                                     ]
                                             }]"
                                        ></a-input-number>
                                        &nbsp;<a-tag color="blue">{{param.valueType.unit}}</a-tag>
                                    </template>
                                    <template v-if="'bool'.indexOf(param.valueType.type)!=-1">
<!--                                        {{'extendParams.'+func.code+'.'+param.code}}={{model.extendParams[func.code][param.code]}}-->
                                        <a-switch
                                            :checkedChildren="param.valueType.trueText"
                                            :unCheckedChildren="param.valueType.falseText"
                                            :checked="model.extendParams[func.code][param.code]"
                                            @change="val=>model.extendParams[func.code][param.code]=val"
                                            v-decorator="[ 'extendParams.'+func.code+'.'+param.code]"
                                        />
                                    </template>
                                    <template v-if="'array'.indexOf(param.valueType.type)!=-1">
                                        <a-textarea :placeholder="'请输入'+param.name" :rows="3"
                                                    v-decorator="[ 'extendParams.'+func.code+'.'+param.code, {
                                                     initialValue: model.extendParams[func.code][param.code],
                                                     rules:[{required:true, message:'请输入'+param.name}]
                                                 }]"
                                        ></a-textarea>
                                    </template>
                                </a-form-item>
                            </div>
                        </a-card>
                    </div>
                </template>
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
    import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
    import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'
    import { getAction, httpAction } from '../../../../../api/manage'

    export default {
        name: 'DeviceInstanceModal',
        components: {
            JSearchSelectTag, JSelectDepart
        },
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
                    edit: '/device/deviceInstance/edit',
                    listInputParams: '/device/deviceInstance/listInputParams',
                    get: '/device/deviceInstance/queryById'
                }
            }
        },
        computed: {
            deviceModelBy() {
                return this.model.modelBy
            }
        },
        watch: {
            deviceModelBy(val, oval) {
                console.log('watch-deviceModelBy', oval, '=>', val)
                this.getExtendParamsCinf(val)
            }
        },
        created() {
        },
        methods: {
            getExtendParamsCinf(val) {
                let that = this
                if (val != undefined) {
                    getAction(this.url.listInputParams, { 'deviceModelId': val }).then(res => {
                        console.log('扩展配置=》', res)
                        if (res.success) {
                            that.extendParamsConf = res.result
                            for (let i in res.result) {
                                let conf = res.result[i]
                                if (that.model.extendParams[conf.code] == undefined) {
                                    that.model.extendParams[conf.code] = {}
                                }
                                for (let j in conf.inputParams || []) {
                                    let inputParam = conf.inputParams[j]
                                    if (that.model.extendParams[conf.code][inputParam.code] == undefined) {
                                        let val
                                        console.log(inputParam.valueType.type)
                                        if (inputParam.valueType.type == 'bool') {
                                            val = false
                                        } else {
                                            val = ''
                                        }
                                        that.model.extendParams[conf.code][inputParam.code] = val
                                        console.log(that.model.extendParams[conf.code][inputParam.code] + '=' + val)
                                    }
                                }
                            }
                            console.log('extendParams==>>>', that.model.extendParams)
                        }
                    })
                }
            },
            parentByChange(parentId) {
                const that = this;
                getAction(this.url.get, {id: parentId}).then(res => {
                    if(res.success){
                        that.model.orgBy = res.result.orgBy;
                        that.$nextTick(() => {
                            this.form.setFieldsValue(pick(this.model, 'orgBy'))
                        })
                    }
                });
            },
            add(record) {
                this.edit(record)
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                console.log('扩展参数=>', JSON.parse(record.extendParams || '{}'))
                this.model.extendParams = JSON.parse(record.extendParams || '{}')
                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.model, 'parentBy', 'code', 'name', 'modelBy', 'orgBy', 'description'))
                })
            },
            close() {
                this.model = {}
                this.extendParamsConf = []
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
                        let formData = JSON.parse(JSON.stringify(Object.assign(this.model, values)))
                        console.log('表单提交数据', formData)
                        formData.extendParams = JSON.stringify(formData.extendParams)
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