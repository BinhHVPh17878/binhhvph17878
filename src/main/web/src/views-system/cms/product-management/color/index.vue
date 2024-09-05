<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Danh sách Màu sắc</h4>
        <span style="font-size: 14px;">Quản lý Màu sắc</span>
      </div>

      <el-button type="primary" icon="el-icon-circle-plus-outline" style="margin-left: 10px;" @click="dialogFormVisible = true">
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

            <el-select
              v-model="listQuery.status"
              style="margin-left: 10px;"
              placeholder=""
              @change="getList()"
            >
              <el-option
                v-for="s in listStatus"
                v-if="s.value !== 'ALL'"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>

            <!-- <el-button style="margin-left: 10px;" @click="getList()">
              Tìm kiếm
              <i class="el-icon-search" />
            </el-button> -->

            <!-- <el-button :loading="downloadLoading" type="primary" style="margin-left: 10px;" @click="exportFileExcel()">
              Xuất excel
              <i class="el-icon-news" />
            </el-button> -->
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

        <el-table-column label="Tên" prop="name" sortable="custom" align="center" />

        <el-table-column label="Mô tả" prop="description" sortable="custom" align="center" />

        <el-table-column label="Thời gian tạo" prop="createdAt" sortable="custom" align="center">
          <template slot-scope="{row}">
            <span>{{ moment(row.createdAt).format('HH:mm:ss DD-MM-YYYY') }}</span>
          </template>
        </el-table-column>

        <el-table-column label="Trạng thái" prop="status" align="center" width="150">
          <template slot-scope="{row}">
            <span v-if="row.status === 'ACTIVE'">
              <el-tag type="success" size="small">Kinh doanh</el-tag>
            </span>
            <span v-if="row.status === 'IN_ACTIVE'">
              <el-tag type="danger" size="small">Ngừng kinh doanh</el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column label="" align="center" width="150">
          <template slot-scope="{row}">
            <el-button type="primary" size="small" plain @click="openModalEdit(row.id)">
              <i class="el-icon-edit" />
            </el-button>

            <el-button type="danger" size="small" plain @click="openConfirmDelete(row.id)">
              <i class="el-icon-delete" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.perPage" @pagination="getList" />

      <el-dialog
        width="30%"
        :title="color.id ? 'Chỉnh sửa' : 'Thêm mới'"
        :visible.sync="dialogFormVisible"
        :close-on-click-modal="false"
        @closed="closeModal()"
      >
        <el-form
          ref="dataForm"
          :model="color"
          label-position="left"
          :v-model="color"
          :rules="rules"
        >
          <el-form-item label="Tên" prop="name">
            <el-input v-model="color.name" />
          </el-form-item>

          <el-form-item label="Mô tả" prop="description">
            <el-input v-model="color.description" />
          </el-form-item>

          <el-form-item label="Trạng thái" prop="status">
            <el-select v-model="color.status" style="width: 100%;" placeholder="">
              <el-option
                v-for="s in listStatus"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>
          </el-form-item>

          <!-- <el-form-item v-if="color.id && color.createdAt && color.createdBy" style="margin-bottom: 0;">
            <span>Thời gian tạo: {{ moment(color.createdAt).format('HH:mm:ss DD-MM-YYYY') }} (bởi: {{ color.createdBy }})</span>
          </el-form-item>

          <el-form-item v-if="color.id && color.updatedAt && color.updatedBy" style="margin-bottom: 0;">
            <span>Thời gian cập nhật gần nhất: {{ moment(color.updatedAt).format('HH:mm:ss DD-MM-YYYY') }} (bởi: {{ color.updatedBy }})</span>
          </el-form-item> -->
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click="closeModal()">
            Hủy
          </el-button>
          <el-button type="primary" @click="edit()">
            {{ color.id ? 'Chỉnh sửa' : 'Thêm mới' }}
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { colorGetAllPage, colorGetById, colorCreateOrUpdate } from '@/api/color'
import Pagination from '@/components/Pagination'
import { ResponseCode, Status } from '@/enums/enums'
import { parseTime } from '@/utils'
import moment from 'moment'

export default {
  name: 'ProductManagementColorListPage',
  components: { Pagination },
  data() {
    return {
      moment: moment,
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
        status: Status.ALL
      },
      sortDefault: {
        prop: 'createdAt',
        order: 'descending'
      },
      listStatus: [
        { value: Status.ALL, label: 'Chọn Trạng thái' },
        { value: Status.ACTIVE, label: 'Kinh doanh' },
        { value: Status.IN_ACTIVE, label: 'Ngừng kinh doanh' }
      ],
      dialogFormVisible: false,
      downloadLoading: false,
      color: {
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
    this.getList()
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    getList() {
      this.listLoading = true
      colorGetAllPage(this.listQuery).then(res => {
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
    openModalEdit(id) {
      colorGetById(id).then(res => {
        this.dialogFormVisible = true
        this.color = res.data
      })
    },
    closeModal() {
      this.dialogFormVisible = false
      this.$refs['dataForm'].clearValidate()
      this.color = {
        id: null,
        name: null,
        description: null,
        createdAt: null,
        updatedAt: null,
        createdBy: null,
        updatedBy: null,
        status: Status.ACTIVE
      }
    },
    edit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.color.id) {
            this.$confirm('Bạn xác nhận cập nhật?', 'Xác nhận cập nhật', {
              confirmButtonText: 'Xác nhận',
              cancelButtonText: 'Hủy',
              type: 'warning'
            }).then(() => {
              colorCreateOrUpdate(this.color).then(res => {
                if (res && res.code === ResponseCode.CODE_SUCCESS) {
                  this.$message({
                    showClose: true,
                    message: 'Cập nhật thành công!',
                    type: 'success'
                  })
                  this.dialogFormVisible = false
                  this.getList()
                }
              })
            })
          } else {
            this.$confirm('Bạn xác nhận thêm mới?', 'Xác nhận thêm mới', {
              confirmButtonText: 'Xác nhận',
              cancelButtonText: 'Hủy',
              type: 'warning'
            }).then(() => {
              colorCreateOrUpdate(this.color).then(res => {
                if (res && res.code === ResponseCode.CODE_SUCCESS) {
                  this.$message({
                    showClose: true,
                    message: 'Thêm mới thành công!',
                    type: 'success'
                  })
                  this.dialogFormVisible = false
                  this.getList()
                }
              })
            })
          }
        } else {
          return false
        }
      })
    },
    openConfirmDelete(id) {
      colorGetById(id).then(res1 => {
        if (res1 && res1.code === ResponseCode.CODE_SUCCESS) {
          this.$confirm('Chuyển trạng thái thành: "Ngừng kinh doanh", bạn chắc chắn chứ?', 'Xác nhận xóa', {
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy',
            type: 'warning'
          }).then(() => {
            res1.data.status = Status.IN_ACTIVE
            colorCreateOrUpdate(res1.data).then(res2 => {
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
