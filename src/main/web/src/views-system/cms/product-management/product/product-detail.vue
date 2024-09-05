<template>
  <div v-loading="isLoading" class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Chi tiết Sản phẩm</h4>
        <span style="font-size: 14px;">{{ product.id ? 'Cập nhật' : 'Thêm mới' }} Sản phẩm</span>
      </div>

      <div>
        <el-button v-if="product.id ? true : false" type="danger" icon="el-icon-delete" style="margin-left: 10px;" @click="edit">
          Ngừng kinh doanh
        </el-button>

        <el-button type="primary" :icon="product.id ? 'el-icon-edit' : 'el-icon-circle-plus-outline'" style="margin-left: 10px;" @click="edit">
          {{ product.id ? 'Cập nhật' : 'Thêm mới' }}
        </el-button>
      </div>
    </div>

    <div class="container-form-table" style="padding-top: 0; padding-bottom: 20px">
      <el-collapse v-model="activeNames" style="border-top: 0;">
        <el-collapse-item name="product-information">
          <template slot="title">
            <i class="el-icon-info" />
            <span style="font-size: 14px; font-weight: bold;"> Thông tin Sản phẩm</span>
          </template>

          <el-divider />

          <el-form
            ref="dataFormProduct"
            label-position="left"
            style="margin-top: 20px;"
            :model="product"
            :rules="rulesProduct"
          >
            <el-row :gutter="40">
              <el-col :span="8">
                <el-form-item prop="name">
                  <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Tên sản phẩm</div>
                  <el-input v-model="product.name" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="40">
              <el-col :span="8">
                <el-form-item prop="price">
                  <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Giá gốc</div>
                  <el-input v-model="product.price" />
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item prop="priceNet">
                  <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Giá giảm</div>
                  <el-input v-model="product.priceNet" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="40">
              <el-col :span="8">
                <el-form-item prop="categoryId">
                  <div style="font-weight: bold;">
                    <div style="float: left;"><span style="color: red;">*</span>Danh mục</div>
                    <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" />Thêm mới</div>
                  </div>

                  <el-select
                    v-model="product.categoryId"
                    style="width: 100%;"
                    placeholder="Chọn Danh mục"
                  >
                    <el-option
                      v-for="c in listCategoryActive"
                      :key="c.id"
                      :label="c.name"
                      :value="c.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>

              <el-col :span="8">
                <el-form-item prop="brandId">
                  <div style="font-weight: bold;">
                    <div style="float: left;"><span style="color: red;">*</span>Thương hiệu</div>
                    <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" />Thêm mới</div>
                  </div>

                  <el-select
                    v-model="product.brandId"
                    style="width: 100%;"
                    placeholder="Chọn Thương hiệu"
                  >
                    <el-option
                      v-for="b in listBrandActive"
                      :key="b.id"
                      :label="b.name"
                      :value="b.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item prop="description">
              <div style="font-weight: bold;">
                <div style="float: left;"><span style="color: red;">*</span>Mô tả</div>
              </div>

              <Tinymce ref="editor" v-model="product.description" :height="200" style="margin-top: 40px;" />
            </el-form-item>
          </el-form>
        </el-collapse-item>

        <el-collapse-item name="image">
          <template slot="title">
            <i class="el-icon-info" />
            <span style="font-size: 14px; font-weight: bold;"> Ảnh</span>
          </template>

          <el-divider />

          <el-upload
            ref="imagePUpload"
            action="#"
            list-type="picture-card"
            style="margin-bottom: 20px; margin-top: 20px;"
            :auto-upload="false"
            :file-list="product.listImage"
          >
            <i slot="default" class="el-icon-plus" />
            <div slot="file" slot-scope="{file}">
              <img
                class="el-upload-list__item-thumbnail"
                :src="file.url"
                alt=""
              >
              <span class="el-upload-list__item-actions">
                <span
                  class="el-upload-list__item-preview"
                  @click="handlePictureCardPreview(file)"
                >
                  <i class="el-icon-zoom-in" />
                </span>
                <span
                  class="el-upload-list__item-delete"
                  @click="deletePImage(file, i)"
                >
                  <i class="el-icon-delete" />
                </span>
              </span>
            </div>
          </el-upload>
        </el-collapse-item>

        <el-collapse-item name="variant" class="collapse-item-custom">
          <template slot="title">
            <i class="el-icon-info" />
            <span style="font-size: 14px; font-weight: bold;"> Các loại biến thể Sản phẩm</span>
          </template>

          <el-divider />

          <div v-for="(pd, i) in product.listProductDetail" :key="i" style="position: relative;">
            <div style="margin-top: 20px; margin-bottom: 20px;">
              <el-form
                ref="dataFormPD"
                label-position="left"
                style="margin-top: 20px;"
                :v-model="pd"
                :model="pd"
                :rules="rulesProductDetail"
              >
                <el-row :gutter="40">
                  <el-col :span="10">
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-row :gutter="20">
                          <el-col :span="8">
                            <el-form-item prop="sizeId">
                              <div style="font-weight: bold;">
                                <div style="float: left;"><span style="color: red;">*</span>Kích cỡ</div>
                                <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" /></div>
                              </div>

                              <el-select
                                v-model="pd.sizeId"
                                placeholder="Kích cỡ"
                                style="margin-bottom: 10px;"
                                :disabled="pd.id ? true : false"
                              >
                                <el-option
                                  v-for="s in listSizeActive"
                                  :key="s.id"
                                  :label="s.name"
                                  :value="s.id"
                                  :disabled="hideProperties('size', s.id, pd)"
                                />
                              </el-select>
                            </el-form-item>
                          </el-col>

                          <el-col :span="8">
                            <el-form-item prop="colorId">
                              <div style="font-weight: bold;">
                                <div style="float: left;"><span style="color: red;">*</span>Màu sắc</div>
                                <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" /></div>
                              </div>

                              <el-select
                                v-model="pd.colorId"
                                placeholder="Màu sắc"
                                style="margin-bottom: 10px;"
                                :disabled="pd.id ? true : false"
                              >
                                <el-option
                                  v-for="c in listColorActive"
                                  :key="c.id"
                                  :label="c.name"
                                  :value="c.id"
                                  :disabled="hideProperties('color', c.id, pd)"
                                />
                              </el-select>
                            </el-form-item>
                          </el-col>

                          <el-col :span="8">
                            <el-form-item prop="materialId">
                              <div style="font-weight: bold;">
                                <div style="float: left;"><span style="color: red;">*</span>Chất liệu</div>
                                <div style="float: right; color: #11A983;"><i class="el-icon-circle-plus-outline" /></div>
                              </div>

                              <el-select
                                v-model="pd.materialId"
                                placeholder="Chất liệu"
                                :disabled="pd.id ? true : false"
                              >
                                <el-option
                                  v-for="m in listMaterialActive"
                                  :key="m.id"
                                  :label="m.name"
                                  :value="m.id"
                                  :disabled="hideProperties('material', m.id, pd)"
                                />
                              </el-select>
                            </el-form-item>
                          </el-col>
                        </el-row>
                      </el-col>

                      <el-col :span="24">
                        <el-row :gutter="20">
                          <el-col :span="8">
                            <el-form-item prop="price">
                              <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Giá gốc</div>
                              <el-input v-model="pd.price" />
                            </el-form-item>
                          </el-col>

                          <el-col :span="8">
                            <el-form-item prop="price">
                              <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Giá giảm</div>
                              <el-input v-model="pd.priceNet" />
                            </el-form-item>
                          </el-col>

                          <el-col :span="8">
                            <el-form-item prop="quantity">
                              <div style="float: left; font-weight: bold;"><span style="color: red;">*</span>Số lượng</div>
                              <el-input v-model="pd.quantity" />
                            </el-form-item>
                          </el-col>
                        </el-row>
                      </el-col>
                    </el-row>
                  </el-col>

                  <el-col :span="14">
                    <el-form-item>
                      <div style="float: left; font-weight: bold; width: 100%;">Ảnh</div>
                      <div>
                        <el-upload
                          ref="imagePDUpload"
                          action="#"
                          list-type="picture-card"
                          :auto-upload="false"
                          :file-list="pd.listImage"
                        >
                          <i slot="default" class="el-icon-plus" />
                          <div slot="file" slot-scope="{file}">
                            <img
                              class="el-upload-list__item-thumbnail"
                              :src="file.url"
                              alt=""
                            >
                            <span class="el-upload-list__item-actions">
                              <span
                                class="el-upload-list__item-preview"
                                @click="handlePictureCardPreview(file)"
                              >
                                <i class="el-icon-zoom-in" />
                              </span>
                              <span
                                class="el-upload-list__item-delete"
                                @click="deletePDImage(file, i)"
                              >
                                <i class="el-icon-delete" />
                              </span>
                            </span>
                          </div>
                        </el-upload>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </div>

            <div v-if="!pd.id" style="position: absolute; top: 0; right: 0; bottom: 0">
              <el-button type="danger" icon="el-icon-delete" circle plain @click="deletePD(i)" />
            </div>
            <el-divider />
          </div>

          <el-button style="margin-top: 20px; margin-bottom: 20px;" @click="addProductVariable">Thêm mới</el-button>

          <!-- <el-divider /> -->
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import { brandGetAllStatusActive } from '@/api/brand'
import { categoryGetAllStatusActive } from '@/api/category'
import { colorGetAllStatusActive } from '@/api/color'
import { sizeGetAllStatusActive } from '@/api/size'
import { materialGetAllStatusActive } from '@/api/material'
import { productCreateOrUpdate, productGetById } from '@/api/product'
import { ResponseCode } from '@/enums/enums'
import axios from 'axios'

