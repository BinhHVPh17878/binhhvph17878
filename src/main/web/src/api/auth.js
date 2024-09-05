import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/user/info',
    method: 'get'
  })
}

export function getUserInfo() {
  return request({
    url: '/user/get-user-info',
    method: 'get'
  })
}

export const getAllRole = () =>
  request({
    url: '/user/get-all-role',
    method: 'get'
  })

export const userGetAllPage = (params) =>
  request({
    url: '/user/get-all-page',
    method: 'get',
    params
  })

export const userCreate = (data) =>
  request({
    url: '/user/create',
    method: 'post',
    data
  })

export const userUpdate = (data) =>
  request({
    url: '/user/update',
    method: 'post',
    data
  })

export const userGetById = (id) =>
  request({
    url: '/user/get-by-id/' + id,
    method: 'get'
  })
