import request from '@/utils/request'

export const colorGetAllPage = (params) =>
  request({
    url: '/color/get-all-page',
    method: 'get',
    params
  })

export const colorGetById = (id) =>
  request({
    url: '/color/' + id,
    method: 'get'
  })

export const colorCreateOrUpdate = (data) =>
  request({
    url: '/color',
    method: 'post',
    data
  })

export const colorGetAllStatusActive = () =>
  request({
    url: '/color/get-all-status-active',
    method: 'get'
  })

export const colorGetAll = () =>
  request({
    url: '/color/get-all',
    method: 'get'
  })
