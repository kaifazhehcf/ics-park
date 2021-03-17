<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <!-- <a href="https://gitee.com/zhangmrit/ruoyi-cloud" target="_blank">RuoYi Cloud</a>
      <a href="https://gitee.com/zhangmrit/ruoyi-ant" target="_blank">RuoYi Ant</a> -->
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter
  }
}
</script>
