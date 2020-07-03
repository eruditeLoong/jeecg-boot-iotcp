<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form @keyup.enter.native="searchQuery" layout="inline">
                <a-row :gutter="24">

                </a-row>
            </a-form>
        </div>
        <!-- 查询区域-END -->

        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
            <a-button @click="handleExportXls('数据解析协议')" icon="download" type="primary">导出</a-button>
            <a-upload :action="importExcelUrl" :headers="tokenHeader" :multiple="false" :showUploadList="false" @change="handleImportExcel" name="file">
                <a-button icon="import" type="primary">导入</a-button>
            </a-upload>
            <a-button @click="refleshCache()" icon="reload" type="">刷新缓存</a-button>
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
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm @confirm="() => handleDelete(record.id)" title="确定删除吗?">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

            </a-table>
        </div>

        <!--    <protocolData-modal ref="modalForm" @ok="modalFormOk"></protocolData-modal>-->
        <protocolData-drawer @ok="modalFormOk" ref="modalForm"></protocolData-drawer>
    </a-card>
</template>

<script>
    import { getAction } from '@/api/manage'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import ProtocolDataModal from './modules/ProtocolDataModal'
    import ProtocolDataDrawer from './modules/ProtocolDataDrawer'

    export default {
        name: 'ProtocolDataList',
        mixins: [JeecgListMixin],
        components: {
            ProtocolDataModal, ProtocolDataDrawer
        },
        data() {
            return {
                description: '数据解析协议管理页面',
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
                        title: '协议标识',
                        align: 'center',
                        dataIndex: 'code'
                    },
                    {
                        title: '协议名称',
                        align: 'center',
                        dataIndex: 'name'
                    },
                    {
                        title: '解析类型',
                        align: 'center',
                        dataIndex: 'type_dictText'
                    },
                    {
                        title: '创建日期',
                        align: 'center',
                        dataIndex: 'createTime'
                    },
                    {
                        title: '状态',
                        align: 'center',
                        dataIndex: 'state_dictText'
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                url: {
                    list: '/protocol/protocolData/list',
                    delete: '/protocol/protocolData/delete',
                    deleteBatch: '/protocol/protocolData/deleteBatch',
                    exportXlsUrl: '/protocol/protocolData/exportXls',
                    importExcelUrl: 'protocol/protocolData/importExcel',
                    refleshCache: 'protocol/protocolData/refleshCache'
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
            refleshCache() {
                let that = this;
                getAction(this.url.refleshCache, {}).then(res=>{
                    if(res.success){
                        that.$message.success('刷新缓存成功！')
                    }
                });
            },
            initDictConfig() {
            }
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less';
</style>