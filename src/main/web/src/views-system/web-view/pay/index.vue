<template>
  <div>
    <div style="font-size: 12px; margin-bottom: 10px;">TRANG CHỦ</div>
    <div class="tag-product">
      <el-row :gutter="40">
        <el-col :span="12">
          <el-form>
            <el-row :gutter="20">
              <!-- <el-col :span="24">
                <el-form-item>
                  <el-alert
                    title="Vui lòng đăng nhập và nhập thông tin để có thể theo dõi trạng thái đơn hàng."
                    type="warning"
                    show-icon
                    :closable="false"
                  />
                </el-form-item>
              </el-col> -->

              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Họ và tên
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.fullName" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Số điện thoại
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.phoneNumber" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Email
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.email" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Tỉnh/Thành phố
                    <span style="color: red;">*</span>
                  </span>
                  <el-select v-model="cityCode" filterable style="width: 100%;">
                    <el-option
                      v-for="c in listCity"
                      :key="c.province_id"
                      :label="c.province_name"
                      :value="c.province_id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Quận/Huyện
                    <span style="color: red;">*</span>
                  </span>
                  <el-select v-model="districtCode" filterable style="width: 100%;">
                    <el-option
                      v-for="d in listDistrict"
                      :key="d.district_id"
                      :label="d.district_name"
                      :value="d.district_id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Phường/Xã
                    <span style="color: red;">*</span>
                  </span>
                  <el-select v-model="wardCode" filterable style="width: 100%;">
                    <el-option
                      v-for="w in listWard"
                      :key="w.ward_id"
                      :label="w.ward_name"
                      :value="w.ward_id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Địa chỉ
                    <span style="color: red;">*</span>
                  </span>
                  <el-input v-model="orderW.address" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item style="margin-bottom: 10px;">
                  <span slot="label">
                    Ghi chú
                  </span>
                  <el-input v-model="orderW.noteByCustomer" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>

        <el-col :span="12">
          <div style="background-color: #fffbf8; padding: 20px; border: 1px solid #f9ede5;">
            <span style="font-size: 16px;">ĐƠN HÀNG CỦA BẠN</span>
            <el-divider class="pay-divider" />
            <div>
              <el-row>
                <el-col :span="16">
                  <div style="font-size: 16px;">Sản phẩm</div>
                </el-col>
                <el-col :span="8" style="text-align: end;">
                  <div style="font-size: 16px;">Tổng tiền</div>
                </el-col>
                <div v-for="od in orderW.listOrderDetail" :key="od.productDetailId">
                  <el-col :span="14" style="margin-top: 10px;">
                    <div style="">
                      {{ od.quantity }}
                      <i class="el-icon-close" style="cursor: pointer;" />
                      {{ od.productDetail.product.name }}
                    </div>
                  </el-col>
                  <el-col :span="10" style="text-align: end; margin-top: 10px;">
                    <div style="color: #ee4d2d;">
                      <span v-if="od.totalAmount > od.totalAmountNet" style="text-decoration: line-through; color: rgba(0,0,0,.26);">{{ formatCurrencyVND(od.totalAmount) }}</span>
                      {{ formatCurrencyVND(od.totalAmountNet) }}
                    </div>
                  </el-col>
                </div>
              </el-row>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <div style="font-size: 16px; margin-bottom: 10px;">Mã giảm giá <span v-if="orderW.voucher">({{ orderW.voucher.type === 'PERCENT' ? orderW.voucher.value + '% - tối đa ' + formatCurrencyVND(orderW.voucher.max) : formatCurrencyVND(orderW.voucher.value) }})</span></div>
              <div>
                <el-row :gutter="20">
                  <el-col :span="18">
                    <el-input v-model="orderW.voucherCode" />
                  </el-col>
                  <el-col :span="6">
                    <div style="font-size: 18px; cursor: pointer; background-color: #11A983; color: #fff; border-radius: 4px; padding: auto; height: 36px; display: flex; align-items: center; justify-content: center;" @click="apMa()">Áp dụng</div>
                  </el-col>
                </el-row>
              </div>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <div style="font-size: 16px;">Hình thức thanh toán</div>
              <el-radio-group v-model="orderW.paymentType" style="margin-top: 10px;">
                <el-radio :label="'PAYMENT_ON_DELIVERY'" class="pay-radio">Khi nhận hàng</el-radio>
                <el-radio :label="'PAYMENT_ONLINE'" class="pay-radio">Online</el-radio>
              </el-radio-group>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <el-row>
                <el-col :span="16">
                  <div>Tổng tiền gốc</div>
                  <div style="margin-top: 10px;">Giảm giá</div>
                  <div style="margin-top: 10px;">Tổng thanh toán</div>
                </el-col>
                <el-col :span="8" style="text-align: end;">
                  <div style="font-size: 16px; color: #ee4d2d;">{{ formatCurrencyVND(orderW.totalAmount) }}</div>
                  <div style="font-size: 16px; color: #ee4d2d; margin-top: 10px;">-{{ formatCurrencyVND(orderW.totalAmount - orderW.totalAmountNet) }}</div>
                  <div style="font-size: 16px; color: #ee4d2d; margin-top: 10px;">{{ formatCurrencyVND(orderW.totalAmountNet) }}</div>
                </el-col>
              </el-row>
              <div style="font-size: 18px; cursor: pointer; margin-top: 10px; background-color: #11A983; color: #fff; border-radius: 4px; padding: auto; height: 36px; display: flex; align-items: center; justify-content: center;" @click="payment">
                {{ order.paymentType === 'PAYMENT_ONLINE' ? 'Thanh toán' : 'Đặt hàng' }}
              </div>
            </div>
            <el-divider class="pay-divider" />
            <div>
              <div>
                <i class="el-icon-phone-outline" />
                Hotline 1900 1000
              </div>
              <div style="margin-top: 10px;">
                <i class="el-icon-tickets" />
                Hướng dẫn đặt hàng
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getAllProductDetailByListId } from '@/api/product-detail'
import { paymentOnline } from '@/api/payment'
import { orderOnline } from '@/api/order'
import { ResponseCode, Status, VoucherType } from '@/enums/enums'
import { formatCurrencyVND } from '@/utils/format'
import { mapActions, mapGetters } from 'vuex'
import axios from 'axios'
import { voucherGetByCode } from '@/api/voucher'
import { getUserInfo } from '@/api/auth'

