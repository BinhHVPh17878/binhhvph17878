import request from '@/utils/request'

const ENV = '/payment'

export const paymentOnline = (data) =>
  request({
    url: ENV,
    method: 'post',
    data
  })
