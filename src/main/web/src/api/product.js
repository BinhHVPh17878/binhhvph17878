import request from '@/utils/request'

export const productCreateOrUpdate = (data) =>
  request({
    url: '/product',
    method: 'post',
    data
  })

export const productGetById = (id) =>
  request({
    url: '/product/' + id,
    method: 'get'
  })

export const productGetAllPage = (params) =>
  request({
    url: '/product/get-all-page',
    method: 'get',
    params
  })

export const productUpdateStatusInActiveById = (id) =>
  request({
    url: '/product/update-status-in-active-by-id/' + id,
    method: 'post'
  })

export const productGetAllBySalesAtTheCounter = (params) =>
  request({
    url: '/product/get-all-by-sales-at-the-counter',
    method: 'get',
    params
  })

export const productGetTop5ProductCreatedAtDESC = (params) =>
  request({
    url: '/product/get-top-5-product-create-at-desc',
    method: 'get'
  })

export const productGetAllProductDetailView = (id) =>
  request({
    url: '/product/get-all-product-detail-view/' + id,
    method: 'get'
  })

export const getProductByIdWebShop = (id) =>
  request({
    url: '/product/get-product-by-id-web-shop/' + id,
    method: 'get'
  })

