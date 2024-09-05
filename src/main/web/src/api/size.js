import request from '@/utils/request'

export const sizeGetAllPage = (params) =>
  request({
    url: '/size/get-all-page',
    method: 'get',
    params
  })

export const sizeGetById = (id) =>
  request({
    url: '/size/' + id,
    method: 'get'
  })

export const sizeCreateOrUpdate = (data) =>
  request({
    url: '/size',
    method: 'post',
    data
  })

export const sizeGetAllStatusActive = () =>
  request({
    url: '/size/get-all-status-active',
    method: 'get'
  })

export const sizeGetAll = () =>
  request({
    url: '/size/get-all',
    method: 'get'
  })
