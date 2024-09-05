import { saveToLocal, loadFromLocal } from '@/utils/local-storage'

const ADD_PRODUCT_TO_CART = 'ADD_PRODUCT_TO_CART'
const REMOVE_PRODUCT_TO_CART = 'REMOVE_PRODUCT_TO_CART'
const CLEAR_CART = 'CLEAR_CART'
const SET_PAY = 'SET_PAY'
const SET_ORDER = 'SET_ORDER'
const SET_ORDER_W = 'SET_ORDER_W'

const cart = {
  state: {
    cart: loadFromLocal('cart', '') || [],
    pay: loadFromLocal('pay', '') || {
      city: '',
      district: '',
      ward: '',
      paymentType: '',
      fullNameCustomer: '',
      phoneNumber: '',
      address: '',
      voucherId: '',
      noteByCustomer: '',
      totalAmount: '',
      email: '',
      createdBy: '',
      orderDetails: null
    },
    order: loadFromLocal('order', '') || {
      id: '',
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
      paymentType: 'PAYMENT_ON_DELIVERY',
      status: '',
      listOrderDetail: [],
      isVoucher: false,
      voucherCode: '',
      voucher: null,
      moneyPaid: 0,
      moneyRefunds: 0
    },
    orderWStore: loadFromLocal('orderWStore', '') || {}
  },
  mutations: {
    [ADD_PRODUCT_TO_CART](state, productDetail) {
      const i = state.cart.findIndex(e => e.productDetailId === productDetail.productDetailId)
      if (i !== -1) {
        state.cart[i].quantity = Number(state.cart[i].quantity) + Number(productDetail.quantity)
      } else {
        state.cart.push(productDetail)
      }
      saveToLocal('cart', state.cart)
    },
    [REMOVE_PRODUCT_TO_CART](state, productDetailId) {
      state.cart = state.cart.filter(e => e.productDetailId !== productDetailId)
      saveToLocal('cart', state.cart)
    },
    [CLEAR_CART](state) {
      state.cart = []
      saveToLocal('cart', state.cart)
      state.pay = {
        city: '',
        district: '',
        ward: '',
        paymentType: '',
        fullNameCustomer: '',
        phoneNumber: '',
        address: '',
        voucherId: '',
        noteByCustomer: '',
        totalAmount: '',
        email: '',
        createdBy: '',
        orderDetails: null
      }
      saveToLocal('pay', state.pay)
      state.order = {
        id: '',
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
        paymentType: 'PAYMENT_ON_DELIVERY',
        status: '',
        listOrderDetail: [],
        isVoucher: false,
        voucherCode: '',
        voucher: null,
        moneyPaid: 0,
        moneyRefunds: 0
      }
      saveToLocal('order', state.order)
    },
    [SET_PAY](state, pay) {
      state.pay = pay
      saveToLocal('pay', state.pay)
    },
    [SET_ORDER](state, order) {
      state.order = order
      saveToLocal('order', state.order)
    },
    [SET_ORDER_W](state, order) {
      state.orderWStore = order
      saveToLocal('orderWStore', state.orderWStore)
    }
  },
  actions: {
    addProductToCart({ commit }, productDetail) {
      return new Promise((resolve, reject) => {
        commit(ADD_PRODUCT_TO_CART, productDetail)
        return resolve()
      })
    },
    removeProductToCart({ commit }, productDetailId) {
      return new Promise((resolve, reject) => {
        commit(REMOVE_PRODUCT_TO_CART, productDetailId)
        return resolve()
      })
    },
    clearCart({ commit }) {
      return new Promise((resolve, reject) => {
        commit(CLEAR_CART)
        return resolve()
      })
    },
    setPay({ commit }, pay) {
      return new Promise((resolve, reject) => {
        commit(SET_PAY, pay)
        return resolve()
      })
    },
    setOrder({ commit }, order) {
      return new Promise((resolve, reject) => {
        commit(SET_PAY, order)
        return resolve()
      })
    },
    setOrderW({ commit }, order) {
      return new Promise((resolve, reject) => {
        commit(SET_ORDER_W, order)
        return resolve()
      })
    }
  },
  getters: {
    cart: state => state.cart,
    pay: state => state.pay,
    order: state => state.order,
    orderWStore: state => state.orderWStore
  }
}

export default cart
