<template>
    <div class="main">
        <a-form :form="form" class="user-layout-login" id="formLogin" ref="formLogin">
            <a-tabs
                    :activeKey="customActiveKey"
                    :tabBarStyle="{ textAlign: 'center', borderBottom: 'unset' }"
                    @change="handleTabClick">
                <a-tab-pane key="tab1" tab="账号密码登陆">
                    <a-form-item>
                        <a-input
                                autocomplete='off'
                                placeholder="请输入帐户名"
                                size="large"
                                type="text"
                                v-decorator="['username',validatorRules.username,{ validator: this.handleUsernameOrEmail }]">
                            <a-icon :style="{ color: 'rgba(0,0,0,.25)' }" slot="prefix" type="user"/>
                        </a-input>
                    </a-form-item>

                    <a-form-item>
                        <a-input
                                autocomplete='off'
                                placeholder="请输入密码"
                                size="large"
                                type="password"
                                v-decorator="['password',validatorRules.password]">
                            <a-icon :style="{ color: 'rgba(0,0,0,.25)' }" slot="prefix" type="lock"/>
                        </a-input>
                    </a-form-item>

                    <a-row :gutter="0">
                        <a-col :span="16">
                            <a-form-item>
                                <a-input
                                        @change="inputCodeChange"
                                        autocomplete='off'
                                        placeholder="请输入验证码"
                                        size="large"
                                        type="text"
                                        v-decorator="['inputCode',validatorRules.inputCode]">
                                    <a-icon :style="{ color: 'rgba(0,0,0,.25)' }" slot="prefix" type="smile"/>
                                </a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :span="8" style="text-align: right">
                            <img :src="randCodeImage" @click="handleChangeCheckCode" style="margin-top: 2px;"
                                 v-if="requestCodeSuccess"/>
                            <img @click="handleChangeCheckCode" src="../../assets/checkcode.png" style="margin-top: 2px;"
                                 v-else/>
                        </a-col>
                    </a-row>


                </a-tab-pane>
                <a-tab-pane key="tab2" tab="手机号登陆">
                    <a-form-item>
                        <a-input
                                autocomplete='off'
                                placeholder="手机号"
                                size="large"
                                type="text"
                                v-decorator="['mobile',validatorRules.mobile]">
                            <a-icon :style="{ color: 'rgba(0,0,0,.25)' }" slot="prefix" type="mobile"/>
                        </a-input>
                    </a-form-item>

                    <a-row :gutter="16">
                        <a-col :span="16" class="gutter-row">
                            <a-form-item>
                                <a-input autocomplete='off'

                                         placeholder="请输入验证码"
                                         size="large"
                                         type="text"
                                         v-decorator="['captcha',validatorRules.captcha]">
                                    <a-icon :style="{ color: 'rgba(0,0,0,.25)' }" slot="prefix" type="mail"/>
                                </a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :span="8" class="gutter-row">
                            <a-button
                                    :disabled="state.smsSendBtn"
                                    @click.stop.prevent="getCaptcha"
                                    class="getCaptcha"
                                    tabindex="-1"
                                    v-text="!state.smsSendBtn && '获取验证码' || (state.time+' s')"></a-button>
                        </a-col>
                    </a-row>
                </a-tab-pane>
            </a-tabs>

            <a-form-item>
                <a-checkbox v-decorator="['rememberMe', {initialValue: true, valuePropName: 'checked'}]">自动登陆
                </a-checkbox>
                <router-link :to="{ name: 'alteration'}" class="forge-password" style="float: right;">
                    忘记密码
                </router-link>
                <router-link :to="{ name: 'register'}" class="forge-password" style="float: right;margin-right: 10px">
                    注册账户
                </router-link>
            </a-form-item>

            <a-form-item style="margin-top:24px">
                <a-button
                        :disabled="loginBtn"
                        :loading="loginBtn"
                        @click.stop.prevent="handleSubmit"
                        class="login-button"
                        htmlType="submit"
                        size="large"
                        type="primary">确定
                </a-button>
            </a-form-item>

            <div class="user-login-other">
                <span>其他登陆方式</span>
                <a @click="onThirdLogin('github')" title="github">
                    <a-icon class="item-icon" type="github"></a-icon>
                </a>
                <a @click="onThirdLogin('wechat_enterprise')" title="企业微信">
                    <a-icon class="item-icon" type="wechat"></a-icon>
                </a>
                <a @click="onThirdLogin('dingtalk')" title="钉钉">
                    <a-icon class="item-icon" type="dingding"></a-icon>
                </a>
            </div>
        </a-form>

        <two-step-captcha
                :visible="stepCaptchaVisible"
                @cancel="stepCaptchaCancel"
                @success="stepCaptchaSuccess"
                v-if="requiredTwoStepCaptcha"></two-step-captcha>
        <login-select-modal @success="loginSelectOk" ref="loginSelect"></login-select-modal>
    </div>
