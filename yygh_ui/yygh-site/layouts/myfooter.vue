<template>
  <div class="footer-container">
    <div class="wrapper">
      <div>
        <span class="record">陕ICP备13018369号</span>
        <span class="phone">电话挂号010-56253825</span>
      </div>
      <div class="right">
                <span class="v-link clickable" @click="$message.warning('作者：黄俊哲')">
                    项目作者
                </span>
        <span class="v-link clickable" @click="$message.info('没有合作伙伴')">
                    合作伙伴
                </span>
        <span class="v-link clickable" @click="$message.info('没有合作伙伴')">
                    用户协议
                </span>
        <span class="v-link clickable" @click="$message.info('没有合作伙伴')">
                    隐私协议
                </span>
      </div>
    </div>
  </div>
</template>
<script>
export default {}
</script>
