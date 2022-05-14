<template>
  <div :style="note">
    <div class="dashboard-text" align="center">
      <br>
      陕医通后台管理系统
      <br>
      V1.0
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name',
      'roles'
    ])
  },
  data() {
    return {
      src: '@/assets/yygh.jpg',
      note: {
        backgroundImage: "url(" + require("../../assets/2.jpg") + ")",
        // 背景图片是否重复
        backgroundRepeat: 'no-repeat',
        // 背景图片大小
        backgroundSize: '100% 100%',

        backgroundAttachment: "fixed"
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
div{	/* div的CSS样式 */
  position : absolute;
  width : 100%;
  height : 100%;
}
*{		/* CSS Reset */
  margin : 0;
  padding : 0;
}
</style>
