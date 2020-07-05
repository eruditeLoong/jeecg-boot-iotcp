<template>
    <j-modal
        :fullscreen="true"
        :switchFullscreen="true"
        :title="title"
        :visible.sync="visible"
        :width="1200"
        @ok="handleOk"
    >
        <a-spin :spinning="confirmLoading">
            <!-- 主表单区域 -->
            <a-form :form="form">
                <a-row>
                    <a-col :sm="8" :xs="8">
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="名称">
                            <a-input autocomplete='off' placeholder="请输入名称" v-decorator="['name', validatorRules.name]"></a-input>
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标识">
                            <a-input autocomplete='off' placeholder="请输入标识" v-decorator="['code', validatorRules.code]"></a-input>
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="3D模型文件">
                            <j-upload :trigger-change="true" v-decorator="['modelFiles', validatorRules.modelFiles]"></j-upload>
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="模型比例">
                            <a-input autocomplete='off' placeholder="请输入模型比例" v-decorator="['modelScale', validatorRules.modelScale]"></a-input>
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="软件缩放">
                            <a-input autocomplete='off' placeholder="请输入软件缩放" v-decorator="['softwareZoom', validatorRules.softwareZoom]"></a-input>
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="地理位置">
                            <a-input autocomplete='off' placeholder="请输入地理位置" v-decorator="['address', validatorRules.address]"></a-input>
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="经纬度">
                            <a-input autocomplete='off' placeholder="请输入经纬度" v-decorator="['latLon', validatorRules.latLon]"></a-input>
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属机构">
                            <j-select-depart
                                :multi="false"
                                :trigger-change="true"
                                @change="val=>model.sysOrgCode=val"
                                customReturnField="orgCode"
                                v-decorator="['sysOrgCode', validatorRules.sysOrgCode]"
                            />
                            <!--                            <j-select-depart :trigger-change="true" v-decorator="['sysOrgCode', validatorRules.sysOrgCode]"/>-->
                        </a-form-item>
                        <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="描述">
                            <a-textarea placeholder="请输入描述" rows="4" v-decorator="['description']"/>
                        </a-form-item>
                    </a-col>

                    <a-col :sm="16" :xs="16">
                        <a-card>
                            <div class="amap-page-container">
                                <el-amap-search-box :on-search-result="onSearchResult" :search-option="searchOption" class="search-box"></el-amap-search-box>
                                <el-amap :center="mapCenter" :events="events" :zoom="18" class="amap-demo" :plugin="plugin" vid="amapDemo">
                                    <el-amap-marker :position="marker" key="marker" v-for="marker in markers"></el-amap-marker>
                                </el-amap>
                            </div>
                        </a-card>
                    </a-col>

                </a-row>
            </a-form>
        </a-spin>
        <!--    </a-modal>-->
    </j-modal>
</template>