</template>

<script>
  //import md5 from "md5"
  import TwoStepCaptcha from '@/components/tools/TwoStepCaptcha'
  import { mapActions } from 'vuex'
  import { timeFix } from '@/utils/util'
  import Vue from 'vue'
  import { ACCESS_TOKEN, ENCRYPTED_STRING } from '@/store/mutation-types'
  import { getAction, postAction } from '@/api/manage'
  import { getEncryptedString } from '@/utils/encryption/aesEncrypt'
  import LoginSelectModal from './LoginSelectModal.vue'

  export default {
    components: {
      TwoStepCaptcha,
      LoginSelectModal
    },
    data() {
      return {
        customActiveKey: 'tab1',
        loginBtn: false,
        // login type: 0 email, 1 username, 2 telephone
        loginType: 0,
        requiredTwoStepCaptcha: false,
        stepCaptchaVisible: false,
        form: this.$form.createForm(this),
        encryptedString: {
          key: '',
          iv: ''
        },
        state: {
          time: 60,
          smsSendBtn: false
        },
        validatorRules: {
          username: { rules: [{ required: true, message: '请输入用户名!' }, { validator: this.handleUsernameOrEmail }] },
          password: { rules: [{ required: true, message: '请输入密码!', validator: 'click' }] },
          mobile: { rules: [{ validator: this.validateMobile }] },
          captcha: { rule: [{ required: true, message: '请输入验证码!' }] },
          inputCode: { rules: [{ required: true, message: '请输入验证码!' }] }
        },
        verifiedCode: '',
        inputCodeContent: '',
        inputCodeNull: true,
        currentUsername: '',
        currdatetime: '',
        randCodeImage: '',
        requestCodeSuccess: false
      }
    },
    created() {
      this.currdatetime = new Date().getTime()
      Vue.ls.remove(ACCESS_TOKEN)
      this.getRouterData()
      this.handleChangeCheckCode()
      // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
      //this.getEncrypte();
      // update-end- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
    },
    methods: {
      ...mapActions(['Login', 'Logout', 'PhoneLogin', 'ThirdLogin']),
      //第三方登录
      onThirdLogin(source) {
        let url = window._CONFIG['domianURL'] + `/thirdLogin/render/${source}`
        window.open(url, `login ${source}`, 'height=500, width=500, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no')
        let that = this
        let receiveMessage = function(event) {
          var origin = event.origin
          console.log('origin', origin)

          let token = event.data
          console.log('event.data', token)
          that.ThirdLogin(token).then(res => {
            if (res.success) {
              that.loginSuccess()
            } else {
              that.requestFailed(res)
            }
          })
        }
        window.addEventListener('message', receiveMessage, false)
      },
      // handler
      handleUsernameOrEmail(rule, value, callback) {
        const regex = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/
        if (regex.test(value)) {
          this.loginType = 0
        } else {
          this.loginType = 1
        }
        callback()
      },
      handleTabClick(key) {
        this.customActiveKey = key
        // this.form.resetFields()
      },
      handleSubmit() {
        let that = this
        let loginParams = {}
        that.loginBtn = true
        // 使用账户密码登陆
        if (that.customActiveKey === 'tab1') {
          that.form.validateFields(['username', 'password', 'inputCode', 'rememberMe'], { force: true }, (err, values) => {
            if (!err) {
              loginParams.username = values.username
              // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
              //loginParams.password = md5(values.password)
              //loginParams.password = encryption(values.password,that.encryptedString.key,that.encryptedString.iv)
              loginParams.password = values.password
              loginParams.remember_me = values.rememberMe
              // update-begin- --- author:scott ------ date:20190805 ---- for:密码加密逻辑暂时注释掉，有点问题
              loginParams.captcha = that.inputCodeContent
              loginParams.checkKey = that.currdatetime
              console.log('登录参数', loginParams)
              that.Login(loginParams).then((res) => {
                this.$refs.loginSelect.show(res.result)
              }).catch((err) => {
                that.requestFailed(err)
              })

            } else {
              that.loginBtn = false
            }
          })
          // 使用手机号登陆
        } else {
          that.form.validateFields(['mobile', 'captcha', 'rememberMe'], { force: true }, (err, values) => {
            if (!err) {
              loginParams.mobile = values.mobile
              loginParams.captcha = values.captcha
              loginParams.remember_me = values.rememberMe
              that.PhoneLogin(loginParams).then((res) => {
                console.log(res.result)
                this.$refs.loginSelect.show(res.result)
              }).catch((err) => {
                that.requestFailed(err)
              })

            }
          })
        }
      },
      getCaptcha(e) {
        e.preventDefault()
        let that = this
        this.form.validateFields(['mobile'], { force: true }, (err, values) => {
            if (!values.mobile) {
              that.cmsFailed('请输入手机号')
            } else if (!err) {
              this.state.smsSendBtn = true
              let interval = window.setInterval(() => {
                if (that.state.time-- <= 0) {
                  that.state.time = 60
                  that.state.smsSendBtn = false
                  window.clearInterval(interval)
                }
              }, 1000)

              const hide = this.$message.loading('验证码发送中..', 0)
              let smsParams = {}
              smsParams.mobile = values.mobile
              smsParams.smsmode = '0'
              postAction('/sys/sms', smsParams)
                .then(res => {
                  if (!res.success) {
                    setTimeout(hide, 0)
                    this.cmsFailed(res.message)
                  }
                  console.log(res)
                  setTimeout(hide, 500)
                })
                .catch(err => {
                  setTimeout(hide, 1)
                  clearInterval(interval)
                  that.state.time = 60
                  that.state.smsSendBtn = false
                  this.requestFailed(err)
                })
            }
          }
        )
      },
      stepCaptchaSuccess() {
        this.loginSuccess()
      },
      stepCaptchaCancel() {
        this.Logout().then(() => {
          this.loginBtn = false
          this.stepCaptchaVisible = false
        })
      },
      handleChangeCheckCode() {
        this.currdatetime = new Date().getTime()
        getAction(`/sys/randomImage/${this.currdatetime}`).then(res => {
          if (res.success) {
            this.randCodeImage = res.result
            this.requestCodeSuccess = true
          } else {
            this.$message.error(res.message)
            this.requestCodeSuccess = false
          }
        }).catch(() => {
          this.requestCodeSuccess = false
        })
      },
      loginSuccess() {
        // update-begin- author:sunjianlei --- date:20190812 --- for: 登录成功后不解除禁用按钮，防止多次点击
        // this.loginBtn = false
        // update-end- author:sunjianlei --- date:20190812 --- for: 登录成功后不解除禁用按钮，防止多次点击
        this.$router.push({ path: '/dashboard/analysis' }).catch(() => {
          console.log('登录跳转首页出错,这个错误从哪里来的')
        })
        this.$notification.success({
          message: '欢迎',
          description: `${timeFix()}，欢迎回来`
        })
      },
      cmsFailed(err) {
        this.$notification['error']({
          message: '登录失败',
          description: err,
          duration: 4
        })
      },
      requestFailed(err) {
        this.$notification['error']({
          message: '登录失败',
          description: ((err.response || {}).data || {}).message || err.message || '请求出现错误，请稍后再试',
          duration: 4
        })
        this.loginBtn = false
      },
      validateMobile(rule, value, callback) {
        if (!value || new RegExp(/^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/).test(value)) {
          callback()
        } else {
          callback('您的手机号码格式不正确!')
        }

      },
      validateInputCode(rule, value, callback) {
        if (!value || this.verifiedCode == this.inputCodeContent) {
          callback()
        } else {
          callback('您输入的验证码不正确!')
        }
      },
      generateCode(value) {
        this.verifiedCode = value.toLowerCase()
      },
      inputCodeChange(e) {
        this.inputCodeContent = e.target.value
      },
      loginSelectOk() {
        this.loginSuccess()
      },
      getRouterData() {
        this.$nextTick(() => {
          if (this.$route.params.username) {
            this.form.setFieldsValue({
              'username': this.$route.params.username
            })
          }
        })
      },
      //获取密码加密规则
      getEncrypte() {
        var encryptedString = Vue.ls.get(ENCRYPTED_STRING)
        if (encryptedString == null) {
          getEncryptedString().then((data) => {
            this.encryptedString = data
          })
        } else {
          this.encryptedString = encryptedString
        }
      }
    }
  }
</script>

<style lang="less" scoped>

    .user-layout-login {
        label {
            font-size: 14px;
        }

        .getCaptcha {
            display: block;
            width: 100%;
            height: 40px;
        }

        .forge-password {
            font-size: 14px;
        }

        button.login-button {
            padding: 0 15px;
            font-size: 16px;
            height: 40px;
            width: 100%;
        }

        .user-login-other {
            text-align: left;
            margin-top: 24px;
            line-height: 22px;

            .item-icon {
                font-size: 24px;
                color: rgba(0, 0, 0, .2);
                margin-left: 16px;
                vertical-align: middle;
                cursor: pointer;
                transition: color .3s;

                &:hover {
                    color: #1890ff;
                }
            }

            .register {
                float: right;
            }
        }
    }

</style>
<style>
    .valid-error .ant-select-selection__placeholder {
        color: #f5222d;
    }
</style>
