<template>
    <a-modal
        :confirmLoading="confirmLoading"
        :title="title"
        :visible="visible"
        :width="width"
        @cancel="handleCancel"
        @ok="handleOk"
        cancelText="关闭">
        <a-spin :spinning="confirmLoading">
            <div>
                <a-transfer
                    :data-source="mockData"
                    :target-keys="targetKeys"
                    :filter-option="(inputValue, item) => item.name.indexOf(inputValue) !== -1"
                    :show-search="showSearch"
                    :show-select-all="false"
                    @change="onChange"
                >
                    <template
                        slot="children"
                        slot-scope="{
                            props: { direction, filteredItems, selectedKeys, disabled: listDisabled },
                            on: { itemSelectAll, itemSelect },
                        }"
                    >
                        <a-table
                            :columns="[
                                {
                                    dataIndex: 'name',
                                    title: '名称',
                                },
                                {
                                    dataIndex: 'addr',
                                    title: '地址',
                                },
                            ]"
                            :custom-row="
                                ({ key, disabled: itemDisabled }) => ({
                                  on: {
                                    click: () => {
                                      if (itemDisabled || listDisabled) return;
                                      itemSelect(key, !selectedKeys.includes(key));
                                    },
                                  },
                                })
                              "
                            :data-source="filteredItems"
                            :row-selection="
                                getRowSelection({ disabled: listDisabled, selectedKeys, itemSelectAll, itemSelect })
                            "
                            size="small"
                        />
                    </template>
                </a-transfer>
            </div>
        </a-spin>

    </a-modal>
</template>

<script>
    import { httpAction, getAction } from '@/api/manage'

    const mockData = [];
    for (let i = 0; i < 20; i++) {
        mockData.push({
            key: i.toString(),
            name: `content${i + 1}`,
            addr: `description of content${i + 1}`,
        });
    }

    const originTargetKeys = mockData.filter(item => +item.key % 3 > 1).map(item => item.key);

    export default {
        name: 'configConnectModal',

        data() {
            return {
                mockData,
                targetKeys: originTargetKeys,
                showSearch: true,
                title: '',
                width: 800,
                visible: false,
                confirmLoading: false,
                gatewayDevicList:[],
                allConnectList: [],
                selectedConnectList:[],
                model: {},
                url: {
                    listGatewayDevice: '/device/deviceInstance/listGatewayDevice',
                    add: '/device/deviceInstance/add',
                    edit: '/device/deviceInstance/edit'
                }
            }
        },

        methods: {
            config(id) {
                this.visible = true
                this.title = '链接信息'
                this.getGatewayDevice()
            },
            close() {
                this.$emit('close')
                this.visible = false
            },
            getGatewayDevice(){
                let that = this
                getAction(this.url.listGatewayDevice, {}).then(res => {
                    console.log(res);
                    if (res.success) {
                        that.gatewayDevicList = res.result
                    }
                })
            },
            handleOk() {
                const that = this
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        that.confirmLoading = true
                        let httpurl = ''
                        let method = ''
                        if (!this.model.id) {
                            httpurl += this.url.add
                            method = 'post'
                        } else {
                            httpurl += this.url.edit
                            method = 'put'
                        }
                        let formData = Object.assign(this.model, values)
                        console.log('表单提交数据', formData)
                        httpAction(httpurl, formData, method).then((res) => {
                            if (res.success) {
                                that.$message.success(res.message)
                                that.$emit('ok')
                            } else {
                                that.$message.warning(res.message)
                            }
                        }).finally(() => {
                            that.confirmLoading = false
                            that.close()
                        })
                    }

                })
            },
            handleCancel() {
                this.close()
            },

            onChange(nextTargetKeys) {
                this.targetKeys = nextTargetKeys;
            },

            triggerDisable(disabled) {
                this.disabled = disabled;
            },

            triggerShowSearch(showSearch) {
                this.showSearch = showSearch;
            },
            getRowSelection({ disabled, selectedKeys, itemSelectAll, itemSelect }) {
                return {
                    getCheckboxProps: item => ({ props: { disabled: disabled || item.disabled } }),
                    onSelectAll(selected, selectedRows) {
                        const treeSelectedKeys = selectedRows
                        .filter(item => !item.disabled)
                        .map(({ key }) => key);
                        const diffKeys = selected
                            ? difference(treeSelectedKeys, selectedKeys)
                            : difference(selectedKeys, treeSelectedKeys);
                        itemSelectAll(diffKeys, selected);
                    },
                    onSelect({ key }, selected) {
                        itemSelect(key, selected);
                    },
                    selectedRowKeys: selectedKeys,
                };
            },
        }
    }
</script>

<style scoped>

</style>