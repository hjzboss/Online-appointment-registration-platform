<template>
  <div class="nav-container page-component">
    <!--左侧导航 #start -->
    <div class="nav left-nav">
      <div class="nav-item ">
        <span class="v-link clickable dark" :onclick="'javascript:window.location=\'/hospital/'+hospital.hoscode+'\''">预约挂号 </span>
      </div>
      <div class="nav-item">
        <span class="v-link clickable dark"
              :onclick="'javascript:window.location=\'/hospital/detail/'+hospital.hoscode+'\''"> 医院详情 </span>
      </div>
      <div class="nav-item">
        <span class="v-link clickable dark"
              :onclick="'javascript:window.location=\'/hospital/notice/'+hospital.hoscode+'\''"> 预约须知 </span>
      </div>
      <div class="nav-item selected"><span
        class="v-link selected dark"
        :onclick="'javascript:window.location=\'/hospital/comment/'+hospital.hoscode+'\''"> 评价 </span>
      </div>
    </div>
    <!-- 左侧导航 #end -->

    <!-- 右侧内容 #start -->
    <div class="page-container">
      <div class="hospital-detail">
        <div class="common-header">
          <div class="title-wrapper"><span class="hospital-title">{{ hospital.hosname }}</span>
            <div class="icon-wrapper"><span class="iconfont"></span> {{ hospital.param.hostypeString }}</div>
          </div>
        </div>
        <div class="info-wrapper"><img :src="'data:image/jpeg;base64,'+hospital.logoData" :alt="hospital.hosname"
                                       style="width: 80px; height: 80px;">
          <div class="content-wrapper">
            <div></div>
            <div></div>
            <div></div>
            <div>
              <br>
              <div class="icon-text-wrapper"><span class="iconfont prefix-icon">总体评分</span>
                <span class="text">
                  <p>
                    <el-rate
                      v-model="value"
                      disabled
                      show-score
                      text-color="#ff9900"
                      score-template="{value}">
                    </el-rate>
                  </p>
                </span>
                <span class="iconfont right-icon"></span></div>
            </div>
          </div>
        </div>
        <div class="title mt40"> 就诊患者评价</div>
        <div class="detail-content mt40">
          <el-table
            :data="tableData"
            height="350"
            border
            style="width: 100%">
            <el-table-column
              prop="date"
              label="就诊日期"
              width="180">
            </el-table-column>
            <el-table-column
              prop="name"
              label="患者"
              width="180">
            </el-table-column>
            <el-table-column
              label="评价">
              <el-rate
                v-model="value"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}">
              </el-rate>
            </el-table-column>
          </el-table>
        </div>
        <br>
        <br>
      </div>
    </div>
    <!-- 右侧内容 #end -->
  </div>
</template>

<script>
import '~/assets/css/hospital_personal.css'
import '~/assets/css/hospital.css'
import hospitalApi from "@/api/hosp/hosp";

export default {
  data() {
    return {
      hoscode: null,
      hospital: {
        param: {},
      },
      value: 3.7,
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }]
    };
  },

  created() {
    this.hoscode = this.$route.params.hoscode;

    this.init();
  },

  methods: {
    init() {
      hospitalApi.show(this.hoscode).then((response) => {
        this.hospital = response.data.hospital;
      });
    },
  },
};

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
