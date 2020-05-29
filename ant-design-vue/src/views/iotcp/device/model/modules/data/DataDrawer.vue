<template>
    <a-drawer :closable="false"
              :title="title"
              :visible="visible"
              :width="width"
              @close="close"
              placement="right"
    >
        <a-spin :spinning="confirmLoading">
            <!-- 主表单区域 -->
            <a-form :form="form">
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标识">
                    <a-input placeholder="请输入标识" v-decorator="[ 'code', validatorRules.code]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称">
                    <a-input placeholder="请输入名称" v-decorator="[ 'name', validatorRules.name]"></a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据属性">
                    <j-dict-select-tag
                        :trigger-change="true"
                        @change="val=>this.model.attrType=val"
                        dictCode="dm_attr_type"
                        placeholder="请选择属性类型"
                        type="list"
                        v-decorator="[ 'attrType', validatorRules.attrType]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="读写权限">
                    <j-dict-select-tag
                        :trigger-change="true"
                        @change="val=>this.model.rwAuthor=val"
                        dictCode="dm_rw_author"
                        placeholder="请选择读写权限"
                        type="radio"
                        v-decorator="[ 'rwAuthor', validatorRules.rwAuthor]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据类型">
                    <JSearchSelectTag
                        :trigger-change="true"
                        @change="val=>this.model.valueType.type=val"
                        dict="dm_data_type"
                        placeholder="请选择数据类型"
                        v-decorator="['valueType.type', validatorRules.dataType]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="单位" v-show="'int,float,double'.indexOf(model.valueType.type)!=-1">
                    <a-input placeholder="请输入单位" v-decorator="['valueType.unit', validatorRules.unit]">
                        <a-tooltip slot="suffix" title="如：摄氏度[℃]，单位文字[单位符号]">
                            <a-icon style="color: rgba(0,0,0,.45)" type="info-circle"/>
                        </a-tooltip>
                    </a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="最小值" v-show="'int,float,double'.indexOf(model.valueType.type)!=-1">
                    <a-input-number placeholder="最小值" v-decorator="['valueType.minValue',
                            { initialValue:0, rules: [{ required: 'int,float,double'.indexOf(model.valueType.type) != -1 ? true : false, message: '请输入最小数值!' },
                            {type: 'number', message: '请输入数字！'}] }]"></a-input-number>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="最大值" v-show="'int,float,double'.indexOf(model.valueType.type)!=-1">
                    <a-input-number placeholder="最大值" v-decorator="['valueType.maxValue',
                            { initialValue:100, rules: [{ required: 'int,float,double'.indexOf(model.valueType.type) != -1 ? true : false, message: '请输入最大数值!' },
                            {type: 'number', message: '请输入数字！'}] }]"></a-input-number>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="精度" v-show="'float,double'.indexOf(model.valueType.type)!=-1">
                    <a-input-number
                        :max="model.valueType.type=='float'?5:12" :min="1"
                        placeholder="请输入精度"
                        v-decorator="['valueType.accuracy',
                    { initialValue:0, rules: [{ required: 'float,double'.indexOf(model.valueType.type) != -1 ? true : false, message: '请输入精度!' },
                    {type: 'number', message: '请输入数字！'}] }]">
                    </a-input-number> 如：2，表示两位小数点
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="字符长度" v-show="'string'.indexOf(model.valueType.type)!=-1">
                    <a-input addonAfter="字节" placeholder="请输入字符长度" v-decorator="['valueType.strLength', validatorRules.strLength]">
                        <a-tooltip slot="suffix" title="如：2，表示2个字节长度">
                            <a-icon style="color: rgba(0,0,0,.45)" type="info-circle"/>
                        </a-tooltip>
                    </a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="布尔值" v-show="'bool'.indexOf(model.valueType.type)!=-1">
                    <a-input
                        placeholder="true说明"
                        v-decorator="['valueType.trueText',
                                { rules: [{ required: 'bool'.indexOf(model.valueType.type) != -1 ? true : false, message: '请填写true说明!' }] }]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label=" " v-show="'bool'.indexOf(model.valueType.type)!=-1">
                    <a-input
                        placeholder="false说明"
                        v-decorator="['valueType.falseText',
                                { rules: [{ required: 'bool'.indexOf(model.valueType.type) != -1 ? true : false, message: '请填写false说明!' }] }]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数组元素类型" v-show="'array'.indexOf(model.valueType.type)!=-1">
                    <JSearchSelectTag
                        :trigger-change="true"
                        @change="val=>this.model.valueType.arrayType=val"
                        dict="dm_data_type"
                        placeholder="请选择数组元素类型"
                        v-decorator="['valueType.arrayType',
                        { rules: [{ required: 'array'.indexOf(model.valueType.type) != -1 ? true : false, message: '请选择数组元素类型!' }] }]"
                    />
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="元素个数" v-show="'array'.indexOf(model.valueType.type)!=-1">
                    <a-input-number placeholder="请输入元素个数" v-decorator="['valueType.arraySize', validatorRules.arraySize]"></a-input-number>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="枚举项" v-show="'enum'.indexOf(model.valueType.type)!=-1">
                    <a-textarea allowClear placeholder="请输入枚举项，json数组格式" rows="3" v-decorator="['valueType.enumOptions', validatorRules.enumOptions]">
                    </a-textarea>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="时间格式" v-show="'datetime'.indexOf(model.valueType.type)!=-1">
                    <a-input placeholder="请输入时间格式" v-decorator="['valueType.datetimeFmt',
                    { rules: [{ required: 'datetime'.indexOf(model.valueType.type) != -1 ? true : false, message: '请输入时间格式!' }] }]">
                        <a-tooltip slot="suffix" title="y:年 M:月 d:日 H:24时 h:12时 m:分秒 S:毫秒">
                            <a-icon style="color: rgba(0,0,0,.45)" type="info-circle"/>
                        </a-tooltip>
                    </a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="密码校验" v-show="'password'.indexOf(model.valueType.type)!=-1">
                    <a-input placeholder="请输入密码校验规则" v-decorator="['valueType.passwordValid', validatorRules.passwordValid]">
                        <a-tooltip slot="suffix" title="正则表达式：至少8个字符，至少1个字母，1个数字和1个特殊字符：^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$">
                            <a-icon style="color: rgba(0,0,0,.45)" type="info-circle"/>
                        </a-tooltip>
                    </a-input>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="文件类型" v-show="'file'.indexOf(model.valueType.type)!=-1">
                    <j-dict-select-tag
                        :trigger-change="true"
                        @change="val=>this.model.valueType.fileType=val"
                        dictCode="dm_file_type"
                        type="radio"
                        v-decorator="['valueType.fileType',
                        { rules: [{ required: 'file'.indexOf(model.valueType.type) != -1 ? true : false, message: '请选择文件类型!' }] }]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="网页打开方式" v-show="'webpage'.indexOf(model.valueType.type)!=-1">
                    <j-dict-select-tag
                        :trigger-change="true"
                        @change="val=>this.model.valueType.fileType=val"
                        dictCode="dm_web_open_type"
                        type="list"
                        v-decorator="['valueType.webOpenType',
                        { rules: [{ required: 'webpage'.indexOf(model.valueType.type) != -1 ? true : false, message: '请选择网页打开类型!' }] }]"/>
                </a-form-item>
                <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述">
                    <a-textarea placeholder="请输入描述" rows="4" v-decorator="['description']"/>
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

    export default {
        name: 'DataDrawer',
        // mixins: [JeecgListMixin],
        components: {
            JSearchSelectTag, JDictSelectTag
        },
        data() {
            return {
                form: this.$form.createForm(this),
                title: '',
                width: 500,
                visible: false,
                valueTypeMap: {
                    int: ['maxValue', 'minValue', 'unit'],
                    float: ['unit', 'maxValue', 'minValue', 'accuracy'],
                    double: ['maxValue', 'minValue', 'unit', 'accuracy'],
                    string: ['strLength'],
                    bool: ['trueText', 'falseText'],
                    array: ['arraySize', 'arrayType'],
                    enum: ['enumOptions'],
                    time: ['timeFmt'],
                    password: ['passwordValid'],
                    file: ['fileType'],
                    webpage: ['webOpenType']
                },
                model: {
                    valueType: { type: undefined }
                },
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
                            { min: 2, max: 20, message: '标识长度：2-20个字符' },
                            {
                                validator: (rule, value, callback) => this.validateDuplicateCode(this.model.deviceModelBy, this.model.id, value, callback)
                            }
                        ]
                    },
                    name: {
                        rules: [
                            { required: true, message: '请输入名称!' },
                            { min: 2, max: 20, message: '名称长度：2-20个字符' },
                            { validator: (rule, value, callback) => this.validateDuplicateName(this.model.deviceModelBy, this.model.id, value, callback) }
                        ]
                    },
                    attrType: { rules: [{ required: false, message: '请选择属性类型!' }] },
                    rwAuthor: { initialValue: 'rw', rules: [{ required: true, message: '请选择读写权限!' }] },
                    dataType: { initialValue: 'r', rules: [{ required: true, message: '请选择数据类型!' }] },
                    unit: {
                        rules: [
                            { required: false, message: '请输入单位!' },
                            { pattern: '[\\s\\S]{1,10}\\[[\\s\\S]{1,10}\\]$', message: '请输入正确单位格式' }
                        ]
                    },
                    strLength: { rules: [{ required: false, message: '请输入字符长度!' }, { pattern: '^[1-9][0-9]{0,5}$', message: '请输入整数' }] }
                },
                url: {
                    add: '/device/deviceModel/addData',
                    edit: '/device/deviceModel/editData',
                    validateCode: '/device/deviceModel/validateDataCode',
                    validateName: '/device/deviceModel/validateDataName'
                }
            }
        },
        computed: {
            dataType() {
                return this.model.valueType['type']
            }
        },
        watch: {
            dataType(val, oval) {
                console.log('watch-dataType', oval + '=>' + val)

            }
        },
        methods: {
            validateDuplicateCode(modelId, dataId = '', dataCode = '', callback) {
                getAction(this.url.validateCode, { deviceModelId: modelId, dataId: dataId, dataCode: dataCode }).then(res => {
                    res['success'] ? callback() : callback(res['message'])
                })
            },
            validateDuplicateName(modelId, dataId = '', dataName = '', callback) {
                getAction(this.url.validateName, { deviceModelId: modelId, dataId: dataId, dataName: dataName }).then(res => {
                    res['success'] ? callback() : callback(res['message'])
                })
            },
            close() {
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
                this.$nextTick(() => {
                    let fieldval = pick(this.model, 'id', 'deviceModelBy', 'code', 'name', 'valueType', 'attrType', 'rwAuthor', 'description')
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
                        let data = Object.assign(this.model, values)

                        let formData = {}
                        formData['id'] = this.model.id
                        formData['code'] = data.code
                        formData['name'] = data.name
                        formData['attrType'] = data.attrType
                        formData['deviceModelBy'] = data.deviceModelBy
                        formData['rwAuthor'] = data.rwAuthor
                        formData['valueType'] = {
                            type: data.valueType.type
                        }
                        formData['description'] = data.description

                        let map = this.valueTypeMap[data.valueType.type]
                        for (let i in map) {
                            formData.valueType[map[i]] = data.valueType[map[i]]
                        }
                        formData.valueType = JSON.stringify(formData.valueType)
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
            }
        }
    }
</script>

<style lang="less" scoped>

</style>