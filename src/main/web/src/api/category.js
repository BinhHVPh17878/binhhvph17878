import request from '@/utils/request'

export const categoryGetAllPage = (params) =>
  request({
    url: '/category/get-all-page',
    method: 'get',
    params
  })

export const categoryGetById = (id) =>
  request({
    url: '/category/' + id,
    method: 'get'
  })

export const categoryCreateOrUpdate = (data) =>
  request({
    url: '/category',
    method: 'post',
    data
  })

export const categoryGetAllStatusActive = () =>
  request({
    url: '/category/get-all-status-active',
    method: 'get'
  })

export const categoryGetAll = () =>
  request({
    url: '/category/get-all',
    method: 'get'
  })
