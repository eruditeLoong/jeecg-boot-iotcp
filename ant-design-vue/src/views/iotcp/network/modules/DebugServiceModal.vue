<template>
    <a-modal
        :closable="false"
        :confirmLoading="confirmLoading"
        :maskClosable="false"
        :title="title"
        :visible="visible"
        :width="width"
        @afterClose="endDebug"
        @cancel="debugClose"
    >
        <a-card :hoverable="true" size="small" style="width: 100%;margin-bottom:10px;cursor:default" title="连接信息">
            <a-row>
                <a-col :span="8">服务类型：{{model.type}}</a-col>
                <a-col :span="8">服务名称：{{model.name}}</a-col>
                <a-col :span="8">地址：{{model.host}}:{{model.port}}</a-col>
            </a-row>
        </a-card>
        <a-row :gutter="10">
            <a-col :span="8">
                <a-card :hoverable="true" :title="leftTitle" size="small"
                        style="width:300px;height:500px;cursor:default">
                    <template slot="extra">
                        <a-button @click="getConnectList" icon="reload" type="link" shape="circle" :title="'刷新'+leftTitle"></a-button>
                        <a-button :disabled="!client.isConnected" @click="startDebug" ghost icon="play-circle"
                                  type="primary" v-show="!debugStarted">开始
                        </a-button>
                        <a-button @click="endDebug" ghost icon="pause-circle" type="danger"
                                  v-show="debugStarted">结束
                        </a-button>
                    </template>
                    <a-list :dataSource="connectList">
                        <a-list-item slot="renderItem" slot-scope="item, index">
                            <a-list-item-meta :description="item.addr">
                                <a-button @click="selectClient(item)" slot="title" type="link">{{item.name || '找不到实例设备！'}}</a-button>
                                <!--                                <a @click="selectClient(item)" slot="title">{{item.name}}</a>-->
                                <a-avatar
                                    icon="link"
                                    slot="avatar"
                                    style="backgroundColor: #f0f0f0; color: #00A0E9" v-if="item.isConnected"
                                />
                                <a-avatar
                                    icon="disconnect"
                                    slot="avatar"
                                    style="backgroundColor: #f0f0f0; color: #00A0E9" v-if="!item.isConnected"
                                />
                            </a-list-item-meta>
                            <a-badge status="success" v-if="item.isConnected"/>
                            <a-badge status="default" v-if="!item.isConnected"/>
                        </a-list-item>
                    </a-list>
                </a-card>
            </a-col>
            <a-col :span="16">
                <a-card :hoverable="true" size="small" style="margin-bottom: 10px;height:180px;cursor:default">
                    <template slot="title">
                        <a-button>发送</a-button>
                        <a-divider type="vertical"/>
                        <a-checkbox :value="isSendHex">按16进制发送</a-checkbox>
                    </template>
                    <template slot="extra">
                        <a-button @click="clearSend" icon="export" type="link">清空</a-button>
                    </template>
                    <a-textarea :rows="4" v-model="sendContent"></a-textarea>
                </a-card>
                <a-card :hoverable="true" size="small" style="height:310px;cursor:default">
                    <template slot="title">
                        <span v-if="(model.type+'').indexOf('server')!=-1">
                            客户端：<a-tag color="blue">{{client.name}}</a-tag>
                        </span>
                        <a-divider type="vertical"/>
                        <a-checkbox v-model="isReceiveHex">按16进制接收</a-checkbox>
                        <a-checkbox v-model="isShowTime">显示时间</a-checkbox>
                    </template>
                    <template slot="extra">
                        <a-button @click="clearReceive" icon="export" type="link">清空</a-button>
                    </template>
                    <a-textarea :rows="10" :readOnly="true" v-model="receiveContent"></a-textarea>
                </a-card>
            </a-col>
        </a-row>


        <template slot="footer">
            <a-button @click="debugClose" icon="close" type="" v-show="!debugStarted">关闭</a-button>
        </template>

    </a-modal>
</template>

<script>
    import store from '@/store/'
    import { getAction } from '@/api/manage'
    import moment from 'moment'

    export default {
        name: 'debugServiceModal',
        data() {
            return {
                title: '调试',
                debugStarted: false,
                width: 1000,
                visible: false,
                confirmLoading: false,
                model: {},
                connectList: [],
                client: {},
                isSendHex: false,
                isReceiveHex: false,
                isShowTime: false,
                debugContent: '',
                websock: null,
                lockReconnect: false,
                sendContent: '',
                receiveContent: ''
            }
        },
        computed: {
            leftTitle() {
                return (this.model.type + '').indexOf('server') != -1 ? '客户端列表' : '服务端信息'
            }
        },
        mounted() {
            // 初始化websocket
            // this.initWebSocket()
        },
        destroyed: function() { // 离开页面生命周期函数
            if(this.websock!=null)
                this.websock.close()
        },
        methods: {
            moment,
            debug(record) {
                this.visible = true
                this.model = record
                this.title = record.name + '调试'
                this.receiveContent = ''
                this.getConnectList(record.id)
            },
            getConnectList() {
                let that = this;
                this.client = {};
                getAction(
                    '/network/networkService/listConnectById',
                    { address: this.model.host+':'+this.model.port }
                ).then(res => {
                    if (res.success) {
                        that.connectList = res.result
                    }
                })
            },
            selectClient(client) {
                if(client['name'] == undefined || client['name'] == ''){
                    this.$message.warning('找不到实例设备！请检查实例配置信息。')
                    // alert('找不到实例设备！请检查实例配置信息。');
                    return ;
                }
                this.client = client
            },
            startDebug() {
                if (((this.model.type + '').indexOf('server') != -1) && (this.client.name == undefined)) {
                    this.$message.info('请选择客户端')
                    return
                }
                this.initWebSocket()
            },
            endDebug() {
                this.websock.close()
            },
            clearReceive() {
                this.receiveContent = ''
            },
            clearSend() {
                this.sendContent = ''
            },
            debugClose() {
                this.visible = false
            },
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

<style scoped>

</style>