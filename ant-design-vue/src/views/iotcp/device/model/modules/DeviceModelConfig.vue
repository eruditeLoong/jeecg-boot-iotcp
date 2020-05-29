<template>
    <a-drawer
        :closable="true"
        :title="title"
        :visible="visible"
        :width="width"
        @close="close"
        placement="right"
    >
        <a-card class="detail-card" hoverable title="基本信息">
            <a-row class="row-bottom-line">
                <a-col :span="10"><p>编码</p></a-col>
                <a-col :span="14">{{model.name}}</a-col>
            </a-row>
            <a-row class="row-bottom-line">
                <a-col :span="10"><p>标识</p></a-col>
                <a-col :span="14">{{model.code}}</a-col>
            </a-row>
            <a-row class="row-bottom-line">
                <a-col :span="10"><p>类型</p></a-col>
                <a-col :span="14">{{model.type}}</a-col>
            </a-row>
            <a-row class="row-bottom-line">
                <a-col :span="10"><p>模型文件</p></a-col>
                <a-col :span="14">{{model.threeModelFile}}</a-col>
            </a-row>
            <a-row class="row-bottom-line">
                <a-col :span="10"><p>传输协议</p></a-col>
                <a-col :span="14">{{model.linkProtocolBy}}</a-col>
            </a-row>
            <a-row class="row-bottom-line">
                <a-col :span="10"><p>数据协议</p></a-col>
                <a-col :span="14">{{model.dataProtocolBy}}</a-col>
            </a-row>
            <a-row class="row-bottom-line">
                <a-col :span="10"><p>描述</p></a-col>
                <a-col :span="14">{{model.description}}</a-col>
            </a-row>
        </a-card>
        <a-card class="detail-card" hoverable title="配置属性">
            <!-- 子表单区域 -->
            <a-tabs @change="handleChangeTabs" v-model="activeKey">
                <a-tab-pane :forceRender="true" :key="refKeys[0]" tab="数据节点">
                    <DataList :deviceModelId="model.id" ref="deviceData"></DataList>
                </a-tab-pane>

                <a-tab-pane :forceRender="true" :key="refKeys[1]" tab="功能定义">
                    <FuncList :deviceModelId="model.id" ref="deviceFunc"></FuncList>
                </a-tab-pane>

                <a-tab-pane :forceRender="true" :key="refKeys[2]" tab="事件定义">
                    <EventList :deviceModelId="model.id" ref="deviceEvent"></EventList>
                </a-tab-pane>

                <a-tab-pane :forceRender="true" :key="refKeys[3]" tab="数据标签">
                </a-tab-pane>

            </a-tabs>

        </a-card>

        <div :style="{
          position: 'absolute',
          right: 0,
          bottom: 0,
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'right',
          zIndex: 1,
        }"
        >
            <a-button @click="close" type="">关闭</a-button>
        </div>
    </a-drawer>
</template>

<script>
    import STable from '@/components/table/'
    import DataList from './data/DataList'
    import FuncList from './func/FuncList'
    import pick from 'lodash.pick'
    import EventList from './event/EventList'

    export default {
        name: 'DeviceModelConfig',
        // mixins: [JeecgListMixin],
        components: {
            EventList,
            STable,
            DataList,
            FuncList
        },
        data() {
            return {
                title: '组件信息',
                width: 700,
                visible: false,
                model: {},
                refKeys: ['deviceData', 'deviceFunc', 'deviceEvent', 'deviceLabel'],
                tableKeys: ['deviceData', 'deviceFunc', 'deviceEvent', 'deviceLabel'],
                activeKey: 'deviceData',
            }
        },
        created() {
            this.resetScreenSize();
        },
        methods: {
            config(record) {
                this.visible = true;
                this.model = record;
                if(this.$refs.deviceData != undefined){
                    this.$refs.deviceData.loadData();
                }
            },
            close() {
                this.visible = false;
            },
            handleChangeTabs(key){
                this.$refs[key].loadData();
            },
            // 根据屏幕变化,设置抽屉尺寸
            resetScreenSize() {
                let screenWidth = document.body.clientWidth
                if (screenWidth < 500) {
                    this.width = screenWidth
                } else {
                    this.width = 650
                }
            },
        }
    }
</script>

<style lang="less" scoped>

    .detail-card {
        margin-bottom: 20px;

        .row-bottom-line {
            border-bottom: 1px #f0f0f0 solid;

            div {
                height: 36px;
                line-height: 36px;
            }
        }
    }

</style>