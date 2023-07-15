<template>
  <el-table :data="socialUsers" :show-header="false">
    <el-table-column label="社交平台" align="left" width="120">
      <template v-slot="scope">
        <img style="height:20px;vertical-align: middle;" :src="scope.row.img" /> {{ scope.row.title }}
      </template>
    </el-table-column>
    <el-table-column label="操作" align="left" >
      <template v-slot="scope">
        <div v-if="scope.row.openid">
          已绑定
          <el-button size="large" type="text" @click="unbind(scope.row)">(解绑)</el-button>
        </div>
        <div v-else>
          未绑定
          <el-button size="large" type="text" @click="bind(scope.row)">(绑定)</el-button>
        </div>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { ref,reactive,computed } from 'vue'
import { propTypes } from '@/utils/propTypes.js'
import ELComponent from '@/plugins/modal.js'
import { SystemUserSocialTypeEnum } from '@/utils/constants.js'

const props = defineProps({
  user: propTypes.object,
  getUser: propTypes.func,
  setActiveTab: propTypes.func
})

const socialUsers = computed(()=>{
  const socialUsers = []
  for (const i in SystemUserSocialTypeEnum) {
    const socialUser = {...SystemUserSocialTypeEnum[i]}
    socialUsers.push(socialUser)
    if (props.user.socialUsers) {
      for (const j in props.user.socialUsers) {
        if (socialUser.type === props.user.socialUsers[j].type) {
          socialUser.openid = props.user.socialUsers[j].openid
          break
        }
      }
    }
  }
  return socialUsers
})
</script>

<style scoped>

</style>
