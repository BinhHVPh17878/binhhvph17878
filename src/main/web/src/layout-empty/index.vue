<template>
  <router-view :key="key" />
</template>

<script>

export default {
  name: 'LayoutEmpty',
  components: {},
  computed: {
    key() {
      return this.$route.path
    }
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
</style>
