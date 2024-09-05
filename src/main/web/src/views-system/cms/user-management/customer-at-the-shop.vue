<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Danh sách Người dùng</h4>
        <span style="font-size: 14px;">Quản lý Người dùng</span>
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
              v-model="listQuery.type"
              style="margin-left: 10px;"
              placeholder=""
              @change="getList()"
            >
              <el-option
                v-for="s in listType"
                :key="s.value"
                :label="s.label"
                :value="s.value"
              />
            </el-select>

            <el-select
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

        <el-table-column label="Tài khoản" prop="userName" sortable="custom" align="center" />

        <el-table-column label="Họ tên" prop="fullName" sortable="custom" align="center" />

        <el-table-column label="Email" prop="email" sortable="custom" align="center" />

        <el-table-column label="SĐT" prop="phoneNumber" sortable="custom" align="center" />

        <el-table-column label="Vai trò" prop="roles" align="center" width="150">
          <template slot-scope="{row}">
            <span v-if="row.roles[0].name === 'ROLE_CUSTOMER'">Khách hàng</span>
            <span v-if="row.roles[0].name === 'ROLE_STAFF_SALES'">Nhân viên bán hàng</span>
            <span v-if="row.roles[0].name === 'ROLE_STAFF_WAREHOUSE_MANAGEMENT'">Quản lý kho</span>
            <span v-if="row.roles[0].name === 'ROLE_SUPER_ADMIN'">Quản lý</span>
          </template>
        </el-table-column>

        <el-table-column label="Trạng thái" prop="status" align="center" width="150">
          <template slot-scope="{row}">
            <span v-if="row.status === 'ACTIVE'">
              <el-tag type="success" size="small">Hoạt động</el-tag>
            </span>
            <span v-if="row.status === 'IN_ACTIVE'">
              <el-tag type="danger" size="small">Ngừng hoạt động</el-tag>
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
        :title="user.id ? 'Chỉnh sửa' : 'Thêm mới'"
        :visible.sync="dialogFormVisible"
        :close-on-click-modal="false"
        @closed="closeModal()"
      >
        <el-form
          ref="dataForm"
          :model="user"
          label-position="left"
          :v-model="user"
          :rules="rules"
        >
          <el-form-item label="Họ tên">
            <el-input v-model="user.fullName" />
          </el-form-item>
          <el-form-item label="Tài khoản">
            <el-input v-model="user.userName" />
          </el-form-item>
          <el-form-item v-if="!user.id" label="Mật khẩu">
            <el-input v-model="user.password" show-password />
          </el-form-item>
          <el-form-item label="Email">
            <el-input v-model="user.email" />
          </el-form-item>
          <el-form-item label="SĐT">
            <el-input v-model="user.phoneNumber" />
          </el-form-item>
          <el-form-item label="Địa chỉ">
            <el-input v-model="user.address" />
          </el-form-item>
          <el-form-item label="Vài trò">
            <el-select v-model="role" style="width: 100%;">
              <el-option
                v-for="item in listRoles"
                :key="item.id"
                :label="item.description"
                :value="item.id"
              >
                <!-- <span v-if="item.name === 'ROLE_CUSTOMER'">Khách hàng</span>
                <span v-if="item.name === 'ROLE_SALES'">Nhân viên bán hàng</span>
                <span v-if="item.name === 'ROLE_WAREHOUSE_MANAGEMENT'">Quản lý kho</span>
                <span v-if="item.name === 'ROLE_MODERATOR'">Quản lý</span> -->
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>

        <div v-if="user.id && user.roles[0].name !== 'ROLE_CUSTOMER'" slot="footer" class="dialog-footer">
          <el-button @click="closeModal()">
            Hủy
          </el-button>
          <el-button type="primary" @click="edit()">
            {{ user.id ? 'Chỉnh sửa' : 'Thêm mới' }}
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { userGetAllPage, userCreate, userUpdate, userGetById } from '@/api/auth'
import { getAllRole } from '@/api/auth'
import Pagination from '@/components/Pagination'
import { ResponseCode, Status } from '@/enums/enums'
import { parseTime } from '@/utils'
import moment from 'moment'

export default {
  name: 'ProductManagementuserListPage',
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
        status: Status.ALL,
        type: 'ALL'
      },
      sortDefault: {
        prop: 'createdAt',
        order: 'descending'
      },
      listStatus: [
        { value: Status.ALL, label: 'Chọn Trạng thái' },
        { value: Status.ACTIVE, label: 'Hoạt động' },
        { value: Status.IN_ACTIVE, label: 'Ngừng hoạt động' }
      ],
      listType: [
        { value: Status.ALL, label: 'Chọn Loại tài khoản' },
        { value: 'ADMIN', label: 'Nhân viên' },
        { value: 'CUSTOMER', label: 'Khách hàng' }
      ],
      dialogFormVisible: false,
      downloadLoading: false,
      listRole: [],
      role: null,
      user: {
        id: '',
        userName: '',
        password: '',
        email: '',
        phoneNumber: '',
        address: '',
        avatar: '',
        fullName: '',
        createdAt: new Date(),
        status: Status.ACTIVE,
        roles: []
      }
      // rules: {
      //   name: [
      //     { required: true, message: 'Không được để trống', trigger: 'blur' }
      //   ],
      //   description: [
      //     { required: true, message: 'Không được để trống', trigger: 'blur' }
      //   ],
      //   status: [
      //     { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
      //   ]
      // }
    }
  },
  watch: {
  },
  created() {
    this.listQuery.sortBy = this.sortDefault.prop
    this.listQuery.sortDesc = this.sortDefault.order === 'descending'
    this.getList()

    getAllRole().then(res => {
      if (res && res.code === ResponseCode.CODE_SUCCESS) {
        this.listRoles = res.data
      }
    })
  },
  mounted() {
  },
  destroyed() {
  },
  methods: {
    getList() {
      this.listLoading = true
      userGetAllPage(this.listQuery).then(res => {
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
      userGetById(id).then(res => {
        this.dialogFormVisible = true
        this.user = res.data
        this.role = res.data.roles[0].id
      })
    },
    closeModal() {
      this.dialogFormVisible = false
      this.$refs['dataForm'].clearValidate()
      this.user = {
        id: '',
        userName: '',
        password: '',
        email: '',
        phoneNumber: '',
        address: '',
        avatar: '',
        fullName: '',
        createdAt: new Date(),
        status: Status.ACTIVE,
        roles: []
      }
    },
    edit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.user.id) {
            this.$confirm('Bạn xác nhận cập nhật?', 'Xác nhận cập nhật', {
              confirmButtonText: 'Xác nhận',
              cancelButtonText: 'Hủy',
              type: 'warning'
            }).then(() => {
              this.user.roles = [this.listRoles.find(e => e.id === this.role)]
              userUpdate(this.user).then(res => {
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
              this.user.roles.push(this.listRoles.find(e => e.id === this.role))
              userCreate(this.user).then(res => {
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
      userGetById(id).then(res1 => {
        if (res1 && res1.code === ResponseCode.CODE_SUCCESS) {
          this.$confirm('Chuyển trạng thái thành: "Ngừng hoạt động", bạn chắc chắn chứ?', 'Xác nhận xóa', {
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy',
            type: 'warning'
          }).then(() => {
            res1.data.status = Status.IN_ACTIVE
            userUpdate(res1.data).then(res2 => {
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
