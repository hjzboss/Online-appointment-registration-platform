<template>
  <!-- header -->
  <div class="nav-container page-component">
    <br>
    <br>
    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div class="hospital-notice">
        <div class="content">
          <h2>{{ notice_title }}</h2>
          <br>
          <p>{{ notice }}</p>
          <br v-for="(item, index) in new Array(2)" :key="index">
          <div>陕医通预约挂号平台</div>
          <br>
          <div>{{ notice_data }}</div>
          <br v-for="(item, index) in new Array(17)" :key="index">
        </div>
      </div>
    </div>
    <!-- 右侧内容 #end -->
  </div>
  <!-- footer -->
</template>

<script>
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'
import '~/assets/css/personal.css'


export default {
  data() {
    return {
      hoscode: null,
      notice: null,
      notice_title: null,
      notice_data: null
    };
  },

  created() {
    this.hoscode = this.$route.params.hoscode;
    console.log(this.hoscode)
    this.init();
  },

  methods: {
    init() {
      switch (this.hoscode) {
        case 1 :
        case '1' : {
          this.notice_title = '临潼人民医院号源暂停更新通知';
          this.notice = '因医院系统升级改造，自2021年11月18日起，暂停预约2019年11月25日以后的114预约平台（包括电话预约和网络预约）的号源，医院系统升级改造完成之后，将另行通知恢复的时间，期间给您带来的不便，敬请谅解。';
          this.notice_data = '2021年11月18日'
          break;
        }
        case 0 :
        case '0' : {
          this.notice_title = '关于延长唐都医院放假的通知';
          this.notice = '依据国务院、国家卫健委、北京市卫健委等上级文件要求，为加强新型冠状病毒感染的肺炎疫情防控工作，有效减少人员聚集，阻断疫情传播，更好保障人民群众生命安全和身体健康，现将延长2022年唐都医院春节假期的具体安排通知如下：\n' +
            '一、农历正月初七至初九为春节假期，医院按节假日门诊进行排班，2022年1月31日、2月1日上午开诊。具体出诊安排见官网和微信公众号。2月3日起正常上班。\n' +
            '二、除产科外，取消原2022年1月31日、2月1日安排的全天门诊。已预约挂号的患者，医院将按原挂号途径办理退号。 给您带来的不便敬请谅解，谢谢您的理解和支持！'
          this.notice_data = '2021年12月28日'
          break;
        }
        case 2 :
        case '2' : {
          this.notice_title = '红会医院号源暂停更新通知'
          this.notice = '因医院系统升级改造，自2022年1月18日起，暂停预约2022年1月25日以后的114预约平台（包括电话预约和网络预约）的号源，医院系统升级改造完成之后，将另行通知恢复的时间，期间给您带来的不便，敬请谅解。'
          this.notice_data = '2022年1月15日'
          break;
        }
        case 3 :
        case '3' : {
          this.notice_title = '核工业417呼吸内科门诊停诊公告'
          this.notice = '因疫情防控工作需要，院内进行环境监测、消杀，即日起我院暂时停诊，给您带来不便，敬请谅解，开诊时间另行通知！'
          this.notice_data = '2022年5月15日'
          break;
        }
        case 4 :
        case '4' : {
          this.notice_title = '西京医院老年医学科门诊停诊公告'
          this.notice = '　　为配合我市疫情防控工作，全力做好全市大规模核酸采样，即日起，我院调整如下诊疗服务：\n' +
            '\n' +
            '　　1、门诊停诊，仅开放产科门诊。\n' +
            '\n' +
            '　　2、发热门诊暂停开放。\n' +
            '\n' +
            '　　3、急诊照常开放。\n' +
            '\n' +
            '　　疫情防控，人人有责。造成不便，敬请谅解！'
          this.notice_data = '2022年5月20日'
          break;
        }
        case 5 :
        case '5' : {
          this.notice_title = '西安市中医院中西医结合心内科门诊停诊公告'
          this.notice = '根据当前疫情防控要求，西安市中医院从2022年2月3日8时起暂停门诊，暂停接收住院患者，恢复时间会及时公告。如有疑问请致电咨询84322000，感谢患者和家属的理解和支持！'
          this.notice_data = '2022年5月25日'
          break;
        }
        default : {
          this.$message.error("发生错误！请稍后再试")
          break;
        }
      }
    }
  }
}

</script>
<style>
.hospital-detail .info-wrapper {
  width: 100%;
  padding-left: 0;
  padding-top: 0;
  margin-top: 0;
  flex-direction: inherit;
}

.hospital-detail .info-wrapper .text {
  font-size: 14px;
}

.hospital-detail .content-wrapper p {
  text-indent: 0;
}
</style>
