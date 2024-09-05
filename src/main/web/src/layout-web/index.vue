<template>
  <div>
    <header>
      <div class="header-1">
        <div class="menu">
          <i class="el-icon-phone" />
          Liên hệ: 19001098
        </div>

        <div class="menu">
          <router-link to="/shop/user/info">
            <i class="el-icon-user-solid" @click="login" />
            {{ isCustomer ? name : 'Đăng nhập' }}
          </router-link>
        </div>

        <div v-if="isCustomer" class="menu" style="border-right: 0;" @click="logout">
          Đăng xuất
          <i class="el-icon-right" />
        </div>
      </div>

      <div class="header-2">
        <div>
          <img style="height: 9vh; filter: brightness(0) invert(1); cursor: pointer;" :src="require('@/assets/images/logojb.png')" alt="" @click="toHome">
        </div>

        <div style="display: flex; align-items: center; background-color: #ffffff; border-radius: 4px;">
          <div>
            <input placeholder="Tìm kiếm Sản phẩm" type="text" class="input-search">
          </div>

          <div>
            <button class="button-search">
              <i class="el-icon-search" />
            </button>
          </div>
        </div>

        <div style="width: 55px;">
          <div class="cart">
            <el-popover
              v-model="showCart"
              placement="bottom-end"
              width="800"
            >
              <!--Content cart-->
              <div style="margin: 10px;">
                <div style="display: flex; justify-content: space-between;">
                  <span style="font-size: 16px;">Giỏ hàng ({{ cart.length }} sản phẩm)</span>
                  <div />
                  <div @click="showCart = false">
                    <i class="el-icon-close" style="cursor: pointer;" />
                  </div>
                </div>

                <el-divider />

                <div style="overflow-y: scroll; max-height: calc(100vh - 400px);">
                  <div v-for="(p, i) in productInCart" :key="p.id" :style="i === 0 ? 'display: flex; position: relative; margin-right: 60px;' : 'margin-top: 15px; display: flex; position: relative; margin-right: 60px;'">
                    <el-image style="width: 150px;" :src="p.listImage[0] ? p.listImage[0] : p.product.listImage[0]" />
                    <div style="margin-left: 10px;">
                      <span>{{ p.product.name }}</span>
                      <div style="display: flex; margin-top: 10px;">
                        <div>
                          <div style="font-weight: 100;">Kích cỡ:</div>
                          <div style="font-weight: 100; margin-top: 5px;">Màu sắc:</div>
                          <div style="font-weight: 100; margin-top: 5px;">Chất liệu:</div>
                          <div style="font-weight: 100; margin-top: 5px;">Số lượng:</div>
                          <div style="font-weight: 100; margin-top: 5px;">Tổng tiền:</div>
                        </div>
                        <div style="margin-left: 40px;">
                          <div style="font-weight: 100;">{{ p.size.name }}</div>
                          <div style="font-weight: 100; margin-top: 5px;">{{ p.color.name }}</div>
                          <div style="font-weight: 100; margin-top: 5px;">{{ p.material.name }}</div>
                          <div style="font-weight: 100; margin-top: 5px;">{{ genQuantityPurchased(p.id) }}</div>
                          <div style="font-weight: 100; margin-top: 5px; color: #ee4d2d;">
                            {{ formatCurrencyVND(genTotalMoneyProductNet(p.id, p.priceNet)) }}
                            <span v-if="genTotalMoneyProduct(p.id, p.price) > genTotalMoneyProductNet(p.id, p.priceNet)" style="text-decoration: line-through; color: rgba(0,0,0,.26);">{{ formatCurrencyVND(genTotalMoneyProduct(p.id, p.price)) }}</span>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div style="position: absolute; top: 0; right: -50px; cursor: pointer;">
                      <i class="el-icon-close" @click="removeProduct(p.id)" />
                    </div>
                  </div>
                </div>

                <el-divider />

                <div>
                  <div style="display: flex; justify-content: space-between;">
                    <div>
                      <div style="font-size: 16px;">Tổng tiền:</div>
                    </div>
                    <div />
                    <div>
                      <div style="color: #ee4d2d; font-size: 16px;">
                        {{ formatCurrencyVND(genTotalMoneyNet()) }}
                        <span v-if="genTotalMoney() > genTotalMoneyNet()" style="text-decoration: line-through; color: rgba(0,0,0,.26);">{{ formatCurrencyVND(genTotalMoney()) }}</span>
                      </div>
                    </div>
                  </div>
                  <div style="margin-top: 20px; display: flex; justify-content: space-between;">
                    <span style="padding: 10px 25px; background-color: #11A983; color: #fff; border-radius: 4px; font-size: 18px; cursor: pointer;" @click="showCart = false">
                      Tiếp tục mua sắm
                    </span>
                    <div />
                    <span style="padding: 10px 25px; background-color: #11A983; color: #fff; border-radius: 4px; font-size: 18px; cursor: pointer;" @click="pay">
                      Đặt hàng
                    </span>
                  </div>
                </div>
              </div>
              <!--Content cart-->

              <!-- Button add to cart -->
              <div slot="reference">
                <el-badge :value="genTotalSP()" class="item">
                  <i class="el-icon-shopping-cart-full" style="cursor: pointer;" />
                </el-badge>
              </div>
              <!-- Button add to cart -->
            </el-popover>
            <!-- {{ formatCurrencyVND(100000) }} -->
          </div>
        </div>
      </div>

      <div class="header-3">
        <div class="menu" style="background-color: #005842;">
          <i class="el-icon-s-unfold" />

          <span style="margin-left: 10px; margin-right: 10px;">DANH SÁCH DANH MỤC</span>

          <i class="el-icon-caret-bottom" />
        </div>

        <div class="menu">
          TRANG CHỦ
        </div>

        <div class="menu">
          GIỚI THIỆU
        </div>

        <div class="menu">
          LIÊN HỆ
        </div>

        <div class="menu">
          CHÍNH SÁCH
        </div>
      </div>
    </header>

    <div style="background-color: #f3f3f3; padding: 10px 12% 10px 12%; min-height: 80vh;">
      <router-view :key="key" />
    </div>

    <footer>
      <div class="footer-1">
        //
      </div>

      <div class="footer-2">
        <div>
          <div>
            //
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import { formatCurrencyVND } from '@/utils/format'
import { mapGetters, mapActions } from 'vuex'
import { ResponseCode } from '@/enums/enums'
import { getAllProductDetailByListId } from '@/api/product-detail'

