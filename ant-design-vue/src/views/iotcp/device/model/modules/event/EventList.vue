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
        <event-drawer @ok="modalFormOk" ref="eventDrawer" :deviceModelId="deviceModelId"></event-drawer>
    </div>
</template>

<script>

    import { getAction } from '@/api/manage'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import EventDrawer from './EventDrawer'

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
            title: '名称',
            dataIndex: 'name',
            align: 'center'
        }, {
            title: '事件来源',
            dataIndex: 'source',
            align: 'center',
            customRender: function(t, r, index){
                if(t==='data'){
                    return '数据提交'
                }else{
                    return '事件上报'
                }
            }
        }, {
            title: '事件等级',
            dataIndex: 'level',
            align: 'center',
            customRender: function(t, r, index){
                if(t==1){
                    return '普通'
                }else if(t==2){
                    return '警告'
                }else{
                    return '紧急'
                }
            }
        }
    ]
    export default {
        name: 'EventList',
        mixins: [JeecgListMixin],
        components: {
            EventDrawer
        },
        props: ['deviceModelId'],
        data() {
            return {
                loading: false,
                columns: columns,
                dataSource: [],
                url: {
                    list: '/device/deviceModel/queryDeviceEventByMainId',
                    getAttributeById: '/device/deviceModel/getAttributeById',
                    deleteBatch: '/device/deviceModel/event/deleteBatch',
                }
            }
        },
        created() {
        },
        methods: {
            handleAdd() {
                this.$refs.eventDrawer.title = '添加事件定义'
                this.$refs.eventDrawer.add({
                    deviceModelBy: this.deviceModelId,
                    type: 'data',
                    level:1
                })
            },
            handleEdit() {
                let eventId = this.selectedRowKeys[0]
                this.$refs.eventDrawer.title = '编辑事件定义'
                getAction(this.url.getAttributeById, { id: eventId, aName: 'event' }).then((res) => {
                    let dmEvent = {}
                    console.log(res)
                    if (res.success) {
                        dmEvent = res.result;
                        this.$refs.eventDrawer.edit(dmEvent)
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
            }
        }
    }
</script>

<style scoped>
</style>