import request from '@/utils/request'

export const orderPaySalesAtTheCounter = (data) =>
  request({
    url: '/order/pay-sales-at-the-counter',
    method: 'post',
    data
  })

export const createOrder = (data) =>
  request({
    url: '/order/create-order',
    method: 'post',
    data
  })

export const orderOnline = (data) =>
  request({
    url: '/order/order-online',
    method: 'post',
    data
  })

export const orderGetAllPageOnline = (params) =>
  request({
    url: '/order/get-all-page-online',
    method: 'get',
    params
  })

export const orderGetAllPageAtTheCounter = (params) =>
  request({
    url: '/order/get-all-page-at-the-counter',
    method: 'get',
    params
  })

export const orderGetDetailById = (id) =>
  request({
    url: '/order/get-detail-by-id/' + id,
    method: 'get'
  })

export const orderChangeStatus = (data) =>
  request({
    url: '/order/change-status',
    method: 'post',
    data
  })

export const orderGetAllByUser = () =>
  request({
    url: '/order/get-list-order-by-user',
    method: 'get'
  })

export const getHistoryCustomer = (data) =>
  request({
    url: '/order/get-history-customer',
    method: 'post',
    data
  })

export const getHistoryCustomerPN = (data) =>
  request({
    url: '/order/get-history-customer-phone',
    method: 'post',
    data
  })
