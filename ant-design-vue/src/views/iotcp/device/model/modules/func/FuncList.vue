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
        <func-drawer @ok="modalFormOk" ref="funcDrawer" :deviceModelId="deviceModelId"></func-drawer>
    </div>
</template>

<script>

    import { getAction } from '@/api/manage'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import FuncDrawer from './FuncDrawer'

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
        }, {
            title: '标识',
            dataIndex: 'code',
            align: 'center'
        }, {
            title: '功能',
            dataIndex: 'name',
            align: 'center'
        }, {
            title: '操作类型',
            dataIndex: 'type',
            align: 'center',
            customRender: function(t, r, index){
                if(t==='front'){
                    return '前端操作'
                }else{
                    return '后台请求'
                }
            }
        }, {
            title: '是否同步',
            dataIndex: 'isSync',
            align: 'center',
            customRender: function(t, r, index){
                if(t){
                    return '同步'
                }else{
                    return '异步'
                }
            }
        }
    ]
    export default {
        name: 'FuncList',
        mixins: [JeecgListMixin],
        components: {
            FuncDrawer
        },
        props: ['deviceModelId'],
        data() {
            return {
                loading: false,
                columns: columns,
                dataSource: [],
                url: {
                    list: '/device/deviceModel/queryDeviceFuncByMainId',
                    getAttributeById: '/device/deviceModel/getAttributeById',
                    deleteBatch: '/device/deviceModel/func/deleteBatch',
                }
            }
        },
        created() {
        },
        methods: {
            handleAdd() {
                this.$refs.funcDrawer.title = '添加功能定义'
                this.$refs.funcDrawer.add({
                    deviceModelBy: this.deviceModelId,
                    type: 'front',
                    isSync: false,
                    inputParams: [{}],
                    backData: ''
                })
            },
            handleEdit() {
                let funcId = this.selectedRowKeys[0]
                this.$refs.funcDrawer.title = '编辑功能定义'
                getAction(this.url.getAttributeById, { id: funcId, aName: 'func' }).then((res) => {
                    let dmFunc = {}
                    console.log(res)
                    if (res.success) {
                        dmFunc = res.result
                        dmFunc.inputParams = JSON.parse(dmFunc.inputParams || '[]')
                        let outputData = JSON.parse(dmFunc.outputData || '{}')
                        for (let okey in outputData) {
                            dmFunc[okey] = outputData[okey]
                        }
                        this.$refs.funcDrawer.edit(dmFunc)
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