<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Danh sách Sản phẩm</h4>
        <span style="font-size: 14px;">Quản lý Sản phẩm</span>
      </div>

      <el-button type="primary" icon="el-icon-circle-plus-outline" style="margin-left: 10px;" @click="toPage('/admin/product-management/product/detail', null)">
        Thêm mới
      </el-button>
    </div>

    <div class="container-form-table">
      <div>
        <el-form @submit.native.prevent>
          <el-form-item>
            <el-input
              v-model="listQuery.filter"
              placeholder="Tìm kiếm"
              style="width: 200px"
              @keyup.enter.native="getList()"
            >
              <i slot="suffix" class="el-icon-search" style="cursor: pointer;" @click="getList()" />
            </el-input>

            <!-- <el-select
              v-model="listQuery.status"
              style="margin-left: 10px;"
              placeholder=""
              @change="getList()"
            >
              <el-option
                v-for="s in listStatus"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select> -->

            <el-button style="padding-left: 10px; padding-right: 10px; float: right;" @click="isBtnFilter = !isBtnFilter">
              <i class="el-icon-more" />
            </el-button>

            <!-- <el-button style="margin-left: 10px;" @click="getList()">
              Tìm kiếm
              <i class="el-icon-search" />
            </el-button> -->

            <!-- <el-button :loading="downloadLoading" type="primary" style="margin-left: 10px;" @click="exportFileExcel()">
              Xuất excel
              <i class="el-icon-news" />
            </el-button> -->
          </el-form-item>

          <el-form-item v-if="isBtnFilter">
            <el-select
              v-model="listQuery.categoryId"
              placeholder=""
              style="width: 200px;"
              @change="getList()"
            >
              <el-option
                v-for="c in listCategory"
                :key="c.id"
                :label="c.name"
                :value="c.id"
              />
            </el-select>

            <el-select
              v-model="listQuery.brandId"
              placeholder=""
              style="width: 200px; margin-left: 10px;"
              @change="getList()"
            >
              <el-option
                v-for="b in listBrand"
                :key="b.id"
                :label="b.name"
                :value="b.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item v-if="isBtnFilter">
            <el-select
              v-model="listQuery.sizeId"
              placeholder=""
              style="width: 200px;"
              @change="getList()"
            >
              <el-option
                v-for="s in listSize"
                :key="s.id"
                :label="s.name"
                :value="s.id"
              />
            </el-select>

            <el-select
              v-model="listQuery.colorId"
              placeholder=""
              style="width: 200px; margin-left: 10px;"
              @change="getList()"
            >
              <el-option
                v-for="c in listColor"
                :key="c.id"
                :label="c.name"
                :value="c.id"
              />
            </el-select>

            <el-select
              v-model="listQuery.materialId"
              placeholder=""
              style="width: 200px; margin-left: 10px;"
              @change="getList()"
            >
              <el-option
                v-for="m in listMaterial"
                :key="m.id"
                :label="m.name"
                :value="m.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item v-if="isBtnFilter">
            <el-select
              v-model="listQuery.status"
              placeholder=""
              style="width: 200px;"
              @change="getList()"
            >
              <el-option
                v-for="s in listStatus"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>

          <el-form-item v-if="false">
            <div style="display: flex; align-items: center;">
              <el-input placeholder="Giá tiền" style="margin-right: 10px; width: 200px;" />
              ~
              <el-input placeholder="Giá tiền" style="margin-left: 10px; width: 200px;" />
            </div>
            <!-- <el-slider
              style="margin: 0 10px 0 10px;"
              range
              :step="1000"
              :max="500000"
            /> -->
          </el-form-item>
        </el-form>
      </div>

      <el-divider />

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        style="width: 100%;"
        :default-sort="sortDefault"
        @sort-change="sortChange"
      >
        <el-table-column label="Stt" align="center" width="100">
          <template slot-scope="scope">
            <span>#{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Tên" prop="name" sortable="custom" align="center" width="200" />

        <el-table-column label="Danh mục" prop="categoryName" align="center" width="130" />

        <el-table-column label="Thương hiệu" prop="brandName" align="center" width="130" />

        <el-table-column label="Kích cỡ" prop="listSizeString" align="center" width="200" />

        <el-table-column label="Màu sắc" prop="listColorString" align="center" width="200" />

        <el-table-column label="Chất liệu" prop="listMaterialString" align="center" width="200" />

        <el-table-column label="Mô tả" prop="description" align="center" width="200" />

        <el-table-column label="Thời gian tạo" prop="createdAt" sortable="custom" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ moment(row.createdAt).format('HH:mm:ss DD-MM-YYYY') }}</span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="Trạng thái" prop="status" align="center" width="150">
          <template slot-scope="{row}">
            <span v-if="row.status === 'ACTIVE'">
              <el-tag type="success" size="small">Kinh doanh</el-tag>
            </span>
            <span v-if="row.status === 'IN_ACTIVE'">
              <el-tag type="danger" size="small">Ngừng kinh doanh</el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="" align="center" width="150">
          <template slot-scope="{row}">
            <el-button type="primary" size="small" plain @click="toPage('/admin/product-management/product/detail', { id: row.id })">
              <i class="el-icon-edit" />
            </el-button>

            <el-button type="danger" size="small" plain @click="openConfirmDelete(row.id)">
              <i class="el-icon-delete" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.perPage" @pagination="getList" />
    </div>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { ResponseCode, Status } from '@/enums/enums'
