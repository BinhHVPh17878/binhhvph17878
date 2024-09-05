<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Danh sách Đơn hàng Online</h4>
        <span style="font-size: 14px;">Quản lý Đơn hàng Online</span>
      </div>

      <!-- <el-button type="primary" icon="el-icon-circle-plus-outline" style="margin-left: 10px;" @click="dialogFormVisible = true">
        Thêm mới
      </el-button> -->
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

            <el-date-picker
              v-model="orderDate"
              type="daterange"
              align="right"
              unlink-panels
              range-separator="~"
              start-placeholder="Từ ngày"
              end-placeholder="Tới ngày"
              :clearable="false"
              format="dd-MM-yyyy"
              value-format="yyyy-MM-dd"
              style="width: 300px; margin-left: 10px;"
              @change="changeOrderDate()"
            />

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

        <el-table-column label="Khách hàng" prop="fullName" sortable="custom" align="center" width="200" />

        <el-table-column label="SĐT" prop="phoneNumber" sortable="custom" align="center" width="200" />

        <el-table-column label="Email" prop="email" sortable="custom" align="center" width="200" />

        <el-table-column label="Địa chỉ" prop="address" sortable="custom" align="center" width="200">
          <template slot-scope="{row}">
            <span>
              {{ row.address + ', ' + row.district + ' - ' + row.ward + ' - ' + row.city }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="Thời gian đặt hàng" prop="createdAt" sortable="custom" align="center" width="200">
          <template slot-scope="{row}">
            <span>{{ moment(row.createdAt).format('HH:mm:ss DD-MM-YYYY') }}</span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="Tổng tiền" prop="totalAmountNet" align="center" width="150">
          <template slot-scope="{row}">
            <span>
              {{ formatCurrencyVND(row.totalAmountNet) }}
            </span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="Trạng thái" prop="status" align="center" width="150">
          <template slot-scope="{row}">
            <span v-if="row.status === 'WAIT_FOR_CONFIRMATION'">
              <el-tag type="warning" size="small">Chờ xác nhận</el-tag>
            </span>
            <span v-if="row.status === 'PREPARING_GOODS'">
              <el-tag type="warning" size="small">Chuẩn bị hàng</el-tag>
            </span>
            <span v-if="row.status === 'DELIVERING'">
              <el-tag type="warning" size="small">Đang giao hàng</el-tag>
            </span>
            <span v-if="row.status === 'DELIVERED'">
              <el-tag type="warning" size="small">Đã giao hàng</el-tag>
            </span>
            <span v-if="row.status === 'SUCCESS'">
              <el-tag type="success" size="small">Thành công</el-tag>
            </span>
            <span v-if="row.status === 'CANCELLED'">
              <el-tag type="danger" size="small">Đã hủy</el-tag>
            </span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="" align="center" width="150">
          <template slot-scope="{row}">
            <el-button type="primary" size="small" plain @click="showDetail(row.id)">
              <i class="el-icon-warning" />
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.currentPage" :limit.sync="listQuery.perPage" @pagination="getList" />

      <el-dialog
        v-if="odDetail"
        width="80%"
        title="Chi tiết"
        :visible.sync="dialogFormVisible"
        :close-on-click-modal="false"
        @closed="closeModal()"
      >
        <el-row :gutter="40">
          <el-col :span="6">
            <div>Khách hàng: {{ odDetail.order.fullName }}</div>
            <div style="margin-top: 20px;">SĐT: {{ odDetail.order.phoneNumber }}</div>
            <div style="margin-top: 20px;">Email: {{ odDetail.order.email }}</div>
            <div style="margin-top: 20px;">Địa chỉ: {{ odDetail.order.address + ', ' + odDetail.order.district + ' - ' + odDetail.order.ward + ' - ' + odDetail.order.city }}</div>
          </el-col>
          <el-col :span="6">
            <div>Mã HĐ: {{ odDetail.order.id }}</div>
            <div style="margin-top: 20px;">Tổng tiền gốc: {{ formatCurrencyVND(odDetail.order.totalAmount) }}</div>
            <div v-if="odDetail.voucher" style="margin-top: 20px;">Mã giảm giá: {{ odDetail.voucher.code }} <span>({{ odDetail.voucher.type === 'PERCENT' ? odDetail.voucher.value + '% - tối đa ' + formatCurrencyVND(odDetail.voucher.max) : formatCurrencyVND(odDetail.voucher.value) }})</span></div>
            <div style="margin-top: 20px;">Giảm giá: {{ formatCurrencyVND(odDetail.order.totalAmount - odDetail.order.totalAmountNet) }}</div>
            <div style="margin-top: 20px;">Tổng tiền: {{ formatCurrencyVND(odDetail.order.totalAmountNet) }}</div>
          </el-col>
          <el-col :span="12">
            <div style="display: flex; justify-content: center;">
              Trạng thái đơn hàng
            </div>
            <div style="margin-top: 20px; display: flex; justify-content: center;">
              <el-timeline :reverse="true">
                <el-timeline-item
                  v-for="(h, index) in odDetail.listHistoryOrder"
                  :key="index"
                  :timestamp="moment(h.createdAt).format('HH:mm:ss DD-MM-YYYY')"
                >
                  <span v-if="h.status === 'WAIT_FOR_CONFIRMATION'">
                    <el-tag :type="odDetail.listHistoryOrder.length === 1 ? 'warning' : 'success'" size="small">Chờ xác nhận</el-tag>
                  </span>
                  <span v-if="h.status === 'PREPARING_GOODS'">
                    <el-tag :type="odDetail.listHistoryOrder.length === 2 ? 'warning' : 'success'" size="small">Chuẩn bị hàng</el-tag>
                  </span>
                  <span v-if="h.status === 'DELIVERING'">
                    <el-tag :type="odDetail.listHistoryOrder.length === 3 ? 'warning' : 'success'" size="small">Đang giao hàng</el-tag>
                  </span>
                  <span v-if="h.status === 'DELIVERED'">
                    <el-tag :type="odDetail.listHistoryOrder.length === 4 ? 'warning' : 'success'" size="small">Đã giao hàng</el-tag>
                  </span>
                  <span v-if="h.status === 'SUCCESS'">
                    <el-tag type="success" size="small">Thành công</el-tag>
                  </span>
                  <span v-if="h.status === 'CANCELLED'">
                    <el-tag type="danger" size="small">Đã hủy</el-tag>
                    <div v-if="odDetail.order.cancelBy === 'ADMIN'" style="margin-top: 5px;">Người bán hủy</div>
                    <div v-if="odDetail.order.cancelBy === 'CUSTOMER'" style="margin-top: 5px;">Người mua hủy</div>
                  </span>
                </el-timeline-item>
              </el-timeline>
            </div>
          </el-col>
        </el-row>

        <el-table
          :key="tableKey"
          v-loading="isLoadingModal"
          :data="odDetail.listOrderDetail"
          :header-cell-style="{ background: '#f5f7fa' }"
          border
          style="margin-top: 20px;"
        >
          <el-table-column align="center" width="50">
            <template slot-scope="scope">
              <span>#{{ scope.$index + 1 }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Sản phẩm" align="center">
            <template slot-scope="{row}">
              <span>{{ row.productDetail.product.name }} ({{ row.productDetail.size.name }} - {{ row.productDetail.color.name }} - {{ row.productDetail.material.name }})</span>
            </template>
          </el-table-column>

          <el-table-column label="SL" align="center">
            <template slot-scope="{row}">
              <span>{{ row.quantity }}</span>
            </template>
          </el-table-column>

          <el-table-column label="Đơn giá" align="center">
            <template slot-scope="{row}">
              {{ formatCurrencyVND(row.productDetailPriceNet) }} <span v-if="row.productDetailPrice > row.productDetailPriceNet" style="text-decoration: line-through;">({{ formatCurrencyVND(row.productDetailPrice) }})</span>
            </template>
          </el-table-column>

          <el-table-column :label="'Thành tiền ' + (odDetail.voucher ? '(Cả mã giảm giá)' : '')" align="center">
            <template slot-scope="{row}">
              {{ formatCurrencyVND(row.totalAmountNet) }} <span v-if="row.totalAmount > row.totalAmountNet" style="text-decoration: line-through;">({{ formatCurrencyVND(row.totalAmount) }})</span>
            </template>
          </el-table-column>
        </el-table>

        <div slot="footer" class="dialog-footer">
          <el-button v-if="odDetail.order.status === 'WAIT_FOR_CONFIRMATION'" type="danger" @click="changeStatus('CANCELLED')">
            Hủy
          </el-button>
          <el-button v-if="odDetail.order.status === 'WAIT_FOR_CONFIRMATION'" type="primary" @click="changeStatus('PREPARING_GOODS')">
            Xác nhận
          </el-button>
          <el-button v-if="odDetail.order.status === 'PREPARING_GOODS'" type="primary" @click="changeStatus('DELIVERING')">
            Gửi hàng
          </el-button>
          <el-button v-if="odDetail.order.status === 'DELIVERING'" type="primary" @click="changeStatus('DELIVERED')">
            Đã giao hàng
          </el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { voucherGetById, voucherCreateOrUpdate } from '@/api/voucher'
import Pagination from '@/components/Pagination'
import { ResponseCode, Status, VoucherType } from '@/enums/enums'
import { parseTime } from '@/utils'
import { formatCurrencyVND } from '@/utils/format'
import moment from 'moment'
import { orderGetAllPageOnline, orderGetDetailById, orderChangeStatus } from '@/api/order'

export default {
  name: 'ProductManagementVoucherListPage',
  components: { Pagination },
  data() {
    return {
      moment: moment,
      Status: Status,
      formatCurrencyVND: formatCurrencyVND,
      VoucherType: VoucherType,
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
        dateFrom: moment(new Date()).subtract(1, 'month').format('YYYY-MM-DD'),
        dateTo: moment(new Date()).format('YYYY-MM-DD')
      },
      sortDefault: {
        prop: 'createdAt',
        order: 'descending'
      },
      listStatus: [
        { value: Status.ALL, label: 'Chọn Trạng thái' },
        { value: 'WAIT_FOR_CONFIRMATION', label: 'Chờ xác nhận' },
        { value: 'PREPARING_GOODS', label: 'Chuẩn bị hàng' },
        { value: 'DELIVERING', label: 'Đang giao hàng' },
        { value: 'DELIVERED', label: 'Đã giao hàng' },
        { value: 'SUCCESS', label: 'Thành công' },
        { value: 'CANCELLED', label: 'Đã hủy' }
      ],
      listVoucherType: [
        { value: Status.ALL, label: 'Chọn Loại mã giảm giá' },
        { value: VoucherType.MONEY, label: 'Tiền' },
        { value: VoucherType.PERCENT, label: 'Phần trăm' }
      ],
      dialogFormVisible: false,
      downloadLoading: false,
      voucher: {
        id: null,
        code: null,
        type: VoucherType.MONEY,
        value: 0,
        quantity: 0,
        max: 0,
        startDate: null,
        endDate: null,
        createdAt: null,
        updatedAt: null,
        createdBy: null,
        updatedBy: null,
        status: Status.ACTIVE
      },
      orderDate: [],
      rules: {
        type: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        value: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        quantity: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: 'Vui lòng chọn', trigger: 'blur' }
        ],
        max: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ]
      },
      odDetail: null
    }
  },
  watch: {
  },
  created() {
    this.orderDate[0] = this.listQuery.dateFrom
    this.orderDate[1] = this.listQuery.dateTo
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
      orderGetAllPageOnline(this.listQuery).then(res => {
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
      voucherGetById(id).then(res => {
        this.dialogFormVisible = true
        this.voucher = res.data
        this.voucherDate = [
          moment(res.data.startDate).format('YYYY-MM-DD'),
          moment(res.data.endDate).format('YYYY-MM-DD')
        ]
      })
    },
    closeModal() {
      this.dialogFormVisible = false
      this.odDetail = null
    },
    edit() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.voucher.id) {
            this.$confirm('Bạn xác nhận cập nhật?', 'Xác nhận cập nhật', {
              confirmButtonText: 'Xác nhận',
              cancelButtonText: 'Hủy',
              type: 'warning'
            }).then(() => {
              voucherCreateOrUpdate(this.voucher).then(res => {
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
              voucherCreateOrUpdate(this.voucher).then(res => {
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
      voucherGetById(id).then(res1 => {
        if (res1 && res1.code === ResponseCode.CODE_SUCCESS) {
          this.$confirm('Chuyển trạng thái thành: "Kết thúc", bạn chắc chắn chứ?', 'Xác nhận xóa', {
            confirmButtonText: 'Xác nhận',
            cancelButtonText: 'Hủy',
            type: 'warning'
          }).then(() => {
            res1.data.status = Status.IN_ACTIVE
            voucherCreateOrUpdate(res1.data).then(res2 => {
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
    changeOrderDate() {
      this.listQuery.dateFrom = this.orderDate[0]
      this.listQuery.dateTo = this.orderDate[1]
      this.getList()
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
    showDetail(id) {
      orderGetDetailById(id).then((res) => {
        this.dialogFormVisible = true
        this.odDetail = res.data
      })
    },
    changeStatus(status) {
      this.$confirm('Bạn xác nhận?', 'Xác nhận.', {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning'
      }).then(() => {
        orderChangeStatus({
          id: this.odDetail.order.id,
          status: status
        }).then((res) => {
          if (res) {
            this.$message({
              showClose: true,
              message: 'Thành công!',
              type: 'success'
            })
            this.dialogFormVisible = false
            this.getList()
          }
        })
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