export default {
  name: 'LayoutWeb',
  components: {},
  data() {
    return {
      formatCurrencyVND: formatCurrencyVND,
      showCart: false,
      productInCart: []
    }
  },
  computed: {
    key() {
      return this.$route.path
    },
    ...mapGetters(['userId', 'name', 'isCustomer', 'cart', 'token'])
  },
  watch: {
    cart: function() {
      this.getListProductDetail()
    },
    showCart: function() {
      if (this.showCart) {
        this.getListProductDetail()
      }
    }
  },
  methods: {
    ...mapActions({
      removeProductToCart: 'removeProductToCart'
    }),
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/shop/login?redirect=${this.$route.fullPath}`)
    },
    login() {
      if (this.token && !this.isCustomer) {
        this.$router.push(`/shop/login?redirect=${this.$route.fullPath}`)
      }
    },
    genTotalSP() {
      return this.cart.reduce((sum, od) => sum + Number(od.quantity), 0)
    },
    pushSearchProduct() {
      this.$router.push({
        name: 'filter-product',
        params: {
          searchProduct: this.searchProduct
        }
      })
    },
    genQuantityPurchased(productDetailId) {
      console.log(this.cart.find(e => e.productDetailId === productDetailId))
      if (this.cart.length > 0) {
        return this.cart.find(e => e.productDetailId === productDetailId).quantity
      } else {
        return 0
      }
    },
    genTotalMoneyProductNet(productDetailId, priceNet) {
      if (this.cart.length > 0) {
        return this.cart.find(e => e.productDetailId === productDetailId).quantity * priceNet
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
    removeProduct(productDetailId) {
      this.removeProductToCart(productDetailId)
    },
    getListProductDetail() {
      console.log('ahshhsa')
      getAllProductDetailByListId({
        listProductDetailId: this.cart.map(e => e.productDetailId)
      }).then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.productInCart = res.data
          console.log(res.data)
        }
      })
    },
    pay() {
      if (!this.isCustomer) {
        this.$confirm('Để tiến hành mua hàng, bạn cần đăng nhập?', 'Thông báo.', {
          confirmButtonText: 'Đăng nhập',
          cancelButtonText: 'Hủy',
          type: 'warning'
        }).then(() => {
          this.showCart = false
          this.$router.push({
            path: '/shop/pay'
          })
        })
      } else {
        this.showCart = false
        this.$router.push({
          path: '/shop/pay'
        })
      }
    },
    toHome() {
      this.$router.push({
        path: '/shop'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
header {
  height: 20vh;
  background-color: #11A983;

  .header-1 {
    height: 15%;
    padding: 5px 12% 5px 12%;
    display: flex;
    align-items: center;
    color: #ffffff;
    font-size: 12px;
    justify-content: end;

    .menu {
      height: 100%;
      padding-left: 10px;
      padding-right: 10px;
      border-right: 1px solid #ffffff;
      display: flex;
      align-items: center;
      font-size: 12px;
      cursor: pointer;
    }
  }

  .header-2 {
    height: 55%;
    padding: 5px 12% 5px 12%;
    display: flex;
    align-items: center;
    color: #ffffff;
    justify-content: space-between;

    .input-search {
      outline: none;
      border: none;
      padding: 6px 20px 6px 20px;
      width: 500px;
    }

    .button-search {
      margin: 5px;
      padding: 6px;
      border: none;
      background-color: #008D6A;
      color: #ffffff;
      border-radius: 4px;
    }

    .cart {
      display: flex;
      align-items: center;

      [class^=el-icon-], [class*=" el-icon-"] {
        // font-weight: bold !important;
        font-size: 25px !important;
      }
    }
  }

  .header-3 {
    background-color: #008D6A;
    height: 30%;
    padding: 0 12% 0 12%;
    display: flex;
    align-items: center;
    color: #ffffff;

    .menu {
      height: 100%;
      padding-left: 20px;
      padding-right: 20px;
      border-right: 1px solid #11A983;
      display: flex;
      align-items: center;
      font-size: 13px;
      font-weight: bold;
      cursor: pointer;
    }
  }
}

footer {
  .footer-1 {
    background-color: #202020;
    padding: 30px 12% 20px 12%;
  }

  .footer-2 {
    background-color: #121212;
    padding: 15px 12% 15px 12%;
  }
}
</style>
