<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="text-center">
              <UserAvatar :img="user?.avatar" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <SvgIcon icon-class="fa-solid fa-user-pen" style="color: #437397" /> 用户名称
                <div class="pull-right">{{ user.nickname }}</div>
              </li>
              <li class="list-group-item">
                <SvgIcon icon-class="fa-solid fa-mobile-screen" style="color: #F29A43" /> 手机号码
                <div class="pull-right">{{ user.mobile }}</div>
              </li>
              <li class="list-group-item">
                <SvgIcon icon-class="fa-regular fa-envelope" style="color: #F8DC6A" /> 用户邮箱
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <SvgIcon icon-class="fa-regular fa-building" style="color: #DA9180" /> 所属部门
                <div class="pull-right" v-if="user.dept">{{ user.dept.name }}</div>
              </li>
              <li class="list-group-item">
                <SvgIcon icon-class="fa-regular fa-address-card" style="color: #C6B89B" /> 所属岗位
                <div class="pull-right" v-if="user.posts">{{ user.posts.map(post => post.name).join(',') }}</div>
              </li>
              <li class="list-group-item">
                <SvgIcon icon-class="fa-solid fa-user-tie" style="color: #CCDAE5" /> 所属角色
                <div class="pull-right" v-if="user.roles">{{ user.roles.map(role => role.name).join(',') }}</div>
              </li>
              <li class="list-group-item">
                <SvgIcon icon-class="fa-regular fa-calendar" style="color: #4B709A" /> 创建日期
                <div class="pull-right">{{ parseTime(user.createTime) }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <UserInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <ResetPwd :user="user" />
            </el-tab-pane>
            <el-tab-pane label="社交信息" name="userSocial">
              <UserSocial :user="user" :getUser="getUser" :setActiveTab="setActiveTab" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref,onMounted } from 'vue'
import { parseTime } from '@/utils/ruoyi.js'
import UserAvatar from './userAvatar.vue'
import ResetPwd from './resetPwd.vue'
import UserInfo from './userInfo.vue'
import UserSocial from './userSocial.vue'
import SvgIcon from '@/components/SvgIcon/index.vue'
import { getUserProfile } from '@/api/system/user.js'

const user = ref({})
const roleGroup = ref({})
const postGroup = ref({})
const activeTab = ref("userinfo")

onMounted(()=>{
  getUser()
})

const getUser = (async ()=>{
  const response = await getUserProfile()
  user.value = response.data
})
</script>

<style scoped>

</style>
