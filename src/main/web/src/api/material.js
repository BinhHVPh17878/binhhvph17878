import request from '@/utils/request'

export const materialGetAllPage = (params) =>
  request({
    url: '/material/get-all-page',
    method: 'get',
    params
  })

export const materialGetById = (id) =>
  request({
    url: '/material/' + id,
    method: 'get'
  })

export const materialCreateOrUpdate = (data) =>
  request({
    url: '/material',
    method: 'post',
    data
  })

export const materialGetAllStatusActive = () =>
  request({
    url: '/material/get-all-status-active',
    method: 'get'
  })

export const materialGetAll = () =>
  request({
    url: '/material/get-all',
    method: 'get'
  })
