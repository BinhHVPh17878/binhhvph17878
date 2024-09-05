<template>
  <div>
    <div style="font-size: 12px; margin-bottom: 10px;">TRANG CHỦ</div>
    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="tag-product" style="background-color: transparent !important; box-shadow: unset !important; padding: 0 !important;">
            <div style="display: flex;">
              <el-avatar :size="50" :src="'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
              <div style="display: flex; align-items: center; margin-left: 15px;">
                {{ user.fullName }}
              </div>
            </div>

            <div style="margin-top: 40px;">
              <div style="margin: 20px 0; cursor: pointer; color: #11A983;">
                <router-link to="/shop/user/info">
                  <i class="el-icon-user-solid" />
                  Thông tin tài khoản
                </router-link>
              </div>
              <div style="margin: 20px 0; cursor: pointer;">
                <router-link to="/shop/user/order">
                  <i class="el-icon-document" />
                  Đơn hàng
                </router-link>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="18">
          <div class="tag-product">
            <div style="font-size: 20px;">Thông tin tài khoản</div>
            <el-divider class="custom-divider" />
            <div style="margin-top: 20px; display: grid; grid-template-columns: 1fr 1fr;">
              <el-form ref="form" label-width="120px">
                <el-form-item label="Họ tên">
                  <el-input v-model="user.fullName" />
                </el-form-item>

                <el-form-item label="Số điện thoại">
                  <el-input v-model="user.phoneNumber" />
                </el-form-item>

                <el-form-item label="Email">
                  <el-input v-model="user.email" />
                </el-form-item>

                <el-form-item label="Tỉnh/Thành phố">
                  <el-select v-model="user.cityCode" filterable style="width: 100%;">
                    <el-option
                      v-for="c in listCity"
                      :key="c.province_id"
                      :label="c.province_name"
                      :value="c.province_id"
                    />
                  </el-select>
                </el-form-item>

                <el-form-item label="Quận/Huyện">
                  <el-select v-model="user.districtCode" filterable style="width: 100%;">
                    <el-option
                      v-for="d in listDistrict"
                      :key="d.district_id"
                      :label="d.district_name"
                      :value="d.district_id"
                    />
                  </el-select>
                </el-form-item>

                <el-form-item label="Phường/Xã">
                  <el-select v-model="user.wardCode" filterable style="width: 100%;">
                    <el-option
                      v-for="w in listWard"
                      :key="w.ward_id"
                      :label="w.ward_name"
                      :value="w.ward_id"
                    />
                  </el-select>
                </el-form-item>

                <el-form-item label="Địa chỉ">
                  <el-input
                    v-model="user.address"
                    type="textarea"
                    :rows="3"
                  />
                </el-form-item>

                <el-form-item>
                  <el-button type="primary">Lưu</el-button>
                </el-form-item>
              </el-form>

              <div style="display: flex; justify-content: center; align-items: center;">
                <el-avatar :size="50" :src="'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { ResponseCode } from '@/enums/enums'
import axios from 'axios'
import { getUserInfo } from '@/api/auth'

export default {
  name: 'UserInfoPage',
  components: { },
  data() {
    return {
      listCity: [],
      listDistrict: [],
      listWard: [],
      user: {}
    }
  },
  watch: {
    'user.cityCode': function() {
      axios.get('https://vapi.vnappmob.com/api/province/district/' + this.cityCode)
        .then(res => {
          if (res.data) {
            this.listDistrict = res.data.results
          }
        })
      this.user.city = this.listCity.find(e => e.province_id === this.cityCode).province_name
    },
    'user.districtCode': function() {
      axios.get('https://vapi.vnappmob.com/api/province/ward/' + this.districtCode)
        .then(res => {
          if (res.data) {
            this.listWard = res.data.results
          }
        })
      this.user.district = this.listDistrict.find(e => e.district_id === this.districtCode).district_name
    },
    'user.wardCode': function() {
      this.user.ward = this.listWard.find(e => e.ward_id === this.wardCode).ward_name
    }
  },
  async created() {
    axios.get('https://vapi.vnappmob.com/api/province/')
      .then(res => {
        if (res.data) {
          this.listCity = res.data.results
        }
      })

    await getUserInfo().then((res) => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.user = res.data
        this.cityCode = res.data.cityCode
        this.districtCode = res.data.districtCode
        this.wardCode = res.data.wardCode
      }
    })
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
  }
}
</script>

<style lang="scss" scoped>
.tag-product {
  background-color: #ffffff;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .08);
  padding: 20px;
}

::v-deep .custom-divider, .el-divider--horizontal {
  margin: 0;
  height: 0.5px;
}

.image-custom {
  width: 100%;
  height: 200px;
  overflow: hidden;
  position: relative;
}

.image-custom img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease-in-out;
}

.image-custom:hover img {
    transform: scale(1.1); /* Phóng to ảnh khi hover */
}

.image-container {
  position: relative; display: inline-block;
}

.combo-button {
  background-color: #11A983;
  position: absolute;
  top: 80%;
  left: 20px;
  right: 20px;
  display: none;
  transition: opacity 0.5s ease-in-out;
  border-radius: 4px;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .08);
  padding: 4px;
}

.image-container:hover .combo-button {
  display: block
}

.image-container img {
  transition: transform 0.2s;
}

.image-container:hover img {
  transform: scale(1.1);
}
</style>
