<template>
    <a-card :bordered="false" class="card-area">

        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <!-- 搜索区域 -->
            <a-form @keyup.enter.native="searchQuery" layout="inline">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}" label="名称">
                            <a-input placeholder="请输入名称查询" v-model="queryParam.roleName"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :md="10" :sm="12">
                        <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}" label="创建时间">
                            <j-date :showTime="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" style="width:45%" v-model="queryParam.createTime_begin"></j-date>
                            <span style="width: 10px;">~</span>
                            <j-date :showTime="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" style="width:45%" v-model="queryParam.createTime_end"></j-date>
                        </a-form-item>
                    </a-col>
                    <span class="table-page-search-submitButtons" style="float: left;overflow: hidden;">
            <a-col :md="6" :sm="24">
              <a-button @click="searchQuery" type="primary">查询</a-button>
              <a-button @click="searchReset" style="margin-left: 8px">重置</a-button>
            </a-col>
          </span>
                </a-row>
            </a-form>
        </div>

        <!-- 操作按钮区域 -->
        <div class="table-operator" style="margin-top: 5px">
            <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
            <a-button @click="handleExportXls('角色信息')" icon="download" type="primary">导出</a-button>
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
                <a-button style="margin-left: 8px">
                    批量操作
                    <a-icon type="down"/>
                </a-button>
            </a-dropdown>
        </div>

        <!-- table区域-begin -->
        <div>
            <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
                <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择&nbsp;<a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项&nbsp;&nbsp;
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
                ref="table"
                rowKey="id"
                size="middle">

                <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                    <a-divider type="vertical"/>
                    <a @click="handlePerssion(record.id)">授权</a>
                    <a-divider type="vertical"/>
                    <a-popconfirm @confirm="() => handleDelete(record.id)" title="确定删除吗?">
                        <a>删除</a>
                    </a-popconfirm>
                </span>
            </a-table>
        </div>
        <!-- table区域-end -->

        <!-- 表单区域 -->
        <role-modal @ok="modalFormOk" ref="modalForm"></role-modal>
        <user-role-modal ref="modalUserRole"></user-role-modal>
    </a-card>
</template>

<script>
    import RoleModal from './modules/RoleModal'
    import UserRoleModal from './modules/UserRoleModal'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import JDate from '@/components/jeecg/JDate'

    export default {
        name: 'RoleList',
        mixins: [JeecgListMixin],
        components: {
            RoleModal,
            UserRoleModal,
            JDate
        },
        data() {
            return {

                description: '角色管理页面',
                // 查询条件
                queryParam: { roleName: '' },
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
                        title: '角色名称',
                        align: 'center',
                        dataIndex: 'roleName'
                    },
                    {
                        title: '角色编码',
                        align: 'center',
                        dataIndex: 'roleCode'
                    },
                    {
                        title: '备注',
                        align: 'center',
                        dataIndex: 'description'
                    },
                    {
                        title: '创建时间',
                        dataIndex: 'createTime',
                        align: 'center',
                        sorter: true
                    },
                    {
                        title: '更新时间',
                        dataIndex: 'updateTime',
                        align: 'center',
                        sorter: true
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                url: {
                    list: '/sys/role/list',
                    delete: '/sys/role/delete',
                    deleteBatch: '/sys/role/deleteBatch',
                    exportXlsUrl: '/sys/role/exportXls',
                    importExcelUrl: 'sys/role/importExcel'
                }
            }
        },
        computed: {
            importExcelUrl: function() {
                return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
            }
        },
        methods: {
            handlePerssion: function(roleId) {
                // alert(roleId);
                this.$refs.modalUserRole.show(roleId)
            },
            onChangeDate(date, dateString) {
                console.log(date, dateString)
            }
        }
    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>