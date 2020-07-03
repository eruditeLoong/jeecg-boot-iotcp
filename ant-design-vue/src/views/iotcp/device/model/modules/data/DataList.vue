<template>
    <div style="margin-top:10px">
        <!-- 操作按钮区域 -->
        <div :md="24" :sm="24" class="table-operator" style="margin: -25px 0px 10px 0px">
            <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>
            <a-button @click="handleEdit" icon="edit" type="" v-if="selectedRowKeys.length == 1">编辑</a-button>

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
                :pagination="false"
                :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
                @change="handleTableChange"
                bordered
                ref="table"
                rowKey="id"
                size="middle">

            </a-table>
        </div>
        <!-- table区域-end -->
        <data-drawer @ok="modalFormOk" ref="dataDrawer"></data-drawer>
    </div>
</template>

<script>

    import { getAction } from '@/api/manage'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import DataDrawer from './DataDrawer'

    const columns = [
        {
            title: '#',
            dataIndex: '',
            key: 'rowIndex',
            width: 40,
            align: 'center',
            customRender: function(t, r, index) {
                return parseInt(index) + 1
            }
        },
        {
            title: '标识',
            dataIndex: 'code',
            align: 'center'
        },
        {
            title: '名称',
            dataIndex: 'name',
            align: 'center'
        },
        {
            title: '数据类型',
            dataIndex: 'valueType',
            align: 'center',
            customRender: function(val) {
                let type = JSON.parse(val)['type'];
                return type
            }
        },
        {
            title: '读写权限',
            dataIndex: 'rwAuthor',
            align: 'center',
            customRender: function(t, r, index) {
                if (t == 'r') return '只读'
                else if (t == 'w') return '只写'
                else return '可读写'
            }
        }
    ]
    export default {
        name: 'DataList',
        mixins: [JeecgListMixin],
        components: {
            DataDrawer
        },
        props: ['deviceModelId'],
        data() {
            return {
                loading: false,
                columns: columns,
                dataSource: [],
                url: {
                    list: '/device/deviceModel/queryDeviceDataByMainId',
                    getAttributeById: '/device/deviceModel/getAttributeById',
                    deleteBatch: '/device/deviceModel/data/deleteBatch'
                }
            }
        },
        created() {
        },
        methods: {
            handleAdd() {
                this.$refs.dataDrawer.title = '添加数据节点'
                this.$refs.dataDrawer.add({
                    deviceModelBy: this.deviceModelId,
                    valueType: { type: undefined }
                })
            },
            handleEdit() {
                let dataId = this.selectedRowKeys[0]
                this.$refs.dataDrawer.title = '编辑数据节点'
                getAction(this.url.getAttributeById, { id: dataId, aName: 'data' }).then((res) => {
                    console.log(res)
                    if (res.success) {
                        let dmData = res.result
                        dmData.valueType = JSON.parse(res.result.valueType || { type: undefined })
                        this.$refs.dataDrawer.edit(dmData)
                    }
                })
            },
            loadData() {
                let that = this
                getAction(this.url.list, { id: this.deviceModelId }).then((res) => {
                    if (res.success) {
                        that.dataSource = res.result || []
                    } else {
                        that.dataSource = []
                    }
                })
            },
            getOrderMain(orderId) {
                this.deviceModelId = orderId
                this.loadData()
            }
        }
    }
</script>

<style scoped>
</style>