import { parseTime } from '@/utils'
import moment from 'moment'
import { brandGetAll } from '@/api/brand'
import { categoryGetAll } from '@/api/category'
import { colorGetAll } from '@/api/color'
import { sizeGetAll } from '@/api/size'
import { materialGetAll } from '@/api/material'
import { productGetAllPage, productGetById, productUpdateStatusInActiveById } from '@/api/product'

export default {
  name: 'ProductManagementProductListPage',
  components: { Pagination },
  data() {
    return {
      moment: moment,
      isBtnFilter: false,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        currentPage: 1,
        perPage: 10,
        filter: '',
        sortBy: '',
        sortDesc: '',
        status: Status.ALL,
        categoryId: 'ALL',
        brandId: 'ALL',
        sizeId: 'ALL',
        colorId: 'ALL',
        materialId: 'ALL',
        minPrice: 0,
        maxPrice: 500000
      },
      sortDefault: {
        prop: 'createdAt',
        order: 'descending'
      },
      listCategory: [],
      listBrand: [],
      listColor: [],
      listSize: [],
      listMaterial: [],
      listStatus: [
        { value: Status.ALL, label: 'Chọn Trạng thái' },
        { value: Status.ACTIVE, label: 'Kinh doanh' },
        { value: Status.IN_ACTIVE, label: 'Ngừng kinh doanh' }
      ],
      dialogFormVisible: false,
      downloadLoading: false,
      product: {
        id: null,
        name: null,
        description: null,
        createdAt: null,
        updatedAt: null,
        createdBy: null,
        updatedBy: null,
        status: Status.ACTIVE
      },
      rules: {
        name: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        description: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        status: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
  },
  created() {
    this.listQuery.sortBy = this.sortDefault.prop
    this.listQuery.sortDesc = this.sortDefault.order === 'descending'
    this.getAllListBrand()
    this.getAllListCategory()
    this.getAllListColor()
    this.getAllListMaterial()
    this.getAllListSize()
    this.getList()
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    getAllListCategory() {
      categoryGetAll().then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.listCategory = res.data
          this.listCategory.unshift({
            id: 'ALL',
            name: 'Chọn Danh mục'
          })
        }
      })
    },
    getAllListBrand() {
      brandGetAll().then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.listBrand = res.data
          this.listBrand.unshift({
            id: 'ALL',
            name: 'Chọn Thương hiệu'
          })
        }
      })
    },
    getAllListSize() {
      sizeGetAll().then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.listSize = res.data
          this.listSize.unshift({
            id: 'ALL',
            name: 'Chọn Kích cỡ'
          })
        }
      })
    },
    getAllListColor() {
      colorGetAll().then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.listColor = res.data
          this.listColor.unshift({
            id: 'ALL',
            name: 'Chọn Màu sắc'
          })
        }
      })
    },
    getAllListMaterial() {
      materialGetAll().then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          this.listMaterial = res.data
          this.listMaterial.unshift({
            id: 'ALL',
            name: 'Chọn Chất liệu'
          })
        }
      })
    },
    getList() {
      this.listLoading = true
      productGetAllPage(this.listQuery).then(res => {
        setTimeout(() => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            this.listLoading = false
            this.list = res.items
            this.total = res.totalRows
          }
        }, 500)
      })
    },
    sortChange(sortChange) {
      this.listQuery.sortBy = sortChange.prop
      this.listQuery.sortDesc = sortChange.order === 'descending'
      this.getList()
    },
    openConfirmDelete(id) {
      productGetById(id).then(res1 => {
        if (res1 && res1.code === ResponseCode.CODE_SUCCESS) {
          this.$confirm('Chuyển trạng thái thành: "Ngừng kinh doanh", bạn chắc chắn chứ?', 'Xác nhận xóa', {
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy',
            type: 'warning'
          }).then(() => {
            productUpdateStatusInActiveById(res1.data.id).then(res2 => {
              if (res2 && res2.code === ResponseCode.CODE_SUCCESS) {
                this.$message({
                  showClose: true,
                  message: 'Cập nhật thành công!',
                  type: 'success'
                })
                this.getList()
              }
            })
          })
        }
      })
    },
    exportFileExcel() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['name', 'description', 'createdAt', 'status']
        const filterVal = ['name', 'description', 'createdAt', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    toPage(path, query) {
      this.$router.push({
        path: path,
        query: query
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
}
</style>
