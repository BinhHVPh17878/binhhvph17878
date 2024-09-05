import request from '@/utils/request'

const ENV = '/product-detail'

export const getAllProductDetailInOfStock = () =>
  request({
    url: '/product-detail/get-all-product-detail-in-of-stock',
    method: 'get'
  })

export const filterProductAttributes = (data) =>
  request({
    url: ENV + '/filter-product-attribute',
    method: 'post',
    data
  })

export const getProductDetailByAttributes = (params) =>
  request({
    url: ENV + '/get-product-detail-by-attributes',
    method: 'get',
    params
  })

export const getAllProductDetailByListId = (data) =>
  request({
    url: ENV + '/get-all-product-detail-by-list-id',
    method: 'post',
    data
  })
