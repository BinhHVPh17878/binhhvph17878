export function formatCurrencyVND(value) {
  value = (value !== null && value !== undefined && !isNaN(value)) ? value : 0
  return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}