export default {
  name: 'PayPage',
  components: { },
  data() {
    return {
      formatCurrencyVND: formatCurrencyVND,
      listCity: [],
      listDistrict: [],
      listWard: [],
      cityCode: '',
      districtCode: '',
      wardCode: '',
      city: '',
      district: '',
      ward: '',
      productInCart: [],
      typePay: 'PAYMENT_ON_DELIVERY',
      fullNameCustomer: '',
      phoneNumber: '',
      address: '',
      email: '',
      voucherId: '',
      noteByCustomer: '',
      paymentOnlineCode: '',
      orderW: {
        id: '',
        fullName: '',
        phoneNumber: '',
        email: '',
        address: '',
        city: '',
        district: '',
        ward: '',
        provinceId: '',
        districtId: '',
        wardId: '',
        totalAmount: 0,
        totalAmountNet: 0,
        voucherId: '',
        noteByCustomer: '',
        noteByAdmin: '',
        createdAt: '',
        updatedAt: '',
        createdBy: '',
        updatedBy: '',
        completedAt: '',
        type: '',
        paymentType: 'PAYMENT_ON_DELIVERY',
        status: '',
        listOrderDetail: [],
        isVoucher: false,
        voucherCode: '',
        voucher: null,
        moneyPaid: 0,
        moneyRefunds: 0
      }
    }
  },
  computed: {
    ...mapGetters([
      'cart',
      'allInfor',
      'pay',
      'userId',
      'order',
      'orderWStore'
    ])
  },
  watch: {
    cityCode: function() {
      axios.get('https://vapi.vnappmob.com/api/province/district/' + this.cityCode)
        .then(res => {
          if (res.data) {
            this.listDistrict = res.data.results
          }
        })
      this.orderW.city = this.listCity.find(e => e.province_id === this.cityCode).province_name
      this.changePay()
    },
    districtCode: function() {
      axios.get('https://vapi.vnappmob.com/api/province/ward/' + this.districtCode)
        .then(res => {
          if (res.data) {
            this.listWard = res.data.results
          }
        })
      this.orderW.district = this.listDistrict.find(e => e.district_id === this.districtCode).district_name
      this.changePay()
    },
    wardCode: function() {
      this.orderW.ward = this.listWard.find(e => e.ward_id === this.wardCode).ward_name
      this.changePay()
    },
    cart: function() {
      this.getListProductDetail()
      this.changePay()
    },
    typePay: function() {
      this.changePay()
    },
    fullNameCustomer: function() {
      this.changePay()
    },
    phoneNumber: function() {
      this.changePay()
    },
    address: function() {
      this.changePay()
    },
    email: function() {
      this.changePay()
    },
    voucherId: function() {
      this.changePay()
    },
    noteByCustomer: function() {
      this.changePay()
    },
    orderW: async function() {
      await this.setListOrderDetail()
      this.setOrder(this.orderW)
    }
  },
  async created() {
    axios.get('https://vapi.vnappmob.com/api/province/')
      .then(res => {
        if (res.data) {
          this.listCity = res.data.results
        }
      })
    if (this.cart.length > 0) {
      await this.getListProductDetail()
    }

    await getUserInfo().then((res) => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.orderW.fullName = res.data.fullName
        this.orderW.address = res.data.address
        this.orderW.phoneNumber = res.data.phoneNumber
        this.orderW.email = res.data.email
        this.cityCode = res.data.cityCode
        this.districtCode = res.data.districtCode
        this.wardCode = res.data.wardCode
      }
    })

    console.log(this.$route.query.vnp_TransactionStatus)
    if (this.$route.query.vnp_TransactionStatus === '00' && this.orderWStore) {
      orderOnline(this.orderWStore).then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.clearCart()
          this.$notify({
            title: 'Success',
            message: 'Đặt hàng thành công.',
            type: 'success'
          })
          this.$router.push({
            path: '/shop'
          })
        }
      })
    }
  },
  methods: {
    ...mapActions([
      'clearCart',
      'setPay',
      'setOrder',
      'setOrderW'
    ]),
    formatCurrenyVND_d(value) {
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value)
    },
    async getListProductDetail() {
      await getAllProductDetailByListId({
        listProductDetailId: this.cart.map(e => e.productDetailId)
      }).then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.productInCart = res.data
          res.data.forEach((e) => {
            this.orderW.listOrderDetail.push({
              id: null,
              orderId: null,
              productDetail: e,
              productDetailId: e.id,
              productDetailPrice: e.price,
              productDetailPriceNet: e.priceNet,
              quantity: this.cart.find(e1 => e1.productDetailId === e.id).quantity,
              totalAmount: e.price * this.cart.find(e1 => e1.productDetailId === e.id).quantity,
              totalAmountNet: e.priceNet * this.cart.find(e1 => e1.productDetailId === e.id).quantity,
              createdAt: '',
              updatedAt: '',
              createdBy: '',
              updatedBy: '',
              status: Status.ACTIVE
            })
          })
        }
      })
      console.log(this.orderW)
      await this.calculatedTotalMoney()
    },
    genQuantityPurchased(productDetailId) {
      console.log(this.cart.find(e => e.productDetailId === productDetailId))
      if (this.cart.length > 0) {
        return this.cart.find(e => e.productDetailId === productDetailId).quantity
      } else {
        return 0
      }
    },
    genTotalMoneyProduct(productDetailId, price) {
      if (this.cart.length > 0) {
        return this.cart.find(e => e.productDetailId === productDetailId).quantity * price
      } else {
        return 0
      }
    },
    genTotalMoney() {
      if (this.cart.length > 0) {
        let total = 0
        this.productInCart.forEach(e => {
          total += e.price * this.cart.find(x => x.productDetailId === e.id).quantity
        })
        return total
      } else {
        return 0
      }
    },
    genTotalMoneyNet() {
      if (this.cart.length > 0) {
        let total = 0
        this.productInCart.forEach(e => {
          total += e.priceNet * this.cart.find(x => x.productDetailId === e.id).quantity
        })
        return total
      } else {
        return 0
      }
    },
    payment() {
      this.$confirm('Bạn xác nhận đặt hàng?', 'Xác nhận.', {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning'
      }).then(() => {
        if (this.orderW.paymentType === 'PAYMENT_ONLINE') {
          paymentOnline({
            id: this.userId,
            money: this.orderW.totalAmountNet,
            bankCode: '',
            billMobile: this.orderW.phoneNumber,
            billEmail: this.orderW.email,
            fullname: this.orderW.fullName,
            billAddress: this.orderW.address,
            billCity: '',
            billCountry: '',
            billState: ''
          }).then(res => {
            if (res && res.code === ResponseCode.CODE_SUCCESS) {
              this.setOrderW(this.orderW)
              window.location.replace(res.data)
            }
          })
        } else {
          this.orderW.district = this.listDistrict.find((e) => e.district_id === this.districtCode).district_name
          this.orderW.ward = this.listWard.find((e) => e.ward_id === this.wardCode).ward_name
          this.orderW.city = this.listCity.find((e) => e.province_id === this.cityCode).province_name
          orderOnline(this.orderW).then(res => {
            if (res && res.code === ResponseCode.CODE_SUCCESS) {
              this.clearCart()
              this.$notify({
                title: 'Success',
                message: 'Đặt hàng thành công.',
                type: 'success'
              })
              this.$router.push({
                path: '/shop'
              })
            }
          })
        }
      })
    },
    changePay() {
      this.setPay({
        city: this.city,
        district: this.district,
        ward: this.ward,
        paymentType: this.typePay,
        fullNameCustomer: this.fullNameCustomer,
        phoneNumber: this.phoneNumber,
        address: this.address,
        voucherId: this.voucherId,
        noteByCustomer: this.noteByCustomer,
        totalAmount: this.genTotalMoney(),
        createdBy: this.allInfor.id,
        orderDetails: this.cart,
        email: this.email
      })
    },
    async setListOrderDetail() {
      await this.getListProductDetail()
      this.productInCart.forEach(e => {
        this.orderW.listOrderDetail.push({
          id: null,
          orderId: null,
          productDetail: null,
          productDetailId: e.id,
          productDetailPrice: e.price,
          productDetailPriceNet: e.priceNet,
          quantity: this.cart.find(x => x.productDetailId === e.id).quantity,
          totalAmount: this.genTotalMoneyProduct(e.id, e.price),
          totalAmountNet: this.genTotalMoneyProduct(e.id, e.priceNet),
          createdAt: '',
          updatedAt: '',
          createdBy: '',
          updatedBy: '',
          status: Status.ACTIVE
        })
      })
      this.orderW.totalAmount = this.genTotalMoney()
      this.orderW.totalAmountNet = this.genTotalMoneyNet()
    },
    apMa() {
      this.orderW.isVoucher = true
      this.calculatedTotalMoney()
    },
    async calculatedTotalMoney() {
      let totalAmountP = 0
      let totalAmountPNet = 0
      let totalDiscountMoney = 0
      let discountMoney = 0
      let totalPriceDifference = 0

      if (!this.orderW.isVoucher) {
        this.orderW.voucher = null
        this.orderW.voucherCode = ''
        this.orderW.voucherId = ''
      }

      if (this.orderW.isVoucher && this.orderW.voucherCode) {
        await voucherGetByCode(this.orderW.voucherCode)
          .then(res => {
            if (res && res.code === ResponseCode.CODE_SUCCESS && res.data) {
              this.orderW.voucher = res.data
              this.orderW.voucherId = res.data.id
            }
          })
      }

      if (this.orderW.voucher && this.orderW.voucher.type === VoucherType.MONEY) {
        totalDiscountMoney = this.orderW.voucher.value > this.orderW.listOrderDetail.reduce((sum, od) => sum + (Number(od.quantity) * Number(od.productDetailPriceNet)), 0) ? this.orderW.listOrderDetail.reduce((sum, od) => sum + (Number(od.quantity) * Number(od.productDetailPriceNet)), 0) : this.orderW.voucher.value
        discountMoney = this.orderW.voucher.value / this.orderW.listOrderDetail.reduce((sum, od) => sum + Number(od.quantity), 0)
      }

      if (this.orderW.voucher && this.orderW.voucher.type === VoucherType.PERCENT) {
        totalDiscountMoney = this.orderW.voucher.value * this.orderW.listOrderDetail.reduce((sum, od) => sum + (Number(od.quantity) * Number(od.productDetailPriceNet)), 0) / 100
        if (totalDiscountMoney > this.orderW.voucher.max) {
          totalDiscountMoney = this.orderW.voucher.max
        }
        discountMoney = totalDiscountMoney / this.orderW.listOrderDetail.reduce((sum, od) => sum + Number(od.quantity), 0)
      }

      for (let j = 0; j < this.orderW.listOrderDetail.length; j++) {
        totalPriceDifference += (this.orderW.listOrderDetail[j].productDetailPrice - this.orderW.listOrderDetail[j].productDetailPriceNet) * this.orderW.listOrderDetail[j].quantity
        const totalAmountOD = this.orderW.listOrderDetail[j].productDetailPrice * this.orderW.listOrderDetail[j].quantity
        let totalAmountODNet = 0
        if (this.orderW.isVoucher && this.orderW.voucherCode && this.orderW.voucher) {
          if (j === this.orderW.listOrderDetail.length - 1) {
            totalAmountODNet = totalAmountP + totalAmountOD - totalPriceDifference - totalDiscountMoney - totalAmountPNet
          } else {
            totalAmountODNet = Math.floor((this.orderW.listOrderDetail[j].productDetailPriceNet * this.orderW.listOrderDetail[j].quantity) - (this.orderW.listOrderDetail[j].quantity > 1 ? (discountMoney * this.orderW.listOrderDetail[j].quantity) : discountMoney))
          }
        } else {
          totalAmountODNet = this.orderW.listOrderDetail[j].productDetailPriceNet * this.orderW.listOrderDetail[j].quantity
        }
        this.orderW.listOrderDetail[j].totalAmount = isNaN(totalAmountOD) ? 0 : totalAmountOD
        this.orderW.listOrderDetail[j].totalAmountNet = isNaN(totalAmountODNet) ? 0 : totalAmountODNet
        totalAmountP += totalAmountOD
        totalAmountPNet += totalAmountODNet
      }

      this.orderW.totalAmount = totalAmountP
      this.orderW.totalAmountNet = totalAmountPNet
      console.log(this.orderW)
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
.tag {
  cursor: pointer;
  padding: 5px 15px;
  border: 1px solid rgba(0,0,0,.09);
  border-radius: 2px;
}
.tag:hover {
  border-color: #11A983;
  color: #11A983;
}
.tag-active {
  border-color: #11A983;
  color: #11A983;
}
.tag-disable {
  cursor: not-allowed;
  background-color: rgba(0,0,0,.09);
}
.tag-disable:hover {
  border-color: rgba(0,0,0,.09);
  color: #000;
}
::v-deep .input-text-align-center {
  input {
    text-align: center !important;
  }
}
</style>
