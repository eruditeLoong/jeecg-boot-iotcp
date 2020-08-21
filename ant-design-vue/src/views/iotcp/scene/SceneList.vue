<template>
  <a-card :bordered="false" class="j-inner-table-wrapper">
    <!-- 查询区域 begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :lg="7" :md="8" :sm="24" :xl="6">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.name" />
            </a-form-item>
          </a-col>
          <a-col :lg="7" :md="8" :sm="24" :xl="6">
            <a-form-item label="所属机构">
              <a-input placeholder="请输入所属机构" v-model="queryParam.sysOrgCode" />
            </a-form-item>
          </a-col>
          <a-col :lg="7" :md="8" :sm="24" :xl="6">
            <span class="table-page-search-submitButtons table-operator">
              <a-button @click="searchQuery" icon="search" type="primary">查询</a-button>
              <a-button @click="searchReset" icon="reload" type="primary">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                <span>{{ toggleSearchStatus ? '收起' : '展开' }}</span>
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域 end -->

    <!-- 操作按钮区域 begin -->
    <div class="table-operator">
      <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
      <a-button @click="handleExportXls('场景管理')" icon="download" type="primary">导出</a-button>
      <a-upload
        :action="importExcelUrl"
        :headers="tokenHeader"
        :multiple="false"
        :showUploadList="false"
        @change="handleImportExcel"
        name="file"
      >
        <a-button icon="import" type="primary">导入</a-button>
      </a-upload>

      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item @click="batchDel" key="1">
            <a-icon type="delete" />
            <span>删除</span>
          </a-menu-item>
        </a-menu>
        <a-button>
          <span>批量操作</span>
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
    </div>
    <!-- 操作按钮区域 end -->

    <!-- table区域 begin -->
    <div>
      <a-alert showIcon style="margin-bottom: 16px;" type="info">
        <template slot="message">
          <span>已选择</span>
          <a style="font-weight: 600;padding: 0 4px;">{{ selectedRowKeys.length }}</a>
          <span>项</span>
          <a @click="onClearSelected" style="margin-left: 24px">清空</a>
        </template>
      </a-alert>

      <a-table
        :columns="columns"
        :dataSource="dataSource"
        :expandedRowKeys="expandedRowKeys"
        :loading="loading"
        :pagination="ipagination"
        :rowSelection="{ selectedRowKeys, onChange: onSelectChange }"
        :expandRowByClick="true"
        @change="handleTableChange"
        @expand="handleExpand"
        bordered
        class="j-table-force-nowrap"
        ref="table"
        rowKey="id"
        size="middle"
      >
        <!-- 内嵌table区域 begin -->
        <template slot="expandedRowRender" slot-scope="record">
          <scene-scheme-sub-table :record="record" />
        </template>
        <!-- 内嵌table区域 end -->

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>

        <template slot="imgSlot" slot-scope="text">
          <div style="font-size: 12px;font-style: italic;">
            <span v-if="!text">无图片</span>
            <img :src="getImgView(text)" alt style="max-width:80px;height:25px;" v-else />
          </div>
        </template>

        <template slot="fileSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无文件</span>
          <a-button
            @click="uploadFile(text)"
            ghost
            icon="download"
            size="small"
            type="primary"
            v-else>
            <span>下载</span>
          </a-button>
        </template>

        <template slot="action" slot-scope="text, record">
          <a @click.stop="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click.stop="handleDeploy(record)">部署</a>
          <a-divider type="vertical" />
          <a-popconfirm @confirm="handleDelete(record.id)" title="确定删除吗?">
            <a>删除</a>
          </a-popconfirm>
        </template>
      </a-table>
    </div>
    <!-- table区域 end -->

    <!-- 表单区域 -->
    <scene-modal @ok="modalFormOk" ref="modalForm" />
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SceneModal from './modules/SceneModal'
import SceneSchemeSubTable from './subTables/SceneSchemeSubTable'
import '@/assets/less/TableExpand.less'

export default {
    name: 'SceneList',
    mixins: [JeecgListMixin],
    components: {
        SceneModal,
        SceneSchemeSubTable
    },
    data () {
        return {
            description: '场景管理列表管理页面',
            // 表头
            columns: [
                {
                    title: '序号',
                    key: 'rowIndex',
                    width: 60,
                    align: 'center',
                    customRender: (t, r, index) => parseInt(index) + 1
                },
                {
                    title: '主键',
                    align: 'center',
                    dataIndex: 'id'
                },
                {
                    title: '名称',
                    align: 'center',
                    dataIndex: 'name'
                },
                {
                    title: '标识',
                    align: 'center',
                    dataIndex: 'code'
                },
                {
                    title: '模型比例',
                    align: 'center',
                    dataIndex: 'modelScale'
                },
                {
                    title: '创建日期',
                    align: 'center',
                    dataIndex: 'createTime'
                },
                {
                    title: '所属机构',
                    align: 'center',
                    dataIndex: 'sysOrgCode_dictText'
                },
                {
                    title: '操作',
                    dataIndex: 'action',
                    align: 'center',
                    scopedSlots: { customRender: 'action' }
                }
            ],
            // 字典选项
            dictOptions: {},
            // 展开的行
            expandedRowKeys: [],
            url: {
                list: '/scene/manage/list',
                countScheme: '/scene/manage/countScheme',
                delete: '/scene/manage/delete',
                deleteBatch: '/scene/manage/deleteBatch',
                exportXlsUrl: '/scene/manage/exportXls',
                importExcelUrl: '/scene/manage/importExcel'
            }
        }
    },
    computed: {
        importExcelUrl () {
            return window._CONFIG['domianURL'] + this.url.importExcelUrl
        }
    },
  /*  mounted() {
        alert("mounted");
    },*/
    methods: {
        handleDeploy (scene) {
            this.$refs.sceneDeploy.deploy(scene)
        },

        initDictConfig () {},

        handleExpand (expanded, record) {
            this.expandedRowKeys = []
            if (expanded === true) {
                this.expandedRowKeys.push(record.id)
            }
        }
    }
}
</script>
<style lang="less" scoped>
@import '~@assets/less/common.less';
</style>
