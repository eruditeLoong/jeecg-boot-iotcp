<template>
  <iframe style="height: 300px;width: 500px; border:1px solid darkcyan"></iframe>
</template>

<script>
import * as Three from 'three'

export default {
  name: 'Container',

  data() {
    return {
      title: '操作',

      camera: null,
      scene: null,
      renderer: null,
      mesh: null,

      url: {
        add: '/scene/scene/add',
        edit: '/scene/scene/edit'
      }
    }
  },
  methods: {
    init: function() {
      let container = this.$refs.container
      // let container = window.top.document.getElementById('container')
      console.log(container)
      this.camera = new Three.PerspectiveCamera(
        45,
        document.documentElement.clientWidth / document.documentElement.clientHeight,
        0.1,
        1000
      )
      this.camera.position.z = 6
      this.scene = new Three.Scene()
      let geometry = new Three.BoxGeometry(0.2, 0.2, 0.2)
      let material = new Three.MeshNormalMaterial()
      this.mesh = new Three.Mesh(geometry, material)
      this.scene.add(this.mesh)

      this.renderer = new Three.WebGLRenderer({ antialias: true })
      this.renderer.setClearColor(new Three.Color(0xeeeeee, 1.0))
      this.renderer.setSize(document.documentElement.clientWidth, document.documentElement.clientHeight)
      this.renderer.shadowMapEnabled = true

      container.append(this.renderer.domElement)
    },
    animate: function() {
      requestAnimationFrame(this.animate)
      this.mesh.rotation.x += 0.01
      this.mesh.rotation.y += 0.02
      this.renderer.render(this.scene, this.camera)
    },
    deploy() {
      const that = this
      this.visible = true
      this.init()
      this.animate()
      window.onresize = () => {
        // 重置渲染器输出画布canvas尺寸
        that.renderer.setSize(window.innerWidth, window.innerHeight)
        // 全屏情况下：设置观察范围长宽比aspect为窗口宽高比
        that.camera.aspect = window.innerWidth / window.innerHeight
        // 渲染器执行render方法的时候会读取相机对象的投影矩阵属性projectionMatrix
        // 但是不会每渲染一帧，就通过相机的属性计算投影矩阵(节约计算资源)
        // 如果相机的一些属性发生了变化，需要执行updateProjectionMatrix ()方法更新相机的投影矩阵
        that.camera.updateProjectionMatrix()
      }
    },
    handleCancel() {
      this.visible = false
    },
    handleOk() {
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
          httpAction(httpurl, formData, method)
            .then(res => {
              if (res.success) {
                console.log('====>', res.message)
                self.$message.success(res.message)
                self.$emit('ok')
              } else {
                self.$message.warning(res.message)
              }
            })
            .finally(() => {
              self.confirmLoading = false
              self.handleCancel()
            })
        }
      })
    }
  },
  mounted() {
    this.deploy()
  }
}
</script>

<style scoped></style>
