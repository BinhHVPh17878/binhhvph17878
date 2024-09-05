<template>
  <el-input
    :id="inputId"
    :value="formattedValue"
    :placeholder="placeholder"
    @input="onInput"
  />
</template>

<script>

export default {
  name: 'InputFormat',
  props: {
    value: {
      type: Number,
      default: 0
    },
    inputId: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    }
  },
  computed: {
    formattedValue() {
      return new Intl.NumberFormat('de-DE').format(this.value)
    }
  },
  watch: {
    value(newVal) {
      // Cập nhật lại giá trị đầu vào khi giá trị thực tế thay đổi
      this.$forceUpdate()
    }
  },
  methods: {
    onInput(value) {
      let numericValue = parseFloat(value.replace(/\./g, '').replace(',', '.'))
      if (isNaN(numericValue)) {
        numericValue = 0
      }
      this.$emit('input', numericValue)
    }
  }
}
</script>

<style scoped>
.el-input__inner {
  text-align: right
}
</style>
