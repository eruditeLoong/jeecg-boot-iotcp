<template>
    <div>
        <a-card :bordered="false" class="table-operator">
            <!-- 检索区域 -->
            服务类型：
            <a-radio-group buttonStyle="solid" defaultValue="all">
                <a-radio-button @click="listService()" value="all">全部</a-radio-button>
                <a-radio-button @click="listService('tcp-server')" value="tcp-server">TCP服务</a-radio-button>
                <a-radio-button @click="listService('tcp-client')" value="tcp-client">TCP客户端</a-radio-button>
                <a-radio-button @click="listService('mqtt-server')" value="mqtt-server">MQTT服务</a-radio-button>
                <a-radio-button @click="listService('mqtt-client')" value="mqtt-client">MQTT客户端</a-radio-button>
            </a-radio-group>
            <a-divider type="vertical"/>
            <!-- 操作按钮区域 -->
            <a-button @click="handleAdd" icon="plus" type="primary">新增服务</a-button>
            <a-button @click="handleExportXls('网络服务管理')" icon="download" type="primary">导出</a-button>
            <a-upload :action="importExcelUrl" :headers="tokenHeader" :multiple="false" :showUploadList="false" @change="handleImportExcel" name="file">
                <a-button icon="import" type="primary">导入</a-button>
            </a-upload>
        </a-card>
        <a-list :bordered="true"
                :dataSource="serviceData"
                itemLayout="horizontal">
            <a-list-item slot="renderItem" slot-scope="record, index" style="display:inline-flex;">
                <a-card hoverable style="width:340px;">
                    <a-card-meta :description="record.type" :title="record.name">
                        <a-avatar
                            :src="getImgView(record.icon)"
                            slot="avatar"
                        />
                    </a-card-meta>
                    <a-divider type="horizontal"/>
                    <template>
                        <a-row>
                            <a-col :span="10"><p>host:port：</p></a-col>
                            <a-col :span="14">{{record.host}}:{{record.port}}</a-col>
                        </a-row>
                        <a-row>
                            <a-col :span="10"><p>启停状态：</p></a-col>
                            <a-col :span="14">
<!--                                <a-switch @change="(chected)=>serviceStateChange(chected, record.id)" size="small" v-model="record.state">-->
<!--                                    <a-icon slot="checkedChildren" type="check"/>-->
<!--                                    <a-icon slot="unCheckedChildren" type="close"/>-->
<!--                                </a-switch>-->
                                <a-popconfirm :slot-scope="record" @confirm="() => serviceStateChange(!record.state, record.id)">
                                    <template slot="title">确定{{record.state?'停止':'启动'}}服务吗?</template>
                                    <a-switch
                                        :loading="stateLoading"
                                        size="small"
                                        :checked="record.state"
                                    >
                                        <a-icon slot="checkedChildren" type="check"/>
                                        <a-icon slot="unCheckedChildren" type="close"/>
                                    </a-switch>
                                </a-popconfirm>
                            </a-col>
                        </a-row>
                    </template>
                    <template class="ant-card-actions" slot="actions">
                        <a-icon @click="handleDebug(record)" title="调试" type="bug"/>
                        <a-icon @click="handleEdit(record)" title="编辑" type="edit"/>
                        <a-icon @click="handleConnect(record)" title="链接" type="link"/>
                        <a-popconfirm @confirm="() => deleteService(record.id)" title="确定删除吗?">
                            <a-icon title="删除" type="delete"/>
                        </a-popconfirm>
                    </template>
                </a-card>
            </a-list-item>
        </a-list>
        <networkService-modal ref="modalForm" @ok="modalFormOk"></networkService-modal>
        <debugService-modal ref="debugModal"></debugService-modal>
        <connectService-modal ref="connectModal"></connectService-modal>
    </div>
</template>

<script>
    import { deleteAction, getAction } from '@/api/manage'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import NetworkServiceModal from './modules/NetworkServiceModal'
    import DebugServiceModal from './modules/DebugServiceModal'
    import ConnectServiceModal from './modules/ConnectServiceModal'

    export default {
        name: 'serviceManager',
        mixins: [JeecgListMixin],
        components: {
            NetworkServiceModal,
            DebugServiceModal,
            ConnectServiceModal
        },
        data() {
            return {
                description: '网络服务管理页面',
                columns: [],
                stateLoading: false,
                url: {
                    list: '/network/networkService/list',
                    delete: '/network/networkService/delete',
                    deleteBatch: '/network/networkService/deleteBatch',
                    exportXlsUrl: '/network/networkService/exportXls',
                    importExcelUrl: '/network/networkService/importExcel',
                    startService: '/network/networkService/startService',
                    stopService: '/network/networkService/stopService'
                },
                serviceData: [],
                dictOptions: {}
            }
        },
        computed: {
            importExcelUrl: function() {
                return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
            }
        },
        methods: {
            loadData() {
                this.listService()
            },
            listService(type = '') {
                let that = this
                this.loading = true
                getAction(this.url.list, { type: type }).then((res) => {
                    if (res.success) {
                        that.serviceData = res.result.records
                    }
                    if (res.code === 510) {
                        that.$message.warning(res.message)
                    }
                    that.loading = false;
                    that.stateLoading = false;
                })
            },
            serviceChangeConfirm(record) {
                this.serviceStateChange(record.state, record.id);
            },
            serviceStateChange(checked, id) {
                var that = this;
                this.stateLoading = true;
                getAction(checked ? that.url.startService : that.url.stopService, { id: id }).then((res) => {
                    if (res.success) {
                        that.$message.success(res.message)
                    } else {
                        that.$message.warning(res.message)
                    }
                    this.listService();
                })
            },
            deleteService: function(id) {
                if (!this.url.delete) {
                    this.$message.error('请设置url.delete属性!')
                    return
                }
                var that = this
                deleteAction(that.url.delete, { id: id }).then((res) => {
                    if (res.success) {
                        that.$message.success(res.message)
                        that.listService()
                    } else {
                        that.$message.warning(res.message)
                    }
                })
            },
            handleDebug(record) {
                this.$refs.debugModal.debug(record);
            },
            handleConnect(record) {
                this.$refs.connectModal.connect(record);
            },
            initDictConfig() {
            }
        },
        mounted() {
            this.listService()
        }
    }
</script>

<style scoped>
    @import '~@assets/less/common.less';
</style>