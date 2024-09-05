import request from '@/utils/request'

export const voucherGetAllPage = (params) =>
  request({
    url: '/voucher/get-all-page',
    method: 'get',
    params
  })

export const voucherGetById = (id) =>
  request({
    url: '/voucher/' + id,
    method: 'get'
  })

export const voucherCreateOrUpdate = (data) =>
  request({
    url: '/voucher',
    method: 'post',
    data
  })

export const voucherGetAllLikeCodeAndStillActive = (params) =>
  request({
    url: '/voucher/get-all-Like-code-and-still-active',
    method: 'get',
    params
  })

export const voucherGetByCode = (code) =>
  request({
    url: '/voucher/get-by-code/' + code,
    method: 'get'
  })
