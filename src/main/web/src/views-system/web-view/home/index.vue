<template>
  <div>
    <div style="font-size: 12px; margin-bottom: 10px;">TRANG CHỦ</div>
    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="tag-product">
            <div>SẢN PHẨM BÁN CHẠY</div>
            <el-divider class="custom-divider" />
          </div>
        </el-col>

        <el-col :span="18">
          <div class="tag-product">
            <div>SẢN PHẨM MỚI</div>
            <el-divider class="custom-divider" />
            <div style="margin-top: 20px;">
              <el-row :gutter="40">
                <el-col v-for="p in listProductTop5" :key="p.id" :span="6" class="image-container" style="margin-bottom: 40px; cursor: pointer;">
                  <div @click="toProduct(p.id)">
                    <div>
                      <img
                        :src="p.listImage[0]"
                        style="width: 100%; height: 100%;"
                      >

                      <!-- <div class="combo-button">
                        <el-row>
                          <el-col :span="8">
                            c
                          </el-col>
                          <el-col :span="8">
                            c
                          </el-col>
                          <el-col :span="8">
                            c
                          </el-col>
                        </el-row>
                      </div> -->
                    </div>
                    <div style="margin-top: 10px; font-size: 15px;">{{ p.name }}</div>
                    <div style="margin-top: 5px;">
                      <el-rate value="4" disabled />
                    </div>
                    <div style="margin-top: 5px; font-weight: bold; font-size: 15px;">
                      <span>{{ formatCurrencyVND(p.priceNet) }}</span>
                      <span v-if="p.price > p.priceNet" style="text-decoration: line-through; color: rgba(0,0,0,.26); margin-left: 10px;"> {{ formatCurrencyVND(p.price) }}</span>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { productGetTop5ProductCreatedAtDESC } from '@/api/product'
import { ResponseCode } from '@/enums/enums'
import { formatCurrencyVND } from '@/utils/format'

export default {
  name: 'HomePage',
  components: { },
  data() {
    return {
      formatCurrencyVND: formatCurrencyVND,
      listProductTop5: []
    }
  },
  watch: {
  },
  created() {
    console.log(this.$route)

    productGetTop5ProductCreatedAtDESC().then(res => {
      console.log(res)
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listProductTop5 = res.data
      }
    })
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    toProduct(id) {
      console.log('đasadas')
      this.$router.push({
        path: '/shop/product/' + id
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
