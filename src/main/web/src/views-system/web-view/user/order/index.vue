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
              <div style="margin: 20px 0; cursor: pointer;">
                <router-link to="/shop/user/info">
                  <i class="el-icon-user-solid" />
                  Thông tin tài khoản
                </router-link>
              </div>
              <div style="margin: 20px 0; cursor: pointer; color: #11A983;">
                <router-link to="/shop/user/order">
                  <i class="el-icon-document" />
                  Đơn hàng
                </router-link>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="18">
          <div v-for="o in listOrder" :key="o.id" class="tag-product" style="margin-bottom: 20px;">
            <div style="margin-bottom: 10px; display: grid; grid-template-columns: 1fr 1fr;">
              <div>
                {{ moment(o.order.createdAt).format('HH:mm:ss DD-MM-YYYY') }}
              </div>
              <div style="text-align: end; color: #ee4d2d;">
                <span v-if="o.order.status === 'WAIT_FOR_CONFIRMATION'">
                  Chờ xác nhận
                </span>
                <span v-if="o.order.status === 'PREPARING_GOODS'">
                  Chuẩn bị hàng
                </span>
                <span v-if="o.order.status === 'DELIVERING'">
                  Đang giao hàng
                </span>
                <span v-if="o.order.status === 'DELIVERED'">
                  Đã giao hàng
                </span>
                <span v-if="o.order.status === 'SUCCESS'">
                  Thành công
                </span>
                <span v-if="o.order.status === 'CANCELLED'">
                  Đã hủy
                </span>
              </div>
            </div>
            <div style="margin-top: 10px;">
              <el-timeline :reverse="true" style="padding-inline-start: 0;">
                <el-timeline-item
                  v-for="(h, index) in o.listHistoryOrder"
                  :key="index"
                  :timestamp="moment(h.createdAt).format('HH:mm:ss DD-MM-YYYY')"
                >
                  <span v-if="h.status === 'WAIT_FOR_CONFIRMATION'">
                    <el-tag :type="o.listHistoryOrder.length === 1 ? 'warning' : 'success'" size="small">Chờ xác nhận</el-tag>
                  </span>
                  <span v-if="h.status === 'PREPARING_GOODS'">
                    <el-tag :type="o.listHistoryOrder.length === 2 ? 'warning' : 'success'" size="small">Chuẩn bị hàng</el-tag>
                  </span>
                  <span v-if="h.status === 'DELIVERING'">
                    <el-tag :type="o.listHistoryOrder.length === 3 ? 'warning' : 'success'" size="small">Đang giao hàng</el-tag>
                  </span>
                  <span v-if="h.status === 'DELIVERED'">
                    <el-tag :type="o.listHistoryOrder.length === 4 ? 'warning' : 'success'" size="small">Đã giao hàng</el-tag>
                  </span>
                  <span v-if="h.status === 'SUCCESS'">
                    <el-tag type="success" size="small">Thành công</el-tag>
                  </span>
                  <span v-if="h.status === 'CANCELLED'">
                    <el-tag type="danger" size="small">Đã hủy</el-tag>
                    <div v-if="o.order.cancelBy === 'ADMIN'" style="margin-top: 5px;">Người bán hủy</div>
                    <div v-if="o.order.cancelBy === 'CUSTOMER'" style="margin-top: 5px;">Người mua hủy</div>
                  </span>
                </el-timeline-item>
              </el-timeline>
            </div>
            <el-divider class="custom-divider" />

            <div v-for="od in o.listOrderDetail" :key="od.id" style="margin-top: 10px; margin-bottom: 10px;">
              <div style="margin-bottom: 10px; display: grid; grid-template-columns: 1fr 4fr 1.5fr;">
                <div>
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="o.listImage.find(e => e.id === od.id).listImage[0]"
                  />
                </div>
                <div>
                  <div>
                    {{ od.productDetail.product.name }}
                  </div>

                  <div style="margin-top: 10px; font-size: 14px;">
                    {{ od.productDetail.size.name }} - {{ od.productDetail.color.name }} - {{ od.productDetail.material.name }}
                  </div>

                  <div style="margin-top: 10px; font-size: 14px;">
                    x{{ od.quantity }}
                  </div>
                </div>
                <div style="text-align: end; color: #ee4d2d; display: flex; justify-content: end; align-items: center">
                  <span v-if="od.totalAmount > od.totalAmountNet" style="text-decoration: line-through; color: rgba(0,0,0,.26);">{{ formatCurrencyVND(od.totalAmount) }}</span>
                  {{ formatCurrencyVND(od.totalAmountNet) }}
                </div>
              </div>
              <el-divider class="custom-divider" />
            </div>

            <div style="margin-top: 20px; text-align: end; color: #ee4d2d; font-size: 18px;">
              Thành tiền: {{ formatCurrencyVND(o.order.totalAmountNet) }}
            </div>

            <div style="text-align: end;">
              <el-button v-if="o.order.status === 'WAIT_FOR_CONFIRMATION'" style="margin-top: 20px;" @click="changeStatus('CANCELLED', o.order.id)">
                Hủy đơn hàng
              </el-button>

              <el-button v-if="o.order.status === 'DELIVERED'" type="primary" style="margin-top: 20px;" @click="changeStatus('SUCCESS', o.order.id)">
                Xác nhận
              </el-button>
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
import { orderGetAllByUser } from '@/api/order'
import { formatCurrencyVND } from '@/utils/format'
import moment from 'moment'
import { orderChangeStatus } from '@/api/order'

export default {
  name: 'UserOrderPage',
  components: { },
  data() {
    return {
      moment: moment,
      formatCurrencyVND: formatCurrencyVND,
      listCity: [],
      listDistrict: [],
      listWard: [],
      user: {},
      listOrder: []
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

    orderGetAllByUser().then((res) => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listOrder = res.data
        console.log(res.data)
      }
    })
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    changeStatus(status, id) {
      this.$confirm('Bạn xác nhận?', 'Xác nhận.', {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning'
      }).then(() => {
        orderChangeStatus({
          id: id,
          status: status
        }).then((res) => {
          if (res) {
            this.$notify({
              title: 'Thông báo.',
              message: 'Thành công.',
              type: 'success',
              offset: 100
            })
            this.getListOrder()
          }
        })
      })
    },
    getListOrder() {
      orderGetAllByUser().then((res) => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.listOrder = res.data
          console.log(res.data)
        }
      })
    }
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
