<template>
    <div>
        <v-chart :data="chartData" :forceFit="true" :height="300" :scale="scale">
            <v-coord :endAngle="22.5" :radius="0.75" :startAngle="-202.5" type="polar"></v-coord>
            <v-axis
                :grid="null"
                :label="axisLabel"
                :line="null"
                :subTickCount="4"
                :subTickLine="axisSubTickLine"
                :tickLine="axisTickLine"
                :zIndex="2"
                dataKey="value"
            ></v-axis>
            <v-axis :show="false" dataKey="1"></v-axis>
            <v-series
                :active="false"
                color="#1890FF"
                gemo="point"
                position="value*1"
                shape="pointer"
            ></v-series>
            <v-guide
                :end="arcGuide1End"
                :start="arcGuide1Start"
                :top="false"
                :vStyle="arcGuide1Style"
                :zIndex="0"
                type="arc"
            ></v-guide>
            <v-guide
                :end="getArcGuide2End"
                :start="arcGuide2Start"
                :vStyle="arcGuide2Style"
                :zIndex="1"
                type="arc"
            ></v-guide>
            <v-guide
                :html="getHtmlGuideHtml()"
                :position="htmlGuidePosition"
                type="html"
            ></v-guide>
        </v-chart>
    </div>
</template>

<script>
    import { registerShape } from 'viser-vue'

    registerShape('point', 'pointer', {
        draw(cfg, container) {
            let point = cfg.points[0]
            point = this.parsePoint(point)
            const center = this.parsePoint({
                x: 0,
                y: 0
            })
            container.addShape('line', {
                attrs: {
                    x1: center.x,
                    y1: center.y,
                    x2: point.x,
                    y2: point.y + 15,
                    stroke: cfg.color,
                    lineWidth: 5,
                    lineCap: 'round'
                }
            })
            return container.addShape('circle', {
                attrs: {
                    x: center.x,
                    y: center.y,
                    r: 9.75,
                    stroke: cfg.color,
                    lineWidth: 4.5,
                    fill: '#fff'
                }
            })
        }
    })

    const scale = [{
        dataKey: 'value',
        min: 0,
        max: 9,
        tickInterval: 1,
        nice: false
    }]

    const data = [
        { value: 7.0 }
    ]

    export default {
        name: 'DashChartDemo',
        props: {
            datasource: {
                type: Number,
                default: 7
            },
            title: {
                type: String,
                default: ''
            }
        },
        created() {
            if (!this.datasource) {
                this.chartData = data
            } else {
                this.chartData = [
                    { value: this.datasource }
                ]
            }
            this.getChartData()
        },
        watch: {
            'datasource': function(val) {
                this.chartData = [
                    { value: val }
                ]
                this.getChartData()
            }
        },
        methods: {
            getChartData() {
                if (this.chartData && this.chartData.length > 0) {
                    this.abcd = this.chartData[0].value * 10
                } else {
                    this.abcd = 70
                }
            },
            getHtmlGuideHtml() {
                return '<div style="width: 300px;text-align: center;">\n' +
                    '<p style="font-size: 14px;color: #545454;margin: 0;">' + this.title + '</p>\n' +
                    '<p style="font-size: 36px;color: #545454;margin: 0;">' + this.abcd + '%</p>\n' +
                    '</div>'
            },
            getArcGuide2End() {
                return [this.chartData[0].value, 0.945]
            }
        },
        data() {
            return {
                chartData: [],
                height: 400,
                scale: scale,
                abcd: 70,
                axisLabel: {
                    offset: -16,
                    textStyle: {
                        fontSize: 14,
                        textAlign: 'center',
                        textBaseline: 'middle'
                    }
                },
                axisSubTickLine: {
                    length: -8,
                    stroke: '#fff',
                    strokeOpacity: 1
                },
                axisTickLine: {
                    length: -17,
                    stroke: '#fff',
                    strokeOpacity: 1
                },
                arcGuide1Start: [0, 0.945],
                arcGuide1End: [9, 0.945],
                arcGuide1Style: {
                    stroke: '#CBCBCB',
                    lineWidth: 18
                },
                arcGuide2Start: [0, 0.945],
                arcGuide2Style: {
                    stroke: '#1890FF',
                    lineWidth: 18
                },
                htmlGuidePosition: ['50%', '100%'],
                htmlGuideHtml: `
        <div style="width: 300px;text-align: center;">
          <p style="font-size: 14px;color: #545454;margin: 0;">${this.title}</p>
          <p style="font-size: 36px;color: #545454;margin: 0;">${this.abcd}%</p>
        </div>
      `
            }
        }
    }
</script>
