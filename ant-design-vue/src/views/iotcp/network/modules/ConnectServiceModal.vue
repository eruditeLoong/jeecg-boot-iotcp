<template>
    <a-drawer
        :closable="true"
        :title="title"
        :visible="visible"
        :width="width"
        @close="close"
        placement="right"
    >
        <a-card :hoverable="true" size="small" style="cursor:default;margin-top: 10px;padding: 0" title="配置信息">
            <a-descriptions :column="2" bordered size="small">
                <a-descriptions-item label="名称">
                    {{model.name}}
                </a-descriptions-item>
                <a-descriptions-item label="标识">
                    {{model.code}}
                </a-descriptions-item>
                <a-descriptions-item label="图标">
                    <a-avatar :src="getImgView(model.icon)" slot="avatar"/>
                </a-descriptions-item>
                <a-descriptions-item label="服务类型">
                    {{model.type}}
                </a-descriptions-item>
                <a-descriptions-item label="地址">
                    {{model.host}}
                </a-descriptions-item>
                <a-descriptions-item label="端口">
                    {{model.port}}
                </a-descriptions-item>
                <a-descriptions-item label="线程数">
                    {{model.threadNum}}
                </a-descriptions-item>
                <a-descriptions-item label="是否同步">
                    {{model.isSync}}
                </a-descriptions-item>
                <a-descriptions-item label="用户名">
                    {{model.username}}
                </a-descriptions-item>
                <a-descriptions-item label="密码">
                    {{model.password}}
                </a-descriptions-item>
                <a-descriptions-item label="客户端id">
                    {{model.clientId}}
                </a-descriptions-item>
                <a-descriptions-item label="订阅主题">
                    {{model.topics}}
                </a-descriptions-item>
                <a-descriptions-item label="解析方式">
                    {{model.resolMethod}}
                </a-descriptions-item>
                <a-descriptions-item :span="2" label="描述">
                    {{model.description}}
                </a-descriptions-item>
            </a-descriptions>
        </a-card>
        <a-card :hoverable="true" size="small" style="cursor:default;margin-top: 10px;padding: 0" title="服务状态">
            <a-descriptions :column="1" bordered size="small">
                <a-descriptions-item label="创建时间">{{model.createTime}}</a-descriptions-item>
                <a-descriptions-item label="启动时间">{{model.createTime}}</a-descriptions-item>
                <a-descriptions-item label="运行时长">3</a-descriptions-item>
                <a-descriptions-item label="连接数量">3</a-descriptions-item>
            </a-descriptions>
        </a-card>
        <a-card :hoverable="true" :title="connectTitle" size="small" style="cursor:default;margin-top: 10px">
            <template slot="extra">
                <a-button @click="configConnect(model.id)" ghost icon="plus-circle" type="primary" v-if="false">配置</a-button>
            </template>
            <a-list :dataSource="connectList">
                <a-list-item slot="renderItem" slot-scope="item, index">
                    <a-list-item-meta :description="item.addr">
                        <a-button @click="selectClient(item)" slot="title" type="link">{{item.name}}</a-button>
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
        <ConfigConnectModal @ok="getConnectList" ref="configConnect"></ConfigConnectModal>
    </a-drawer>
</template>

<script>
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import { getAction } from '@/api/manage'
    import ConfigConnectModal from './ConfigConnectModal'

    export default {
        name: 'DetailServiceModal',
        mixins: [JeecgListMixin],
        components: {
            ConfigConnectModal
        },
        data() {
            return {
                title: '组件信息',
                width: 500,
                visible: false,
                model: {},
                connectList: [],
                url: {
                    list: '/network/networkService/list'
                }
            }
        },
        computed: {
            connectTitle() {
                return (this.model.type + '').indexOf('server') != -1 ? '客户端列表' : '服务端信息'
            }
        },
        methods: {
            connect(record) {
                this.visible = true
                this.model = record
                this.title = '链接信息'
                this.getConnectList(record.id)
            },
            close() {
                this.$emit('close')
                this.visible = false
            },
            getConnectList(serviceId) {
                let that = this
                getAction('/network/networkService/listConnectById', { id: serviceId }).then(res => {
                    if (res.success) {
                        that.connectList = res.result
                    }
                })
            },
            configConnect(serviceId) {
                this.$refs.configConnect.config(serviceId)
            }
        }
    }
</script>

<style lang="less" scoped>

    .detail-card {
        margin-bottom: 20px;

        .row-bottom-line {
            border-bottom: 1px #f0f0f0 solid;

            div {
                height: 36px;
                line-height: 36px;
            }
        }
    }

</style>