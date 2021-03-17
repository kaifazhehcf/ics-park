<template>
  <div class="ant-pro-pages-account-projects-cardList">
    <a-list>
    </a-list>
  </div>
</template>

<script>

export default {
  name: 'Project',
  components: {

  },
  data () {
    return {
      data: [],
      form: this.$form.createForm(this),
      loading: true
    }
  },
  filters: {
  },
  mounted () {

  },
  methods: {

  }
}
</script>

<style lang="less" scoped>
  .ant-pro-pages-account-projects-cardList {
    margin-top: 24px;

    /deep/ .ant-card-meta-title {
      margin-bottom: 4px;
    }

    /deep/ .ant-card-meta-description {
      height: 44px;
      overflow: hidden;
      line-height: 22px;
    }

    .cardItemContent {
      display: flex;
      height: 20px;
      margin-top: 16px;
      margin-bottom: -4px;
      line-height: 20px;

      > span {
        flex: 1 1;
        color: rgba(0,0,0,.45);
        font-size: 12px;
      }

      /deep/ .ant-pro-avatar-list {
        flex: 0 1 auto;
      }
    }
  }
</style>
