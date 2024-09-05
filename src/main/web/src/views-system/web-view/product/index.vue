<template>
  <div>
    <div style="font-size: 12px; margin-bottom: 10px;">TRANG CHỦ</div>
    <div v-if="product">
      <div class="tag-product">
        <el-row :gutter="60">
          <el-col :span="10">
            <el-image style="width: 100%; border: 1px solid #e5e5e5;" :src="product.listImage[0]" />
          </el-col>

          <el-col :span="14">
            <div style="font-size: 16px; font-weight: bold;">
              {{ product.name }}
            </div>

            <div style="margin-top: 10px; display: flex; align-items: center; font-size: 14px;">
              <el-rate
                :value="product ? product.rate : 0"
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                text-color="#ff9900"
                disabled
              />
              {{ product ? product.rate + '/5' : '0/5' }}
              <div style="border-left: 1px solid gray; margin-left: 20px; padding-left: 20px;">
                {{ product ? product.quantitySold : 0 }} Đã bán
              </div>
            </div>

            <div style="margin-top: 10px; font-size: 18px; font-weight: bold;">
              <div v-if="productDetail">
                <span style="color: #ee4d2d;">{{ formatCurrencyVND(productDetail.priceNet) }}</span>
                <span v-if="productDetail.price > productDetail.priceNet" style="text-decoration: line-through; color: rgba(0,0,0,.26); margin-left: 10px; font-size: 16px;">{{ formatCurrencyVND(productDetail.price) }}</span>
              </div>

              <div v-else>
                <span style="color: #ee4d2d;">{{ formatCurrencyVND(product.priceNet) }}</span>
                <span v-if="product.price > product.priceNet" style="text-decoration: line-through; color: rgba(0,0,0,.26); margin-left: 10px; font-size: 16px;">{{ formatCurrencyVND(product.price) }}</span>
              </div>
            </div>

            <div style="margin-top: 20px;">
              <span style="font-size: 14px;">Kích cỡ:</span>
              <div style="display: flex; margin-top: 5px; font-size: 14px;">
                <div v-for="s in listSize" :key="s.id">
                  <div
                    style="margin-right: 10px;"
                    :class="'tag ' + (sizeId === s.id ? 'tag-active' : '')"
                    @click="selectVariant(s.id, 'size')"
                  >
                    {{ s.name }}
                  </div>
                </div>
              </div>
            </div>

            <div style="margin-top: 10px;">
              <span style="font-size: 14px;">Màu sắc:</span>
              <div style="display: flex; margin-top: 5px; font-size: 14px;">
                <div v-for="c in listColor" :key="c.id">
                  <div
                    style="margin-right: 10px;"
                    :class="'tag ' + (colorId === c.id ? 'tag-active' : '')"
                    @click="selectVariant(c.id, 'color')"
                  >
                    {{ c.name }}
                  </div>
                </div>
              </div>
            </div>

            <div style="margin-top: 10px;">
              <span style="font-size: 14px;">Chất Liệu:</span>
              <div style="display: flex; margin-top: 5px; font-size: 14px;">
                <div v-for="m in listMaterial" :key="m.id">
                  <div
                    style="margin-right: 10px;"
                    :class="'tag ' + (materialId === m.id ? 'tag-active' : '')"
                    @click="selectVariant(m.id, 'material')"
                  >
                    {{ m.name }}
                  </div>
                </div>
              </div>
            </div>

            <div style="margin-top: 10px;">
              <span style="font-size: 14px;">Số lượng:</span>
              <div style="margin-top: 5px;">
                <div style="display: flex; align-items: center">
                  <el-button
                    plain
                    type="info"
                    style="padding-left: 4px; padding-right: 4px;"
                    @click="--quantityPurchased"
                  >
                    <i class="el-icon-minus" />
                  </el-button>
                  <el-input
                    v-model="quantityPurchased"
                    style="text-align: center !important; margin-left: 4px; margin-right: 4px; width: 70px; border-radius: 0 !important;"
                    class="input-text-align-center input-quantity"
                  />
                  <el-button
                    plain
                    type="info"
                    style="padding-left: 4px; padding-right: 4px;"
                    @click="++quantityPurchased"
                  >
                    <i class="el-icon-plus" />
                  </el-button>
                  <span style="margin-left: 20px; font-size: 14px;">{{ totalQuantity }} sản phẩm có sẵn</span>
                </div>
              </div>
            </div>
            <div style="margin-top: 20px; padding: 10px 0;">
              <span style="padding: 10px 25px; background-color: #11A983; color: #fff; border-radius: 4px; font-size: 18px; cursor: pointer;" @click="addToCart()">
                <i class="el-icon-shopping-cart-full" />
                Thêm vào giỏ hàng
              </span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import { productGetAllProductDetailView } from '@/api/product'
import { getProductDetailByAttributes } from '@/api/product-detail'
import { ResponseCode } from '@/enums/enums'
import { formatCurrencyVND } from '@/utils/format'
import moment from 'moment'
import { mapActions } from 'vuex'

