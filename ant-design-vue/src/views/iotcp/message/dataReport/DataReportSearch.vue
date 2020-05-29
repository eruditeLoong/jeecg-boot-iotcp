<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form @keyup.enter.native="searchQuery" layout="inline">
                <a-row :gutter="24">
                    <a-col :lg="7" :md="8" :sm="24" :xl="6">
                        <a-form-item label="实例设备">
                            <j-search-select-tag dict="iot_device_instance,name,id" :disabled="true" placeholder="请选择实例设备" v-model="queryParam.instanceDeviceBy"/>
                        </a-form-item>
                    </a-col>
                    <template v-if="toggleSearchStatus">
                        <a-col :lg="7" :md="8" :sm="24" :xl="6">
                            <a-form-item label="创建日期">
                                <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择创建日期" v-model="queryParam.createTime"></j-date>
                            </a-form-item>
                        </a-col>
                        <a-col :lg="7" :md="8" :sm="24" :xl="6">
                            <a-form-item label="录入方式">
                                <j-dict-select-tag dictCode="dm_type" placeholder="请选择录入方式" v-model="queryParam.inputMode"/>
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
            <a-button @click="handleExportXls('设备上传数据')" icon="download" type="primary">导出</a-button>
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
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                @change="handleTableChange"
                bordered
                class="j-table-force-nowrap"
                ref="table"
                rowKey="id"
                size="middle">

                <template slot="htmlSlot" slot-scope="text">
                    <div v-html="text"></div>
                </template>
                <template slot="imgSlot" slot-scope="text">
                    <span style="font-size: 12px;font-style: italic;" v-if="!text">无图片</span>
                    <img :src="getImgView(text)" alt="" height="25px" style="max-width:80px;font-size: 12px;font-style: italic;" v-else/>
                </template>
                <template slot="fileSlot" slot-scope="text">
                    <span style="font-size: 12px;font-style: italic;" v-if="!text">无文件</span>
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

        <dataReport-modal @ok="modalFormOk" ref="modalForm"></dataReport-modal>
    </a-card>
</template>

<script>

    import '@/assets/less/TableExpand.less'
    import { mixinDevice } from '@/utils/mixin'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import DataReportModal from './modules/DataReportModal'
    import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
    import JDate from '@/components/jeecg/JDate.vue'
    import JSearchSelectTag from '@/components/dict/JSearchSelectTag'

    export default {
        name: 'DataReportList',
        mixins: [JeecgListMixin, mixinDevice],
        components: {
            JDictSelectTag,
            JDate,
            JSearchSelectTag,
            DataReportModal
        },
        props: {
            deviceInstanceId: {
                type: String,
                default: ""
            }
        },
        data() {
            return {
                description: '设备上传数据管理页面',
                queryParam:{instanceDeviceBy: this.deviceInstanceId},
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
                        title: '实例设备',
                        align: 'center',
                        dataIndex: 'instanceDeviceBy_dictText'
                    },
                    {
                        title: '上报数据',
                        align: 'center',
                        dataIndex: 'data'
                    },
                    {
                        title: '创建日期',
                        align: 'center',
                        dataIndex: 'createTime'
                    },
                    {
                        title: '录入方式',
                        align: 'center',
                        dataIndex: 'inputMode_dictText'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        // fixed:"right",
                        width: 147,
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                url: {
                    list: '/message/data/list',
                    delete: '/message/data/delete',
                    deleteBatch: '/message/data/deleteBatch',
                    exportXlsUrl: '/message/data/exportXls',
                    importExcelUrl: 'message/data/importExcel'
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