export default {
  name: 'ProductManagementProductDetailPage',
  components: { Tinymce },
  data() {
    return {
      isLoading: false,
      activeNames: ['product-information', 'variant', 'image'],
      productId: '',
      product: {
        name: '',
        description: '',
        price: 0,
        priceNet: 0,
        brandId: '',
        categoryId: '',
        listProductDetail: [],
        listImage: [],
        listImageDelete: []
      },
      listBrandActive: [],
      listCategoryActive: [],
      listColorActive: [],
      listSizeActive: [],
      listMaterialActive: [],
      totalProductCanCreate: 0,
      totalSizeCanCreate: 0,
      totalColorCanCreate: 0,
      totalMaterialCanCreate: 0,
      rulesProduct: {
        name: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        description: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        price: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        priceNet: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        brandId: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ]
      },
      rulesProductDetail: {
        sizeId: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        colorId: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        materialId: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        quantity: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        price: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        priceNet: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ]
      },
      listImageDelete: []
    }
  },
  watch: {
  },
  async created() {
    this.productId = this.$route.query.id ? this.$route.query.id : ''
    await this.getProductById()
    await brandGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listBrandActive = res.data
      }
    })

    await categoryGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listCategoryActive = res.data
      }
    })

    await colorGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listColorActive = res.data
      }
    })

    await sizeGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listSizeActive = res.data
      }
    })

    await materialGetAllStatusActive().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listMaterialActive = res.data
      }
    })

    this.calculateQuantity()
  },
  mounted() {
    this.productId = this.$route.query.id ? this.$route.query.id : ''
  },
  destroyed() {
  },
  methods: {
    getProductById() {
      if (this.productId) {
        productGetById(this.productId).then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            this.product = res.data
          }
        })
      }
    },
    deletePDImage(file, i) {
      if (!file.raw) {
        this.product.listProductDetail[i].listImageDelete.push({
          id: file.id,
          publicId: file.publicId,
          url: file.url,
          type: file.type,
          secondaryId: file.secondaryId
        })
      }
      this.$refs.imagePDUpload[i].handleRemove(file)
    },
    deletePImage(file) {
      if (!file.raw) {
        this.product.listImageDelete.push({
          id: file.id,
          publicId: file.publicId,
          url: file.url,
          type: file.type,
          secondaryId: file.secondaryId
        })
      }
      this.$refs.imagePUpload.handleRemove(file)
    },
    calculateQuantity() {
      this.totalProductCanCreate = this.listColorActive.length * this.listMaterialActive.length * this.listSizeActive.length
      this.totalColorCanCreate = this.listMaterialActive.length * this.listSizeActive.length
      this.totalSizeCanCreate = this.listMaterialActive.length * this.listColorActive.length
      this.totalMaterialCanCreate = this.listColorActive.length * this.listSizeActive.length
    },
    hideProperties(properties, idProperties, pd) {
      // if (this.product.listProductDetail && this.listColorActive && this.listMaterialActive && this.listSizeActive) {
      //   return false
      // }
      if (this.product.listProductDetail.length === this.listColorActive.length * this.listMaterialActive.length * this.listSizeActive.length) {
        return true
      }
      if (properties === 'size' && this.product.listProductDetail.find(e => e.sizeId === idProperties && e.colorId === pd.colorId && e.materialId === pd.materialId)) {
        return true
      }
      if (properties === 'color' && this.product.listProductDetail.find(e => e.colorId === idProperties && e.sizeId === pd.sizeId && e.materialId === pd.materialId)) {
        return true
      }
      if (properties === 'material' && this.product.listProductDetail.find(e => e.materialId === idProperties && e.colorId === pd.colorId && e.sizeId === pd.sizeId)) {
        return true
      }
      return false
    },
    addProductVariable() {
      this.product.listProductDetail.push({
        colorId: '',
        sizeId: '',
        materialId: '',
        quantity: 0,
        price: (this.product.price && this.product.price > 0) ? this.product.price : 0,
        priceNet: (this.product.price && this.product.priceNet > 0) ? this.product.priceNet : 0,
        listImage: [],
        listImageDelete: []
      })
    },
    deletePD(i) {
      this.product.listProductDetail.splice(i, 1)
    },
    async edit() {
      this.$confirm(this.product.id ? 'Bạn xác nhận cập nhật?' : 'Bạn xác nhận thêm mới?', this.product.id ? 'Xác nhận cập nhật' : 'Xác nhận thêm mới', {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning'
      }).then(async() => {
        this.isLoading = true

        if (this.$refs.imagePUpload.uploadFiles.length === 0) {
          this.$message({
            showClose: true,
            message: 'Vui lòng thêm ít nhất 1 ảnh cho sản phẩm!',
            type: 'error'
          })
          this.isLoading = false
          return
        }

        if (this.product.listProductDetail.length === 0) {
          this.$message({
            showClose: true,
            message: 'Vui lòng thêm ít nhất 1 biến thể cho sản phẩm!',
            type: 'error'
          })
          this.isLoading = false
          return
        }

        // eslint-disable-next-line no-unused-vars
        var isValidate = true

        this.$refs.dataFormProduct.validate(valid => {
          if (!valid) {
            isValidate = false
            return false
          }
        })

        if (isValidate) {
          for (const image of this.$refs.imagePUpload.uploadFiles) {
            if (image.raw) {
              const data = new FormData()
              data.append('file', image.raw)
              data.append('upload_preset', 'vubq-upload')
              data.append('cloud_name', 'vubq')
              await axios.post('https://api.cloudinary.com/v1_1/vubq/image/upload', data)
                .then((res) => {
                  this.product.listImage.push({
                    publicId: res.data.public_id,
                    url: res.data.url
                  })
                })
            }
          }
        }

        // eslint-disable-next-line no-unused-vars
        for (const [i, e] of this.product.listProductDetail.entries()) {
          this.$refs.dataFormPD[i].validate(valid => {
            if (!valid) {
              isValidate = false
              return false
            }
          })

          if (isValidate) {
            for (const image of this.$refs.imagePDUpload[i].uploadFiles) {
              if (image.raw) {
                const data = new FormData()
                data.append('file', image.raw)
                data.append('upload_preset', 'vubq-upload')
                data.append('cloud_name', 'vubq')
                await axios.post('https://api.cloudinary.com/v1_1/vubq/image/upload', data)
                  .then((res) => {
                    this.product.listProductDetail[i].listImage.push({
                      publicId: res.data.public_id,
                      url: res.data.url
                    })
                  })
              }
            }
          }
        }

        if (isValidate) {
          productCreateOrUpdate(this.product).then(res => {
            if (res && res.code === ResponseCode.CODE_SUCCESS) {
              if (this.product.id) {
                this.$message({
                  showClose: true,
                  message: 'Cập nhật thành công!',
                  type: 'success'
                })
              } else {
                this.$message({
                  showClose: true,
                  message: 'Thêm mới thành công!',
                  type: 'success'
                })
              }

              this.$router.push({
                path: '/admin/product-management/product/detail',
                query: {
                  id: res.data.id
                }
              })
            }
          // eslint-disable-next-line no-return-assign
          }).finally(() => this.isLoading = false)
        } else {
          this.$message({
            showClose: true,
            message: 'Vui lòng nhập đầy đủ thông tin!',
            type: 'error'
          })
          this.isLoading = false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  ::v-deep .el-input__suffix {
    right: 15px;
  }

  ::v-deep .el-divider--horizontal {
    margin-bottom: 0;
    margin-top: 0;
  }

  ::v-deep .el-divider--horizontal {
    height: 0.5px;
  }

  // ::v-deep .collapse-item-custom {
  //   .el-collapse-item__header, .el-collapse-item__wrap {
  //     border-bottom: none;
  //   }
  // }

  ::v-deep .el-collapse-item__content {
    padding-bottom: 0;
  }

  ::v-deep .mce-tinymce {
    box-shadow: none !important;
    border: 1px solid #DCDFE6;
    border-radius: 8px;
  }

  ::v-deep .mce-tinymce {
    width: unset !important;
  }

  ::v-deep .mce-edit-area, .mce-container, .mce-panel, .mce-stack-layout-item {
    border-width: 1px 1px 0px 0px !important;
  }

  ::v-deep .editor-custom-btn-container {
    display: none;
  }

  ::v-deep .mce-branding, .mce-widget, .mce-label, .mce-flow-layout-item, .mce-last {
    display: none !important;
  }

  ::v-deep .mce-statusbar, .mce-container, .mce-panel, .mce-stack-layout-item, .mce-last {
    background-color: transparent;
  }

  ::v-deep .mce-top-part::before {
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.2);
  }

  ::v-deep .mce-edit-area, .mce-container, .mce-panel, .mce-stack-layout-item {
    font-family: "Nunito", sans-serif !important;
  }

  ::v-deep .mce-colorbutton .mce-preview {
    left: 51%;
  }

  ::v-deep .el-upload-list {
    transition: none !important;
  }

  ::v-deep .el-upload-list__item {
    transition: none !important;
  }

  ::v-deep .el-upload-list__item-name,
  .el-upload-list__item-status-label,
  .el-upload-list__item-actions {
    transition: none !important;
  }
  ::v-deep .el-loading-spinner {
    top: 50vh !important;
  }
</style>