export default {
  name: 'ProductViewPage',
  components: { },
  data() {
    return {
      formatCurrencyVND: formatCurrencyVND,
      Moment: moment,
      productId: this.$route.params.id ? this.$route.params.id : '',
      listOfFeedbacks: [],
      product: null,
      listProductDetail: [],
      productDetail: null,
      listSize: [],
      listColor: [],
      listMaterial: [],
      listSizeActive: [],
      listColorActive: [],
      listMaterialActive: [],
      sizeId: '',
      colorId: '',
      materialId: '',
      totalQuantity: 0,
      quantityPurchased: 1,
      maxPrice: 0,
      minPrice: 0
    }
  },
  watch: {
  },
  async created() {
    if (this.productId) {
      await productGetAllProductDetailView(this.productId).then((res) => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.product = res.data
          this.listProductDetail = res.data.listProductDetail

          res.data.listProductDetail.forEach(async(pd) => {
            if (!this.listSize.find((x) => x.id === pd.size.id)) {
              this.listSize.push(pd.size)
              // this.listSizeActive.push(pd.size)
            }

            if (!this.listColor.find((x) => x.id === pd.color.id)) {
              this.listColor.push(pd.color)
              // this.listColorActive.push(pd.color)
            }

            if (!this.listMaterial.find((x) => x.id === pd.material.id)) {
              this.listMaterial.push(pd.material)
              // this.listMaterialActive.push(pd.material)
            }

            this.totalQuantity += pd.quantity
          })

          this.maxPrice = res.data.listProductDetail.reduce((max, item) => item.priceNet > max.priceNet ? item : max)
          this.minPrice = res.data.listProductDetail.reduce((min, item) => item.priceNet < min.priceNet ? item : min)
        }
      })
    }
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    ...mapActions([
      'addProductToCart'
    ]),
    selectVariant(id, variant) {
      // const s = []
      // const c = []
      // const m = []
      this.totalQuantity = 0

      if (variant === 'size') {
        this.sizeId = this.sizeId === id ? '' : id
        // if (this.productDetail.sizeId) {
        //   s.push({ id: this.productDetail.sizeId })
        // }
      }

      if (variant === 'color') {
        this.colorId = this.colorId === id ? '' : id
        // if (this.productDetail.colorId) {
        //   c.push({ id: this.productDetail.colorId })
        // }
      }

      if (variant === 'material') {
        this.materialId = this.materialId === id ? '' : id
        // if (this.productDetail.materialId) {
        //   m.push({ id: this.productDetail.materialId })
        // }
      }

      const list = this.listProductDetail.filter((pd) =>
        (pd.size.id === this.sizeId || this.sizeId === '') &&
        (pd.color.id === this.colorId || this.colorId === '') &&
        (pd.material.id === this.materialId || this.materialId === '')
      )
      list.forEach((pd) => {
        // if (!s.find((x) => x.id === pd.size.id)) {
        //   s.push(pd.size)
        // }

        // if (!c.find((x) => x.id === pd.color.id)) {
        //   c.push(pd.color)
        // }

        // if (!m.find((x) => x.id === pd.material.id)) {
        //   m.push(pd.material)
        // }

        this.totalQuantity += pd.quantity
      })

      if (this.materialId && this.sizeId && this.colorId && list.length === 1) {
        this.productDetail = list[0]
      } else {
        this.productDetail = null
      }
      // this.listSizeActive = s
      // this.listColorActive = c
      // this.listMaterialActive = m
    },
    addToCart() {
      if (this.sizeId && this.colorId && this.materialId) {
        if (this.quantityPurchased <= 0) {
          this.$notify({
            title: 'Cảnh báo.',
            message: 'Chọn số lượng sản phẩm.',
            type: 'error',
            offset: 100
          })
          return
        }
        if (this.quantityPurchased > this.totalQuantity) {
          this.$notify({
            title: 'Cảnh báo.',
            message: 'Số lượng sản phẩm còn lại không đủ.',
            type: 'error',
            offset: 100
          })
        } else {
          getProductDetailByAttributes({
            productId: this.productId,
            sizeId: this.sizeId,
            colorId: this.colorId,
            materialId: this.materialId
          }).then(res => {
            if (res && res.code === ResponseCode.CODE_SUCCESS) {
              this.addProductToCart({
                productDetailId: res.data.id,
                quantity: this.quantityPurchased
              })
              this.$notify({
                title: 'Thông báo.',
                message: 'Sản phẩm đã được thêm vào giỏ hàng.',
                type: 'success',
                offset: 100
              })
            }
          })
        }
      } else {
        this.$notify({
          title: 'Cảnh báo.',
          message: 'Bạn cần chọn các thuộc tính của sản phẩm.',
          type: 'error',
          offset: 100
        })
      }
    },
    loadImageAndPrice() {
      if (this.productId && this.sizeSelected && this.colorSelected && this.materialSelected) {
        getProductDetailByAttributes({
          productId: this.productId,
          sizeId: this.sizeSelected,
          colorId: this.colorSelected,
          materialId: this.materialSelected
        }).then(res => {
          if (res && res.data && res.code === ResponseCode.CODE_SUCCESS) {
            this.pd = res.data
          }
        })
      } else {
        this.pd = null
      }
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
