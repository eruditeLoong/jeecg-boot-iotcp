<template>
    <div>
        <template slot="action">
            <a-button @click="getDeviceDetail" icon="reload" shape="circle" title="重新获取数据" type="link">刷新</a-button>
        </template>
        <a-card :hoverable="true" size="small"
                style="cursor:default;margin-top: 10px;padding: 0"
                title="设备实例" v-if="tabsKey=='instanceInfo'">
            <a-descriptions :column="3" bordered size="small">
                <a-descriptions-item label="实例主键">{{model.deviceInstance.id}}</a-descriptions-item>
                <a-descriptions-item label="实例名称">{{model.deviceInstance.name}}</a-descriptions-item>
                <a-descriptions-item label="实例标识">{{model.deviceInstance.code}}</a-descriptions-item>
                <a-descriptions-item label="所属机构">
                    <j-select-depart
                        :disabled="true"
                        :multi="false"
                        v-model="model.deviceInstance.orgBy"/>
                </a-descriptions-item>
                <a-descriptions-item label="上级实例">
                    <j-dict-select-tag
                        :disabled="true"
                        dictCode="iot_device_instance,name,id"
                        v-model="model.deviceInstance.parentBy"
                    />
                </a-descriptions-item>
                <a-descriptions-item label="说明">{{model.deviceInstance.description}}</a-descriptions-item>
            </a-descriptions>
        </a-card>

        <a-card :hoverable="true" size="small"
                style="cursor:default;margin-top: 10px;padding: 0"
                title="设备模型" v-if="tabsKey=='instanceInfo'">
            <a-descriptions :column="3" bordered size="small">
                <a-descriptions-item label="模型主键">{{model.deviceModel.id}}</a-descriptions-item>
                <a-descriptions-item label="模型名称">{{model.deviceModel.name}}</a-descriptions-item>
                <a-descriptions-item label="模型标识">{{model.deviceModel.code}}</a-descriptions-item>
                <a-descriptions-item label="设备类型">
                    <j-dict-select-tag
                        :disabled="true"
                        dictCode="dm_type"
                        v-model="model.deviceModel.type"/>
                </a-descriptions-item>
                <a-descriptions-item label="传输协议">
                    <j-dict-select-tag
                        :disabled="true"
                        dictCode="iot_protocol_link,name,id"
                        v-model="model.deviceModel.linkProtocolBy"
                    />
                </a-descriptions-item>
                <a-descriptions-item label="数据解析">
                    <j-dict-select-tag
                        :disabled="true"
                        dictCode="iot_protocol_data,name,id"
                        v-model="model.deviceModel.dataProtocolBy"
                    />
                </a-descriptions-item>
                <a-descriptions-item label="说明">{{model.deviceModel.description}}</a-descriptions-item>
            </a-descriptions>
        </a-card>

        <template v-if="tabsKey=='runningState'">
            <a-row :gutter="24">
                <a-col :md="12" :sm="24" :style="{ marginBottom: '24px' }" :xl="6">
                    <chart-card :loading="loading" title="在线状态">
                        <a-tooltip slot="action" title="状态说明">
                            <a-icon type="info-circle-o"/>
                        </a-tooltip>
                        <template slot="total">
                            <li v-show="model.deviceInstance.status=='online'">
                                <a-icon style="color:green;" theme="filled" title="在线" type="check-circle"/>
                                在线
                            </li>
                            <li v-show="model.deviceInstance.status=='notActive'">
                                <a-icon style="color:gray" theme="filled" title="未激活" type="close-circle"/>
                                未激活
                            </li>
                            <li v-show="model.deviceInstance.status=='offline'">
                                <a-icon style="color:red" theme="filled" title="离线" type="exclamation-circle"/>
                                离线
                            </li>
                        </template>
                        <template slot="footer">上线时间：2020/05/18 12:34:33</template>
                    </chart-card>
                </a-col>
                <template v-for="(deviceData, key, index) in model.deviceDataList">
                    <a-col :md="12" :sm="24" :style="{ marginBottom: '24px' }" :xl="6" v-if="deviceData.rwAuthor=='r'">
                        <chart-card
                            :loading="loading"
                            :title="deviceData.name"
                            :total="(deviceData.valueType.value||'--')+' '+ deviceData.valueType.unitCode||''"
                        >
                            <div v-if="'int,float,double'.indexOf(deviceData.valueType.type)!=-1">
                                <mini-progress
                                    :height="8"
                                    :percentage="deviceData.valueType.value-0"
                                    :target="deviceData.valueType.maxValue"
                                    color="rgb(19, 194, 194)"/>
                            </div>
                            <div v-if="'bool'.indexOf(deviceData.valueType.type)!=-1">
                                <a-switch
                                    :checked="true"
                                    :checkedChildren="deviceData.valueType.trueText"
                                    :unCheckedChildren="deviceData.valueType.falseText"
                                />
                            </div>
                            <template slot="footer">上报时间：{{deviceData.valueType.reportTime||'暂无数据'}}</template>
                        </chart-card>
                    </a-col>
                </template>
            </a-row>
        </template>
        <template v-if="tabsKey=='reportData'">
            <DataReport-search :deviceInstanceId="model.deviceInstance.id"></DataReport-search>
        </template>
        <template v-if="tabsKey=='functionConfig'">
            <a-card v-if="(model.deviceFunctionList||[]).length>0">
                <a-form :form="form">
                    <a-tabs
                        :default-active-key="model.deviceFunctionList[0].code"
                        @change="funcActiveChange"
                        tab-position="left"
                    >
                        <a-tab-pane :key="func.code" :tab="func.name" v-for="(func, index) in model.deviceFunctionList">
                            <a-card size="small" title="输入参数">
                                <div v-for="(inputParam, index) in func.inputParams">
                                    <a-form-item :label="inputParam.name+'('+inputParam.valueType.type+')'" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <template v-if="'bool'.indexOf(inputParam.valueType.type)!=-1">
                                            <a-switch
                                                :checkedChildren="inputParam.valueType.trueText"
                                                :readOnly="inputParamReadonly"
                                                :unCheckedChildren="inputParam.valueType.falseText"
                                                v-decorator="[func.code+'.'+inputParam.code, {initialValue: inputParam.value}]"
                                            />
                                        </template>
                                        <template v-if="'int'.indexOf(inputParam.valueType.type)!=-1">
                                            <a-input-number
                                                :max="inputParam.valueType.maxValue"
                                                :min="inputParam.valueType.minValue"
                                                :placeholder="'请输入'+inputParam.name"
                                                :readOnly="inputParamReadonly"
                                                v-decorator="[func.code+'.'+inputParam.code, {
                                                 initialValue: inputParam.value,
                                                 rules:[
                                                     {required:true, message:'请输入'+inputParam.name}
                                                     ]
                                             }]"
                                            />
                                            <a-tag color="blue" style="margin-left: 10px" v-if="inputParam.valueType.unit">
                                                {{inputParam.valueType.unit}}
                                            </a-tag>
                                        </template>
                                        <template v-if="'float,double'.indexOf(inputParam.valueType.type)!=-1">
                                            <a-input-number
                                                :max="inputParam.valueType.maxValue"
                                                :min="inputParam.valueType.minValue"
                                                :placeholder="'请输入'+inputParam.name"
                                                :precision="inputParam.valueType.accuracy"
                                                :readOnly="inputParamReadonly"
                                                v-decorator="[func.code+'.'+inputParam.code, {
                                                 initialValue: inputParam.value,
                                                 rules:[
                                                     {required:true, message:'请输入'+inputParam.name}
                                                     ]
                                             }]"
                                            />
                                            <a-tag color="blue" style="margin-left: 10px" v-if="inputParam.valueType.unit">
                                                {{inputParam.valueType.unit}}
                                            </a-tag>
                                        </template>
                                        <template v-if="'string'.indexOf(inputParam.valueType.type)!=-1">
                                            <a-input :placeholder="'请输入'+inputParam.name"
                                                     :readOnly="inputParamReadonly"
                                                     v-decorator="[func.code+'.'+inputParam.code, {
                                                     initialValue: inputParam.value,
                                                     rules:[
                                                         {required:true, message:'请输入'+inputParam.name},
                                                         {max: inputParam.valueType.strLength-0 , message:'超出字符串最大长度：'+inputParam.valueType.strLength},
                                                     ]
                                                 }]"
                                            ></a-input>
                                        </template>
                                        <template v-if="'ip'.indexOf(inputParam.valueType.type)!=-1">
                                            <a-input :placeholder="'请输入'+inputParam.name"
                                                     :readOnly="inputParamReadonly"
                                                     v-decorator="[func.code+'.'+inputParam.code, {
                                                     initialValue: inputParam.value,
                                                     rules:[
                                                         {required:true, message:'请输入'+inputParam.name},
                                                         {pattern: '^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$', message:'请正确输入IP地址'},
                                                     ]
                                                 }]"
                                            ></a-input>
                                        </template>
                                        <template v-if="'address'.indexOf(inputParam.valueType.type)!=-1">
                                            <a-input
                                                :placeholder="'请输入'+inputParam.name"
                                                :readOnly="inputParamReadonly"
                                                v-decorator="[func.code+'.'+inputParam.code, {
                                                     initialValue: inputParam.value,
                                                     rules:[
                                                         {required:true, message:'请输入'+inputParam.name},
                                                         {pattern: '^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\:\\d{2,6}$', message:'请正确输入TCP地址[IP:PORT]'},
                                                     ]
                                                 }]"
                                            ></a-input>
                                        </template>
                                        <template v-if="'array'.indexOf(inputParam.valueType.type)!=-1">
                                            <a-textarea :placeholder="'请输入'+inputParam.name" :readOnly="inputParamReadonly"
                                                        :rows="3"
                                                        v-decorator="[func.code+'.'+inputParam.code, {
                                                     initialValue: inputParam.value,
                                                     rules:[{required:true, message:'请输入'+inputParam.name}]
                                                 }]"
                                            ></a-textarea>
                                        </template>
                                    </a-form-item>
                                </div>
                            </a-card>
                            <a-card size="small" style="margin-top: 16px" title="用户操作">
                            <span class="table-page-search-submitButtons" slot="extra" style="float: left;overflow: hidden;">
                                <a-button @click="execFunction(func.code)" type="primary">执行</a-button>
                                <!--                                <a-button @click="execFunction(func.code)" type="primary">执行</a-button>-->
                            </span>

                                <template>
                                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="执行模式">
                                        <template slot="help">
                                            <li>调试模式：输入参数可修改，可同步|异步，只执行1次</li>
                                            <li>任务模式：输入参数不可修改，只能异步</li>
                                        </template>
                                        <a-radio-group
                                            @change="execModeChange"
                                            v-decorator="['execConf.execMode', {initialValue: 'debug'}]">
                                            <a-radio value="debug">调试模式</a-radio>
                                            <a-radio value="task">任务模式</a-radio>
                                        </a-radio-group>
                                    </a-form-item>
                                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="间隔时长">
                                        <j-cron ref="innerVueCron" v-decorator="['execConf.corn', { initialValue: '* * * * * ? *' }]"></j-cron>
                                    </a-form-item>
                                    <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否同步">
                                        <a-switch
                                            checkedChildren="同步"
                                            unCheckedChildren="异步"
                                            v-decorator="['execConf.isSync', {initialValue: func.isSync}]"
                                        />
                                    </a-form-item>
                                </template>
                            </a-card>
                            <a-card size="small" style="margin-top: 16px" title="返回结果">
                                {{func.outputData}}
                                <a-form-item :label="func.outputData.name" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                    <a-textarea :readOnly="true" rows="3">{{model.outputData}}</a-textarea>
                                </a-form-item>
                            </a-card>
                        </a-tab-pane>
                    </a-tabs>
                </a-form>
            </a-card>
        </template>

    </div>
