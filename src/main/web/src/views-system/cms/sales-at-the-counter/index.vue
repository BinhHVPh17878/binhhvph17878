<!-- eslint-disable vue/valid-v-on -->
<!-- eslint-disable vue/no-unused-vars -->
<!-- eslint-disable vue/no-use-v-if-with-v-for -->
<template>
  <div class="app-container">
    <div class="container-form-header">
      <div>
        <h4 style="margin-block-end: 5px; margin-block-start: 0;">Bán hàng tại quầy</h4>
        <span style="font-size: 14px;">Hóa đơn</span>
      </div>

      <el-button type="primary" icon="el-icon-circle-plus-outline" style="margin-left: 10px;" @click="addOrder(orderId)">
        Thêm hóa đơn
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="16">
        <div class="container-form-table" style="padding-top: 10px;">
          <div>
            <el-form>
              <el-form-item>
                <el-tabs v-model="orderId" closable @tab-remove="deleteOrder">
                  <el-tab-pane
                    v-for="(o, i) in listOrder"
                    :key="o.id"
                    :label="o.fullName ? o.fullName : ('Hóa đơn ' + o.id)"
                    :name="o.id"
                  >
                    <!-- <el-input
                      placeholder="Tìm kiếm Sản phẩm"
                      style="width: 100%"
                      @keyup.enter.native="getList()"
                    >
                      <i slot="suffix" class="el-icon-search" style="cursor: pointer;" @click="getList()" />
                    </el-input> -->

                    <el-autocomplete
                      v-model="searchProductDetail"
                      class="inline-input"
                      style="width: 100%;"
                      placeholder="Tìm kiếm Sản phẩm"
                      :fetch-suggestions="loadListProductDetail"
                      :trigger-on-focus="true"
                      @select="$item => selectProductDetail($item, i)"
                    >
                      <i slot="suffix" class="el-icon-search" style="cursor: pointer;" />
                      <template slot-scope="{ item }">
                        <el-divider v-if="item.index !== 0" class="divider-custom" />
                        <div style="margin-top: 20px; margin-bottom: 20px; display: flex;">
                          <el-image
                            style="width: 130px; height: 130px"
                            :src="item.listImage.length > 0 ? item.listImage[0] : item.product.listImage[0]"
                          />
                          <div style="margin-left: 20px;">
                            <div style="width: 100%;">
                              <span style="font-weight: bold;">{{ item.product.name }}</span>
                            </div>
                            <div style="width: 100%;">
                              <span>Thuộc tính: {{ item.size.name }} - {{ item.color.name }} - {{ item.material.name }}</span>
                            </div>
                            <div style="width: 100%;">
                              <span>Giá: {{ formatCurrencyVND(item.priceNet) }} <span v-if="item.price > item.priceNet" style="text-decoration: line-through;">({{ formatCurrencyVND(item.price) }})</span></span>
                            </div>
                            <div style="width: 100%;">
                              <span>Số lượng còn lại: {{ item.quantity }}</span>
                            </div>
                          </div>
                        </div>
                        <el-divider v-if="item.index === item.listSize" class="divider-custom" />
                      </template>
                    </el-autocomplete>

                    <el-table
                      :key="tableKey"
                      :data="o.listOrderDetail"
                      style="width: 100%;"
                      class="tableOPD"
                    >
                      <el-table-column label="Stt" align="center" width="50">
                        <template slot-scope="scope">
                          <span>#{{ scope.$index + 1 }}</span>
                        </template>
                      </el-table-column>

                      <el-table-column label="Sản phẩm" prop="productDetail.product.name" align="center" />

                      <el-table-column label="Thuộc tính" align="center">
                        <template slot-scope="{row}">
                          {{ row.productDetail.size.name + ' - ' + row.productDetail.color.name + ' - ' + row.productDetail.material.name }}
                        </template>
                      </el-table-column>

                      <el-table-column label="Đơn giá" prop="productDetailPriceNet" align="center">
                        <template slot-scope="{row}">
                          {{ formatCurrencyVND(row.productDetailPriceNet) }} <span v-if="row.productDetailPrice > row.productDetailPriceNet" style="text-decoration: line-through;">({{ formatCurrencyVND(row.productDetailPrice) }})</span>
                        </template>
                      </el-table-column>

                      <el-table-column fixed="right" label="Số lượng" prop="quantity" align="center">
                        <template slot-scope="scope">
                          <el-form
                            ref="dataFormQuantity"
                            :model="listOrder[indexOrder()].listOrderDetail[scope.$index]"
                            :rules="rulesFormQuantity"
                          >
                            <el-form-item prop="quantity">
                              <div style="display: flex;">
                                <el-button
                                  plain
                                  type="info"
                                  style="padding-left: 4px; padding-right: 4px;"
                                  @click="reduceQuantity(i, scope.$index), calculatedTotalMoney(i)"
                                >
                                  <i class="el-icon-minus" />
                                </el-button>
                                <el-input
                                  v-model="scope.row.quantity"
                                  style="text-align: center !important; margin-left: 4px; margin-right: 4px;"
                                  class="input-text-align-center input-quantity"
                                  @change="calculatedTotalMoney(i)"
                                />
                                <el-button
                                  plain
                                  type="info"
                                  style="padding-left: 4px; padding-right: 4px;"
                                  @click="increaseQuantity(i, scope.$index), calculatedTotalMoney(i)"
                                >
                                  <i class="el-icon-plus" />
                                </el-button>
                              </div>
                            </el-form-item>
                          </el-form>
                        </template>
                      </el-table-column>

                      <el-table-column fixed="right" label="Tổng tiền" prop="totalAmountNet" align="center">
                        <template slot-scope="{row}">
                          {{ formatCurrencyVND(row.totalAmountNet) }} <span v-if="row.totalAmount > row.totalAmountNet" style="text-decoration: line-through;">({{ formatCurrencyVND(row.totalAmount) }})</span>
                        </template>
                      </el-table-column>

                      <el-table-column fixed="right" label="" align="center" width="80">
                        <template slot-scope="scope">
                          <el-button type="danger" size="small" plain @click="listOrder[i].listOrderDetail.splice(scope.$index, 1), calculatedTotalMoney(i)">
                            <i class="el-icon-delete" />
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-tab-pane>
                </el-tabs>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="container-form-table">
          <div>
            <span style="font-size: 14px; font-weight: bold;">Thông tin Khách hàng</span>

            <el-form
              ref="dataFormInformation"
              :model="listOrder[indexOrder()]"
              :rules="rulesFormInformation"
              style="margin-top: 10px;"
            >
              <el-form-item prop="fullName">
                <el-autocomplete
                  v-model="listOrder[indexOrder()].fullName"
                  class="inline-input input-border-bottom"
                  placeholder="Tên Khách hàng"
                  :fetch-suggestions="querySearch"
                  :trigger-on-focus="false"
                  style="width: 100%;"
                  @select="handleSelect"
                >
                  <template slot-scope="{ item }">
                    <span>{{ item.fullName + ' - ' + item.phoneNumber + (item.address ? ' - ' + item.address : '') }}</span>
                  </template>
                  <i slot="suffix" class="el-icon-user" />
                </el-autocomplete>
                <!-- <el-input
                  v-model="listOrder[indexOrder()].fullName"
                  placeholder="Tên Khách hàng"
                  class="input-border-bottom"
                  style="width: 100%"
                >
                  <i slot="suffix" class="el-icon-user" />
                </el-input> -->
              </el-form-item>

              <el-form-item prop="phoneNumber">
                <el-autocomplete
                  v-model="listOrder[indexOrder()].phoneNumber"
                  class="inline-input input-border-bottom"
                  placeholder="SĐT Khách hàng"
                  :fetch-suggestions="querySearch2"
                  :trigger-on-focus="false"
                  style="width: 100%;"
                  @select="handleSelect"
                >
                  <template slot-scope="{ item }">
                    <span>{{ item.fullName + ' - ' + item.phoneNumber + (item.address ? ' - ' + item.address : '') }}</span>
                  </template>
                  <i slot="suffix" class="el-icon-phone-outline" />
                </el-autocomplete>
              </el-form-item>

              <el-form-item prop="address">
                <el-input
                  v-model="listOrder[indexOrder()].address"
                  placeholder="Địa chỉ Khách hàng"
                  class="input-border-bottom"
                  style="width: 100%"
                >
                  <i slot="suffix" class="el-icon-location-outline" />
                </el-input>
              </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="container-form-table">
          <div>
            <el-form
              ref="dataFormPay"
              :model="listOrder[indexOrder()]"
              :rules="rulesFormPay"
            >
              <el-form-item>
                <label>Thông tin thanh toán</label>
              </el-form-item>

              <el-form-item>
                <div>
                  <label>Tổng tiền ({{ totalProductDetail() }} sản phẩm):</label>

                  <span style="float: right;">{{ formatCurrencyVND(listOrder[indexOrder()].totalAmount) }}</span>
                </div>
              </el-form-item>

              <el-form-item v-if="listOrder[indexOrder()].listOrderDetail.reduce((sum, od) => sum + ((od.quantity * od.productDetailPrice) - (od.quantity * od.productDetailPriceNet)), 0) > 0">
                <div>
                  <label>Giảm giá:</label>

                  <span style="float: right;">-{{ formatCurrencyVND(listOrder[indexOrder()].listOrderDetail.reduce((sum, od) => sum + ((od.quantity * od.productDetailPrice) - (od.quantity * od.productDetailPriceNet)), 0)) }}</span>
                </div>
              </el-form-item>

              <el-form-item>
                <div>
                  <el-checkbox v-model="listOrder[indexOrder()].isVoucher" @change="calculatedTotalMoney(indexOrder())">
                    <template #default>
                      <span style="font-weight: bold;">Mã giảm giá</span>
                    </template>
                  </el-checkbox>

                  <div v-if="listOrder[indexOrder()].isVoucher" style="float: right;">

                    <el-autocomplete
                      v-model="listOrder[indexOrder()].voucherCode"
                      class="inline-input input-border-bottom"
                      style="width: 100%;"
                      placeholder="Mã giảm giá"
                      :fetch-suggestions="loadListVoucher"
                      :trigger-on-focus="true"
                      @select="selectVoucher"
                      @change="calculatedTotalMoney(indexOrder())"
                    >
                      <i slot="suffix" class="el-icon-s-promotion" />
                      <template slot-scope="{ item }">
                        {{ item.code }}
                      </template>
                    </el-autocomplete>

                    <!-- <el-input v-model="listOrder[indexOrder()].voucherCode" class="input-border-bottom">
                      <i slot="suffix" class="el-icon-s-promotion" />
                    </el-input> -->
                  </div>
                </div>
              </el-form-item>

              <el-form-item v-if="listOrder[indexOrder()].isVoucher && listOrder[indexOrder()].voucher && listOrder[indexOrder()].voucherCode">
                <div>
                  <label>Giảm giá (Mã giảm giá):</label>

                  <span v-if="listOrder[indexOrder()].voucher.type === VoucherType.MONEY" style="float: right;">
                    -{{ formatCurrencyVND(listOrder[indexOrder()].voucher.value) }}
                  </span>

                  <span v-else style="float: right;">
                    -{{ formatCurrencyVND((listOrder[indexOrder()].listOrderDetail.reduce((sum, od) => sum + (od.quantity * od.productDetailPriceNet), 0) * listOrder[indexOrder()].voucher.value / 100) > listOrder[indexOrder()].voucher.max ? listOrder[indexOrder()].voucher.max : listOrder[indexOrder()].listOrderDetail.reduce((sum, od) => sum + (od.quantity * od.productDetailPriceNet), 0) * listOrder[indexOrder()].voucher.value / 100) }}
                  </span>
                </div>
              </el-form-item>

              <el-form-item>
                <div>
                  <label>Tổng tiền khách phải trả:</label>

                  <span style="float: right;">{{ formatCurrencyVND(listOrder[indexOrder()].totalAmountNet) }}</span>
                </div>
              </el-form-item>

              <el-form-item prop="moneyPaid">
                <div>
                  <label>Tiền khách đưa:</label>

                  <div style="float: right;">
                    <el-input v-model="listOrder[indexOrder()].moneyPaid" class="input-border-bottom" @input="calculatedMoneyRefunds(indexOrder())">
                      <i slot="suffix" class="el-icon-money" />
                    </el-input>
                  </div>
                </div>
              </el-form-item>

              <el-form-item>
                <div>
                  <label>Trả lại khách:</label>

                  <span style="float: right;">{{ formatCurrencyVND(listOrder[indexOrder()].moneyRefunds) }}</span>
                </div>
              </el-form-item>

              <el-form-item>
                <el-button style="width: 100%;" type="primary" @click="pay(indexOrder())">Thanh toán</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog
      width="30%"
      title="Hóa đơn"
      :visible.sync="isDialogInvoice"
      :close-on-click-modal="false"
      @closed="closeDialogInvoice()"
    >
      <div id="invoice">
        ádasdasd
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialogInvoice()">
          Hủy
        </el-button>
        <el-button type="primary" @click="printInvoice()">
          In
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import moment from 'moment'
import { formatCurrencyVND } from '@/utils/format'
import { productGetAllBySalesAtTheCounter } from '@/api/product'
import { ResponseCode, Status, VoucherType } from '@/enums/enums'
import { voucherGetAllLikeCodeAndStillActive, voucherGetByCode } from '@/api/voucher'
import html2canvas from 'html2canvas'
import jsPDF from 'jspdf'
import { orderPaySalesAtTheCounter, getHistoryCustomer, getHistoryCustomerPN } from '@/api/order'

