<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form @keyup.enter.native="searchQuery" layout="inline">
                <a-row :gutter="24">
                    <a-col :lg="7" :md="8" :sm="24" :xl="6">
                        <a-form-item label="名称">
                            <a-input placeholder="请输入名称" v-model="queryParam.name"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :lg="7" :md="8" :sm="24" :xl="6">
                        <a-form-item label="服务类型">
                            <a-input placeholder="请输入服务类型" v-model="queryParam.type"></a-input>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :lg="7" :md="8" :sm="24" :xl="6">
                            <a-form-item label="端口号">
                                <a-input placeholder="请输入端口号" v-model="queryParam.port"></a-input>
                            </a-form-item>
                        </a-col>
                    </template>
                    <a-col :lg="7" :md="8" :sm="24" :xl="6">
            <span class="table-page-search-submitButtons" style="float: left;overflow: hidden;">
              <a-button @click="searchQuery" icon="search" type="primary">查询</a-button>
              <a-button @click="searchReset" icon="reload" style="margin-left: 8px" type="primary">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
                    </a-col>

                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->

        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
            <a-button @click="handleExportXls('网络服务管理')" icon="download" type="primary">导出</a-button>
            <a-upload :action="importExcelUrl" :headers="tokenHeader" :multiple="false" :showUploadList="false" @change="handleImportExcel" name="file">
                <a-button icon="import" type="primary">导入</a-button>
            </a-upload>
            <a-dropdown v-if="selectedRowKeys.length > 0">
                <a-menu slot="overlay">
                    <a-menu-item @click="batchDel" key="1">
                        <a-icon type="delete"/>
                        删除
                    </a-menu-item>
                </a-menu>
                <a-button style="margin-left: 8px"> 批量操作
                    <a-icon type="down"/>
                </a-button>
            </a-dropdown>
        </div>

        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
                <a @click="onClearSelected" style="margin-left: 24px">清空</a>
            </div>

            <a-table
                :columns="columns"
                :dataSource="dataSource"
                :loading="loading"
                :pagination="ipagination"
                :rowSelection="{fixed:true,selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                @change="handleTableChange"
                bordered
                ref="table"
                rowKey="id"

                size="middle">

                <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span style="font-size: 12px;font-style: italic;" v-if="!text">无此图片</span>
                    <img :src="getImgView(text)" alt="图片不存在" height="25px" style="max-width:80px;font-size: 12px;font-style: italic;" v-else/>
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span style="font-size: 12px;font-style: italic;" v-if="!text">无此文件</span>
                    <a-button
                        :ghost="true"
                        @click="uploadFile(text)"
                        icon="download"
                        size="small"
                        type="primary"
                        v-else>
                        下载
                    </a-button>
                </template>

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                    <a-divider type="vertical"/>
                    <a-popconfirm @confirm="() => handleDelete(record.id)" title="确定删除吗?">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </div>

        <networkService-modal @ok="modalFormOk" ref="modalForm"></networkService-modal>
    </a-card>
</template>

<script>

    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import NetworkServiceModal from './modules/NetworkServiceModal'
    import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'

    export default {
        name: 'NetworkServiceList',
        mixins: [JeecgListMixin],
        components: {
            JDictSelectTag,
            NetworkServiceModal
        },
        data() {
            return {
                description: '网络服务管理管理页面',
                // 表头
                columns: [
                    {
                        title: '#',
                        dataIndex: '',
                        key: 'rowIndex',
                        width: 60,
                        align: 'center',
                        customRender: function(t, r, index) {
                            return parseInt(index) + 1
                        }
                    },
                    {
                        title: '名称',
                        align: 'center',
                        dataIndex: 'name'
                    },
                    {
                        title: '服务类型',
                        align: 'center',
                        dataIndex: 'type_dictText'
                    },
                    {
                        title: 'HOST',
                        align: 'center',
                        dataIndex: 'host'
                    },
                    {
                        title: '端口号',
                        align: 'center',
                        dataIndex: 'port'
                    },
                    {
                        title: '创建日期',
                        align: 'center',
                        dataIndex: 'createTime'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                url: {
                    list: '/network/networkService/list',
                    delete: '/network/networkService/delete',
                    deleteBatch: '/network/networkService/deleteBatch',
                    exportXlsUrl: '/network/networkService/exportXls',
                    importExcelUrl: 'network/networkService/importExcel'
                },
                dictOptions: {}
            }
        },
        computed: {
            importExcelUrl: function() {
                return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
            }
        },
        methods: {
            initDictConfig() {
            }
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less';
</style>