<template>
  <a-result
    status="success"
    :isSuccess="true"
    :content="false"
    :title="username"
    :sub-title="description">

    <template #extra>
      <a-button size="large" type="primary" @click="goHomeHandle">返回登录页</a-button>
    </template>

  </a-result>
</template>

<script>
export default {
  name: 'RegisterResult',
  data () {
    return {
      description: '请及时登录系统，在个人设置个修改资料。',
      form: {}
    }
  },
  computed: {
    username () {
      const v = this.form && this.form.username || 'xxx'
      return `你的账户：${v} 注册成功`
    }
  },
  created () {
    this.form = this.$route.params
  },
  methods: {
    goHomeHandle () {
      this.$router.push({ name: 'login' })
    }
  }
}
</script>

<style scoped>

</style>