export default {
  name: 'ProductManagementBrandListPage',
  components: {},
  data() {
    var validateQuantity = (rule, value, callback) => {
      if (isNaN(value)) {
        callback(new Error('Số lượng phải là số'))
      } else if (value <= 0) {
        callback(new Error('Số lượng phải lớn hơn 0'))
      } else {
        callback()
      }
    }
    return {
      isDialogInvoice: false,
      tableKey: 0,
      moment: moment,
      VoucherType: VoucherType,
      formatCurrencyVND: formatCurrencyVND,
      listLoading: true,
      orderId: '1',
      listOrder: [
        {
          id: '1',
          fullName: '',
          phoneNumber: '',
          email: '',
          address: '',
          city: '',
          district: '',
          ward: '',
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
          paymentType: 'PAYMENT_IN_SHOP',
          status: '',
          listOrderDetail: [],
          isVoucher: false,
          voucherCode: '',
          voucher: null,
          moneyPaid: 0,
          moneyRefunds: 0
        }
      ],
      orderIndex: 1,
      searchProductDetail: '',
      rulesFormInformation: {
        fullName: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ],
        phoneNumber: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ]
        // address: [
        //   { required: true, message: 'Không được để trống', trigger: 'blur' }
        // ]
      },
      rulesFormPay: {
        moneyPaid: [
          { required: true, message: 'Không được để trống', trigger: 'blur' }
        ]
      },
      rulesFormQuantity: {
        quantity: [
          { validator: validateQuantity, trigger: 'change' }
        ]
      }
    }
  },
  watch: {},
  created() {
    // console.log(this.listOrder.findIndex(o => o.id === this.orderId))
  },
  mounted() {},
  destroyed() {},
  methods: {
    closeDialogInvoice() {
      this.isDialogInvoice = false
    },
    printInvoice() {
      const invoice = document.getElementById('invoice')
      html2canvas(invoice).then((canvas) => {
        const imgData = canvas.toDataURL('image/png')
        // eslint-disable-next-line new-cap
        const pdf = new jsPDF()
        pdf.addImage(imgData, 'PNG', 0, 0)
        const pdfBlob = pdf.output('blob')

        // Mở PDF và in
        const blobURL = URL.createObjectURL(pdfBlob)
        // const printWindow = window.open(blobURL)

        // printWindow.print()

        const iframe = document.createElement('iframe')
        iframe.style.display = 'none'
        iframe.src = blobURL
        document.body.appendChild(iframe)

        // Print the PDF once the iframe loads
        iframe.onload = () => {
          iframe.contentWindow.print()
          document.body.removeChild(iframe)
          URL.revokeObjectURL(blobURL)
        }
      })
    },
    addOrder(o) {
      // eslint-disable-next-line prefer-const
      let newOrderId = ++this.orderIndex + ''
      this.listOrder.push({
        id: newOrderId,
        fullName: '',
        phoneNumber: '',
        email: '',
        address: '',
        city: '',
        district: '',
        ward: '',
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
        paymentType: 'PAYMENT_IN_SHOP',
        status: '',
        listOrderDetail: [],
        isVoucher: false,
        voucherCode: '',
        voucher: null,
        moneyPaid: 0,
        moneyRefunds: 0
      })
      this.orderId = newOrderId
    },
    deleteOrder(o) {
      console.log(o)
      // eslint-disable-next-line prefer-const
      let os = this.listOrder
      let orderIdActive = this.orderId
      if (orderIdActive === o) {
        os.forEach((tab, index) => {
          if (tab.id === o) {
            // eslint-disable-next-line prefer-const
            let nextTab = os[index + 1] || os[index - 1]
            if (nextTab) {
              orderIdActive = nextTab.id
            }
          }
        })
      }

      this.orderId = orderIdActive
      this.listOrder = os.filter(tab => tab.id !== o)
      if (this.listOrder.length === 0) {
        this.addOrder(this.orderId)
      }
    },
    indexOrder() {
      return this.listOrder.findIndex(o => o.id === this.orderId)
    },
    totalProductDetail() {
      if (this.listOrder[this.indexOrder()].listOrderDetail.length > 0) {
        const total = this.listOrder[this.indexOrder()].listOrderDetail.reduce((sum, od) => sum + Number(od.quantity), 0)
        return isNaN(total) ? 0 : total
      }
      return 0
    },
    selectProductDetail(item, i) {
      // console.log(i)
      // console.log(item)
      const j = this.listOrder[i].listOrderDetail.findIndex(o => o.productDetailId === item.id)
      if (j === -1) {
        this.listOrder[i].listOrderDetail.push({
          id: null,
          orderId: null,
          productDetail: item,
          productDetailId: item.id,
          productDetailPrice: item.price,
          productDetailPriceNet: item.priceNet,
          quantity: 1,
          totalAmount: item.price * 1,
          totalAmountNet: item.priceNet * 1,
          createdAt: '',
          updatedAt: '',
          createdBy: '',
          updatedBy: '',
          status: Status.ACTIVE
        })
      } else {
        this.listOrder[i].listOrderDetail[j].quantity = this.listOrder[i].listOrderDetail[j].quantity + 1
      }

      this.calculatedTotalMoney(i)
    },
    selectVoucher(item) {
      this.listOrder[this.indexOrder()].voucherCode = item.code
      this.calculatedTotalMoney(this.indexOrder())
    },
    loadListVoucher(code, cb) {
      voucherGetAllLikeCodeAndStillActive({
        code: (code !== null && code !== undefined && code !== '') ? code : ''
      })
        .then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            cb(res.data)
          } else {
            cb(null)
          }
        })
    },
    loadListProductDetail(searchBy, cb) {
      productGetAllBySalesAtTheCounter({
        searchBy: (searchBy !== null && searchBy !== undefined && searchBy !== '') ? searchBy : ''
      }).then(res => {
        if (res && res.code === ResponseCode.CODE_SUCCESS) {
          if (res.data.length > 0) {
            for (var i = 0; i < res.data.length; i++) {
              res.data[i].index = i
              res.data[i].listSize = res.data.length
            }
            cb(res.data)
          } else {
            cb(null)
          }
        }
      })
    },
    async calculatedTotalMoney(i) {
      let totalAmountP = 0
      let totalAmountPNet = 0
      let totalDiscountMoney = 0
      let discountMoney = 0
      let totalPriceDifference = 0

      if (!this.listOrder[i].isVoucher) {
        this.listOrder[i].voucher = null
        this.listOrder[i].voucherCode = ''
        this.listOrder[i].voucherId = ''
      }

      if (this.listOrder[i].isVoucher && this.listOrder[i].voucherCode) {
        await voucherGetByCode(this.listOrder[i].voucherCode)
          .then(res => {
            if (res && res.code === ResponseCode.CODE_SUCCESS && res.data) {
              this.listOrder[i].voucher = res.data
              this.listOrder[i].voucherId = res.data.id
            }
          })
      }

      if (this.listOrder[i].voucher && this.listOrder[i].voucher.type === VoucherType.MONEY) {
        totalDiscountMoney = this.listOrder[i].voucher.value > this.listOrder[i].listOrderDetail.reduce((sum, od) => sum + (Number(od.quantity) * Number(od.productDetailPriceNet)), 0) ? this.listOrder[i].listOrderDetail.reduce((sum, od) => sum + (Number(od.quantity) * Number(od.productDetailPriceNet)), 0) : this.listOrder[i].voucher.value
        discountMoney = this.listOrder[i].voucher.value / this.listOrder[i].listOrderDetail.reduce((sum, od) => sum + Number(od.quantity), 0)
      }

      if (this.listOrder[i].voucher && this.listOrder[i].voucher.type === VoucherType.PERCENT) {
        totalDiscountMoney = this.listOrder[i].voucher.value * this.listOrder[i].listOrderDetail.reduce((sum, od) => sum + (Number(od.quantity) * Number(od.productDetailPriceNet)), 0) / 100
        if (totalDiscountMoney > this.listOrder[i].voucher.max) {
          totalDiscountMoney = this.listOrder[i].voucher.max
        }
        discountMoney = totalDiscountMoney / this.listOrder[i].listOrderDetail.reduce((sum, od) => sum + Number(od.quantity), 0)
      }

      for (let j = 0; j < this.listOrder[i].listOrderDetail.length; j++) {
        totalPriceDifference += (this.listOrder[i].listOrderDetail[j].productDetailPrice - this.listOrder[i].listOrderDetail[j].productDetailPriceNet) * this.listOrder[i].listOrderDetail[j].quantity
        const totalAmountOD = this.listOrder[i].listOrderDetail[j].productDetailPrice * this.listOrder[i].listOrderDetail[j].quantity
        let totalAmountODNet = 0
        if (this.listOrder[i].isVoucher && this.listOrder[i].voucherCode && this.listOrder[i].voucher) {
          if (j === this.listOrder[i].listOrderDetail.length - 1) {
            totalAmountODNet = totalAmountP + totalAmountOD - totalPriceDifference - totalDiscountMoney - totalAmountPNet
          } else {
            totalAmountODNet = Math.floor((this.listOrder[i].listOrderDetail[j].productDetailPriceNet * this.listOrder[i].listOrderDetail[j].quantity) - (this.listOrder[i].listOrderDetail[j].quantity > 1 ? (discountMoney * this.listOrder[i].listOrderDetail[j].quantity) : discountMoney))
          }
        } else {
          totalAmountODNet = this.listOrder[i].listOrderDetail[j].productDetailPriceNet * this.listOrder[i].listOrderDetail[j].quantity
        }
        this.listOrder[i].listOrderDetail[j].totalAmount = isNaN(totalAmountOD) ? 0 : totalAmountOD
        this.listOrder[i].listOrderDetail[j].totalAmountNet = isNaN(totalAmountODNet) ? 0 : totalAmountODNet
        totalAmountP += totalAmountOD
        totalAmountPNet += totalAmountODNet
      }

      this.listOrder[i].totalAmount = totalAmountP
      this.listOrder[i].totalAmountNet = totalAmountPNet
      this.calculatedMoneyRefunds(i)
    },
    changeQuantity(i, j, value) {
      if (value <= 0) {
        this.$confirm('Sản phẩm này sẽ bị xóa?', 'Cảnh báo', {
          confirmButtonText: 'Xác nhận',
          cancelButtonText: 'Hủy',
          type: 'warning'
        }).then(async() => {
          this.listOrder[i].listOrderDetail.splice(j, 1)
        })
      }
      this.calculatedTotalMoney(i)
    },
    increaseQuantity(i, j) {
      this.listOrder[i].listOrderDetail[j].quantity++
    },
    reduceQuantity(i, j) {
      if (this.listOrder[i].listOrderDetail[j].quantity - 1 <= 0) {
        this.$confirm('Sản phẩm này sẽ bị xóa?', 'Cảnh báo', {
          confirmButtonText: 'Xác nhận',
          cancelButtonText: 'Hủy',
          type: 'warning'
        }).then(async() => {
          this.listOrder[i].listOrderDetail.splice(j, 1)
        })
      } else {
        this.listOrder[i].listOrderDetail[j].quantity--
      }
    },
    calculatedMoneyRefunds(i) {
      this.listOrder[i].moneyRefunds = this.listOrder[i].moneyPaid >= this.listOrder[i].totalAmountNet ? this.listOrder[i].moneyPaid - this.listOrder[i].totalAmountNet : 0
    },
    querySearch(queryString, cb) {
      const data = []
      getHistoryCustomer({
        fullName: queryString
      }).then((res) => {
        res.data.forEach(e => {
          data.push({
            fullName: e.fullName,
            phoneNumber: e.phoneNumber,
            address: e.address
          })
        })
      })
      cb(data)
    },
    querySearch2(queryString, cb) {
      const data = []
      getHistoryCustomerPN({
        phoneNumber: queryString
      }).then((res) => {
        res.data.forEach(e => {
          data.push({
            fullName: e.fullName,
            phoneNumber: e.phoneNumber,
            address: e.address
          })
        })
      })
      cb(data)
    },
    handleSelect(item) {
      this.listOrder[this.indexOrder()].fullName = item.fullName
      this.listOrder[this.indexOrder()].phoneNumber = item.phoneNumber
      this.listOrder[this.indexOrder()].address = item.address
    },
    pay(i) {
      // eslint-disable-next-line no-unused-vars
      let isValidate = true

      if (this.listOrder[i].listOrderDetail.length === 0) {
        this.$message({
          showClose: true,
          message: 'Chưa thêm sản phẩm nào. Vui lòng thêm ít nhất 1 sản phẩm.',
          type: 'error'
        })
        return
      }

      for (let j = 0; j < this.listOrder[i].listOrderDetail.length; j++) {
        this.$refs.dataFormQuantity[j].validate(valid => {
          if (!valid) {
            isValidate = false
            return false
          }
        })
      }

      if (!isValidate) {
        this.$message({
          showClose: true,
          message: 'Nhập đúng số lượng sản phẩm',
          type: 'error'
        })
        return
      }

      this.$refs.dataFormInformation.validate(valid => {
        if (!valid) {
          isValidate = false
          return false
        }
      })

      this.$refs.dataFormPay.validate(valid => {
        if (!valid) {
          isValidate = false
          return false
        }
      })

      if (!isValidate) {
        this.$message({
          showClose: true,
          message: 'Vui lòng nhập đủ thông tin',
          type: 'error'
        })
        return
      }

      this.$confirm('Xác nhận thanh toán?', 'Xác nhận', {
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        type: 'warning'
      }).then(async() => {
        orderPaySalesAtTheCounter(this.listOrder[i]).then(res => {
          if (res && res.code === ResponseCode.CODE_SUCCESS) {
            // this.listOrder[i].listOrderDetail.splice(i, 1)
            this.deleteOrder(this.listOrder[i].id)
            this.$message({
              showClose: true,
              message: 'Thanh toán thành công.',
              type: 'success'
            })
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
.divider-custom {
  margin: 0;
}

::v-deep .input-border-bottom {
  input {
    border-radius: 0 !important;
    border-top: 0 !important;
    border-left: 0 !important;
    border-right: 0 !important;
    // background-color: #FAFBFE !important;
    padding-left: 10px !important;
    padding-right: 10px !important;
  }
}
::v-deep .input-text-align-center {
  input {
    text-align: center !important;
  }
}

::v-deep .input-quantity {
  input {
    padding-left: 1px; padding-right: 1px;
  }
}

::v-deep .tableOPD {
  .el-table--medium th, .el-table--medium td {
    height: 80px;
  }
}
</style>
