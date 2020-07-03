<template>
    <a-card size="small">
        <template slot="title">
            <span style="font-family: 'Kaiti SC';font-weight: bold; font-size: 18px">{{record.name}} </span>
            系统方案
        </template>
        <a-button slot="extra" @click="handleAdd(record)" icon="plus" type="primary">新增</a-button>

        <a-table
            :columns="columns"
            :dataSource="dataSource"
            :loading="loading"
            :pagination="false"
            bordered
            rowKey="id"
            size="small"
        >
            <template slot="status" slot-scope="text, record">
                <li v-show="text==1">
                    <a-icon style="color:green;" theme="filled" title="在线" type="check-circle"/>
                    有效
                </li>
                <li v-show="text==0">
                    <a-icon style="color:gray" theme="filled" title="未激活" type="close-circle"/>
                    无效
                </li>
            </template>

            <template slot="action" slot-scope="text, record">
                <a @click="handleEdit(record)">编辑</a>
                <a-divider type="vertical"/>
                <a @click="handleDevice(record)">设备</a>
                <a-divider type="vertical"/>
                <a-popconfirm @confirm="handleDelete(record.id)" title="确定删除吗?">
                    <a>删除</a>
                </a-popconfirm>

            </template>

        </a-table>

        <SceneSchemeDrawer @ok="loadScheme" ref="sceneSchemeDrawer"/>

        <SchemeDeviceDrawer ref="schemeDeviceDrawer"/>
    </a-card>

</template>

<script>
    import { getAction } from '@api/manage'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import SceneSchemeDrawer from '../modules/SceneSchemeDrawer'
    import SchemeDeviceDrawer from '../modules/SchemeDeviceDrawer'

    export default {
        name: 'SceneSchemeSubTable',
        mixins: [JeecgListMixin],
        components: {
            SceneSchemeDrawer,
            SchemeDeviceDrawer
        },
        props: {
            record: {
                type: Object,
                default: null
            }
        },
        data() {
            return {
                description: '场景方案内嵌列表',
                disableMixinCreated: true,
                loading: false,
                dataSource: [],
                columns: [
                    {
                        title: '序号',
                        key: 'rowIndex',
                        width: 60,
                        align: 'center',
                        customRender: (t, r, index) => parseInt(index) + 1
                    },
                    {
                        title: '方案标识',
                        align: 'center',
                        dataIndex: 'id'
                    },
                    {
                        title: '方案名称',
                        align: 'center',
                        dataIndex: 'name'
                    },
                    {
                        title: '方案状态',
                        align: 'center',
                        dataIndex: 'status',
                        scopedSlots: { customRender: 'status' }
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        align: 'center',
                        scopedSlots: { customRender: 'action' }
                    }
                ],
                url: {
                    list: '/scene/sceneScheme/listBySceneId',
                    delete: '/scene/sceneScheme/delete',
                    add: '/scene/sceneScheme/add',
                    edit: '/scene/sceneScheme/edit'
                }
            }
        },
        watch: {
            record: {
                immediate: true,
                handler() {
                    if (this.record != null) {
                        this.loadScheme()
                    }
                }
            }
        },
        methods: {

            loadScheme() {
                this.loading = true
                this.dataSource = []
                getAction(this.url.list, {
                    sceneId: this.record.id
                }).then((res) => {
                    if (res.success) {
                        this.dataSource = res.result
                    }
                }).finally(() => {
                    this.loading = false
                })
            },

            handleAdd(scene){
                this.$refs.sceneSchemeDrawer.title = '新增方案：' + scene.name
                this.$refs.sceneSchemeDrawer.add(scene.id);
            },

            handleEdit(scene) {
                this.$refs.sceneSchemeDrawer.title = '修改方案'
                this.$refs.sceneSchemeDrawer.edit(scene);
            },

            handleDevice(record) {
                this.$refs.schemeDeviceDrawer.deviceTree(record);
            }

        }
    }
</script>

<style scoped>

</style>