<script>

    import pick from 'lodash.pick'
    import { validateDuplicateValue } from '@/utils/util'
    import JUpload from '@/components/jeecg/JUpload'
    import JModal from '@/components/jeecg/JModal'
    import JSelectDepart from '@/components/jeecgbiz/JSelectDepart'
    import { httpAction } from '@api/manage'
    import VueAMap from 'vue-amap'
    import Vue from 'vue'

    Vue.use(VueAMap)
    VueAMap.initAMapApiLoader({
        key: '698c5b919602ad87bb7423a91f353502',
        plugin: [
            'AMap.Geocoder','AMap.MapType'
        ]
    })

    export default {
        name: 'SceneModal',
        components: {
            JUpload, JModal,
            JSelectDepart
        },
        data() {
            return {
                form: this.$form.createForm(this),
                title: '操作',
                visible: false,
                model: {},
                confirmLoading: false,
                fullscreen: true,
                switchFullscreen: true,

                mapCenter: [117.134024, 36.672127],
                markers: [],
                searchOption: {
                    city: '全国',
                    citylimit: true
                },
                plugin: [{
                    pName: 'MapType',
                    defaultType: 1,
                    events: {
                        init(instance) {
                            console.log(instance);
                        }
                    }
                }],
                events: {
                    click: e => {
                        const self = this
                        this.mapCenter = [e.lnglat.lng, e.lnglat.lat]
                        this.markers[0] = [e.lnglat.lng, e.lnglat.lat]
                        let geocoder = new AMap.Geocoder({
                            radius: 1000,
                            extensions: 'all'
                        })
                        geocoder.getAddress(this.mapCenter, function(status, result) {
                            if (status === 'complete' && result.info === 'OK') {
                                self.model.address = result.regeocode.formattedAddress
                                self.model.latLon = self.mapCenter.join(',')
                                self.$nextTick(() => {
                                    self.form.setFieldsValue(self.model, 'address', 'latLon')
                                })
                            }
                        })
                    }
                },

                labelCol: {
                    xs: { span: 24 },
                    sm: { span: 6 }
                },
                wrapperCol: {
                    xs: { span: 24 },
                    sm: { span: 16 }
                },
                validatorRules: {
                    name: {
                        rules: [
                            { required: true, message: '请输入名称!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_scene', 'name', value, this.model.id, callback) }
                        ]
                    },
                    code: {
                        rules: [
                            { required: true, message: '请输入标识!' },
                            { validator: (rule, value, callback) => validateDuplicateValue('iot_scene', 'code', value, this.model.id, callback) }
                        ]
                    },
                    modelFiles: {
                        rules: [
                            { required: true, message: '请输入3D模型文件!' }
                        ]
                    },
                    modelScale: {
                        rules: [
                            { required: true, message: '请输入模型比例!' }
                        ]
                    },
                    softwareZoom: {
                        rules: [
                            { required: true, message: '请输入软件缩放!' },
                            { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!' }
                        ]
                    },
                    address: {
                        rules: [
                            { required: true, message: '请输入地理位置!' }
                        ]
                    },
                    latLon: {
                        rules: [
                            { required: true, message: '请输入经纬度!' }
                        ]
                    },
                    sysOrgCode: {
                        rules: [
                            { required: true, message: '请输入所属机构!' }
                        ]
                    }
                },
                url: {
                    add: '/scene/scene/add',
                    edit: '/scene/scene/edit'
                }
            }
        },
        methods: {
            onSearchResult(pois) {
                let latSum = 0
                let lngSum = 0
                if (pois.length > 0) {
                    pois.forEach(poi => {
                        let { lng, lat } = poi
                        lngSum += lng
                        latSum += lat
                        this.markers.push([poi.lng, poi.lat])
                    })
                    let center = {
                        lng: lngSum / pois.length,
                        lat: latSum / pois.length
                    }
                    this.mapCenter = [center.lng, center.lat]
                }
            },
            add() {
                this.edit()
            },
            edit(record) {
                this.form.resetFields()
                this.model = Object.assign({}, record)
                this.visible = true
                console.log(this.model.latLon)
                if (this.model.latLon != undefined)
                    this.mapCenter = [this.model.latLon.split(',')[0] - 0, this.model.latLon.split(',')[1] - 0]
                this.markers[0] = this.mapCenter
                let fieldval = pick(this.model, 'name', 'code', 'modelFiles', 'modelScale', 'softwareZoom', 'address', 'latLon', 'createTime', 'sysOrgCode', 'description')
                this.$nextTick(() => {
                    this.form.setFieldsValue(fieldval)
                })
            },
            handleCancel() {
                this.mapCenter = []
                this.markers = []
                this.model = {}
                this.extendParamsConf = []
                this.form.resetFields()
                this.visible = false
            },
            handleOk() {
                alert('ok');
                const self = this
                // 触发表单验证
                this.form.validateFields((err, values) => {
                    if (!err) {
                        self.confirmLoading = true
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
                                console.log('====>', res.message)
                                self.$message.success(res.message)
                                self.$emit('ok')
                            } else {
                                self.$message.warning(res.message)
                            }
                        }).finally(() => {
                            self.confirmLoading = false
                            self.handleCancel()
                        })
                    }

                })
            }
        }
    }
</script>

<style scoped>
    .amap-demo {
        height: 800px;
    }

    .search-box {
        position: absolute;
        top: 20px;
        left: 20px;
    }

    .amap-page-container {
        position: relative;
    }
</style>
