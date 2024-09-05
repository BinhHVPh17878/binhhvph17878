<template>
  <div class="container-web">
    <el-row :gutter="200">
      <el-col :span="12">
        <div style="padding: 25% 0 25% 12%;">
          <img style="filter: brightness(0) invert(1);" :src="require('@/assets/images/logojb.png')" alt="">
        </div>
      </el-col>

      <el-col :span="12">
        <div class="login-form">
          <h3>Đăng nhập</h3>

          <el-form ref="dataFormLogin" v-loading="loading" :model="user" :rules="rules">
            <el-form-item v-if="isShowError">
              <el-alert
                title="Tài khoản hoặc mật khẩu không đúng."
                type="error"
                show-icon
                :closable="false"
              />
            </el-form-item>

            <el-form-item prop="username">
              <el-input v-model="user.username" placeholder="Tài khoản" />
            </el-form-item>

            <el-form-item prop="password">
              <el-input v-model="user.password" type="password" placeholder="Mật khẩu" />
            </el-form-item>

            <el-form-item style="margin-bottom: 0;">
              <el-button style="width: 100%" type="primary" @click="login">Đăng nhập</el-button>
            </el-form-item>

            <el-form-item>
              <span style="font-size: 13px;">Quên mật khẩu?</span>
            </el-form-item>

            <el-divider content-position="center">Hoặc</el-divider>

            <el-form-item>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-button style="width: 100%;">Facebook</el-button>
                </el-col>

                <el-col :span="12">
                  <el-button style="width: 100%;">Google</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item>
              <div style="font-size: 15px; color: rgba(0,0,0,.26); text-align: center;">Bạn chưa có tài khoản? <span style="color: #11A983; font-weight: bold;">Đăng ký</span></div>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { Role } from '@/enums/enums'

export default {
  name: 'LoginPage',
  components: { },
  data() {
    return {
      user: {
        username: '',
        password: ''
      },
      rules: {
        userName: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ]
      },
      isShowError: false,
      otherQuery: {},
      redirect: undefined,
      loading: false
    }
  },
  computed: {
    ...mapState({
      roles: state => state.user.roles
    })
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    async login() {
      this.$refs.dataFormLogin.validate(async valid => {
        if (valid) {
          this.loading = true
          this.isShowError = false
          await this.$store.dispatch('user/login', this.user).then(async() => {
            if (this.roles.includes(Role.ROLE_CUSTOMER)) {
              await this.$store.dispatch('user/getInfo')
              this.$router.push({ path: this.redirect || '/shop', query: this.otherQuery })
            } else {
              this.isShowError = true
            }
            // console.log(res.data.roles.includes(Role.ROLE_CUSTOMER))
            // if (res && res.code === ResponseCode.CODE_SUCCESS && res.data.roles.includes(Role.ROLE_CUSTOMER)) {
            //   this.$router.push({ path: this.redirect || '/shop', query: this.otherQuery })
            // } else {
            //   this.isShowError = true
            // }
          }).catch(() => {
            this.isShowError = true
          })
          this.loading = false
        } else {
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
  }
}
</script>

<style lang="scss" scoped>
.container-web {
  background-color: #11A983;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .08);
  border-radius: 4px;
  padding: 40px;
}

.login-form {
  background-color: #ffffff;
  border-radius: 4px;
  padding: 10px 30px 20px 30px;
}
</style>