</template>

<script>
    import ChartCard from '@/components/ChartCard'
    import DashChart from '@/components/chart/DashChartDemo'
    import MiniArea from '@/components/chart/MiniArea'
    import MiniBar from '@/components/chart/MiniBar'
    import MiniProgress from '@/components/chart/MiniProgress'
    import { getAction, httpAction } from '../../../../api/manage'
    import JSearchSelectTag from '@/components/dict/JSearchSelectTag'
    import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'
    import JDictSelectTag from '@/components/dict/JDictSelectTag'
    import DataReportSearch from '../../message/dataReport/DataReportSearch'
    import JCron from '@/components/jeecg/JCron'
    import store from '@/store/'
    import moment from 'moment'
    import pick from 'lodash.pick'

    export default {
        name: 'DeviceInstanceDetail',
        components: {
            DataReportSearch,
            DashChart,
            ChartCard,
            MiniArea,
            MiniBar,
            MiniProgress,
            JSearchSelectTag,
            JSelectDepart,
            JDictSelectTag,
            JCron
        },
        created() {
            setTimeout(() => {
                this.loading = !this.loading
            }, 1000)
            this.getDeviceDetail()
        },
        data() {
            return {
                description: '设备实例详情页面',
                form: this.$form.createForm(this),
                loading: true,
                inputParamReadonly: false,
                model: {},
                execConf: {
                    isSync: false,
                    corn: '* * * * * ? *',
                    execMode: 'debug'
                },
                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 3 }
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 6 }
                },
                avatar: 'https://gw.alipayobjects.com/zos/rmsportal/RzwpdLnhmvDJToTdfDPe.png',
                // extraImage: 'https://gw.alipayobjects.com/zos/rmsportal/RzwpdLnhmvDJToTdfDPe.png',
                tabsKey: 'instanceInfo',
                tabs: {
                    items: [
                        {
                            key: 'instanceInfo',
                            title: '设备信息'
                        },
                        {
                            key: 'runningState',
                            title: '运行状态'
                        },
                        {
                            key: 'reportData',
                            title: '上报数据'
                        },
                        {
                            key: 'functionConfig',
                            title: '功能配置'
                        }
                    ],
                    active: () => {
                        return this.tabsKey
                    },
                    callback: (key) => {
                        switch (key) {
                            case 'instanceInfo':
                                this.tabsKey = 'instanceInfo'
                                break
                            case 'runningState':
                                this.tabsKey = 'runningState'
                                break
                            case 'reportData':
                                this.tabsKey = 'reportData'
                                break
                            case 'functionConfig':
                                this.tabsKey = 'functionConfig'
                                break
                            default:
                        }
                    }
                },
                url: {
                    instanceDetail: 'device/deviceInstance/instanceDetail',
                    execFuncStart: 'device/deviceInstance/execFuncStart'
                }
            }
        },
        methods: {
            execModeChange(e) {
                if (e.target.value === 'task') {
                    // 输入参数不可修改，只能异步
                    this.inputParamReadonly = true;
                    // this.execConf.isSync = false;

                } else {
                    // 输入参数可修改，可同步|异步，只执行1次
                    this.inputParamReadonly = false;
                    // this.execConf.isSync = true;

                }

                this.$nextTick(() => {
                    this.form.setFieldsValue(pick(this.execConf, 'isSync'))
                })
            },
            getDeviceDetail() {
                let that = this
                let deviceInstanceId = this.$route.query.deviceInstanceId
                getAction(this.url.instanceDetail, { 'deviceInstanceId': deviceInstanceId }).then(res => {
                    if (res.success) {
                        that.description = res.result.deviceInstance.name
                        that.model = res.result
                        // 网关设备：删除上报数据tab
                        if (that.model.deviceModel.type == 'gateway') {
                            that.tabs.items.splice(2, 1)
                        }
                        that.model.deviceDataList.filter((data, index, arr) => {
                            data.valueType = JSON.parse(data.valueType)
                            let unit = data.valueType['unit'] || ''
                            data.valueType['unitCode'] = unit.length > 0 ? unit.substring(unit.indexOf('[') + 1, unit.indexOf(']')) : ''
                        })

                        that.model.deviceFunctionList.filter((func, index, arr) => {
                            func.inputParams = JSON.parse(func.inputParams)
                        })

                    }
                })
            },
            execFunction(funcCode) {
                console.log('execFunction', funcCode)
                const that = this
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    console.log(values)
                    if (!err) {
                        let formData = {
                            'funcCode': funcCode,
                            'inputParams': values[funcCode],
                            'execConfig': values.execConf,
                            'deviceInstanceId': this.model.deviceInstance.id,
                            'deviceModelId': this.model.deviceModel.id
                        }
                        console.log(formData)

                        httpAction(this.url.execFuncStart, formData, 'post').then((res) => {
                            if (res.success) {
                                console.log('====>', res.message)
                                that.model.outputData = res.result
                                that.$message.success(res.message)
                            } else
                                that.$message.error(res.message)

                        })
                    }

                })
            },
            funcActiveChange(key) {
                console.log(key)
            },
            /*  */
            initWebSocket: function() {
                // WebSocket与普通的请求所用协议有所不同，ws等同于http，wss等同于https
                var clientId = store.getters.userInfo.id + '-debugService'
                var url = window._CONFIG['domianURL'].replace('https://', 'wss://').replace('http://', 'ws://') + '/websocket/' + clientId
                console.log(url)
                this.websock = new WebSocket(url)
                this.websock.onopen = this.websocketOnopen
                this.websock.onerror = this.websocketOnerror
                this.websock.onmessage = this.websocketOnmessage
                this.websock.onclose = this.websocketOnclose
            },
            websocketOnopen: function() {
                this.appendReceiveContent('开始调试')
                this.debugStarted = true
            },
            websocketOnerror: function(e) {
                console.log('WebSocket连接发生错误')
                this.appendReceiveContent('连接发生错误, 即将重新连接')
                this.debugStarted = false
                this.reconnect()
            },
            websocketOnmessage: function(e) {
                console.log('-----接收消息-------', e.data)
                var data = eval('(' + e.data + ')') //解析对象
                if (data.cmd == 'topic') {
                    //系统通知
                    this.loadData()
                } else if (data.cmd == 'user') {
                    //用户消息
                    this.loadData()
                }
            },
            websocketOnclose: function(e) {
                console.log('connection closed (' + e + ')')
                this.appendReceiveContent('结束调试')
                this.debugStarted = false
            },
            websocketSend(text) { // 数据发送
                try {
                    this.websock.send(text)
                } catch (err) {
                    console.log('send failed (' + err.code + ')')
                }
            },
            reconnect() {
                var that = this
                if (that.lockReconnect) return
                that.lockReconnect = true
                //没连接上会一直重连，设置延迟避免请求过多
                setTimeout(function() {
                    console.info('尝试重连...')
                    that.initWebSocket()
                    that.lockReconnect = false
                }, 5000)
            },
            appendReceiveContent(text) {
                this.receiveContent += (this.isShowTime ? ('[' + moment().format('YYYY-MM-DD HH:mm:ss') + '] ') : '') + text + '\r\n'
            }

        }
    }
</script>

<style lang="less" scoped>
    .ant-input-affix-wrapper {
        box-sizing: border-box;
        margin: 0;
        padding: 0;
        color: rgba(0, 0, 0, 0.65);
        font-size: 14px;
        font-variant: tabular-nums;
        line-height: 1.5;
        list-style: none;
        font-feature-settings: 'tnum';
        position: relative;
        display: inline-block;
        /* width: 100%; */
        text-align: start;
    }

    .ant-input-disabled {
        /* color: rgba(0, 0, 0, 0.25); */
        background-color: snow !important;
        /* cursor: not-allowed; */
        /* opacity: 1; */
    }

    .ant-select-disabled .ant-select-selection {
        /* background: #f5f5f5; */
        cursor: default;
        border: 0;
        color: #505050 !important;
    }

    .ant-select-arrow .ant-select-arrow-icon svg {
        color: #fff !important;
    }
</style>