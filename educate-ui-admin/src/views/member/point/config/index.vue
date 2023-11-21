<template>
  <div class="app-container">
    <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        v-loading="formLoading"
    >
      <el-tabs>
        <el-tab-pane label="积分配置">
          <el-form-item label="积分抵扣" prop="tradeDeductEnable">
            <el-switch v-model="formData.pointTradeDeductEnable" style="user-select: none" />
            <el-text class="w-full" size="small" type="info">下单积分是否抵用订单金额</el-text>
          </el-form-item>
          <el-form-item label="积分抵扣" prop="tradeDeductUnitPrice">
            <el-input-number
                v-model="computedTradeDeductUnitPrice"
                placeholder="请输入积分抵扣金额"
                :step="0.1"
                :precision="2"
            />
            <el-text class="w-full" size="small" type="info">
              积分抵用比例(1 积分抵多少金额)，单位：【元】
            </el-text>
          </el-form-item>
          <el-form-item label="积分抵扣最大值" prop="tradeDeductMaxPrice">
            <el-input-number
                v-model="formData.pointTradeDeductMaxPrice"
                placeholder="请输入积分抵扣最大值"
            />
            <el-text class="w-full" size="small" type="info">
              单次下单积分使用上限，【0】 <span style="color: #ec5656">不限制</span>
            </el-text>
          </el-form-item>
          <el-form-item label="1 元赠送多少分" prop="tradeGivePoint">
            <el-input-number
                v-model="formData.pointTradeGivePoint"
                placeholder="请输入 1 元赠送多少积分"
            />
            <el-text class="w-full" size="small" type="info">
              下单支付金额按比例赠送积分（实际支付 1 【元】赠送多少积分）
            </el-text>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>

      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import ELComponent from '@/plugins/modal.js'
import { ref, reactive,onMounted,computed } from 'vue'
import { saveConfig, getDBConfig } from '@/api/member/point/config/index.js'

onMounted(() => {
  getConfig()
})

// 弹窗的是否展示
const dialogVisible = ref(false)
// 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formLoading = ref(false)
const formData = ref({
  id: undefined,
  // 积分抵扣开关
  pointTradeDeductEnable: true,
  // 积分抵扣
  pointTradeDeductUnitPrice: 0,
  // 积分抵扣最大值
  pointTradeDeductMaxPrice: 0,
  // 元赠送多少分
  pointTradeGivePoint: 0
})

// 创建一个计算属性，用于将 tradeDeductUnitPrice 显示为带两位小数的形式
const computedTradeDeductUnitPrice = computed({
  get: () => (formData.value.pointTradeDeductUnitPrice / 100).toFixed(2),
  set: (newValue) => {
    formData.value.pointTradeDeductUnitPrice = Math.round(newValue * 100)
  }
})

const formRules = reactive({})
// 表单 Ref
const formRef = ref()

/** 修改积分配置 */
const onSubmit = async () => {
  // 校验表单
  if (!formRef) {
    return
  }
  const valid = await formRef.value.validate()
  if (!valid) {
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value
    console.log('开始执行保存data.pointTradeGivePoint',data.pointTradeDeductUnitPrice)
    console.log('开始执行保存data',data)
    await saveConfig(data)
    ELComponent.msgSuccess('更新成功！')
    dialogVisible.value = false
  } finally {
    formLoading.value = false
  }
}

/** 获得积分配置 */
const getConfig = async () => {
  try {
    const response = await getDBConfig()
    if (response.data === null) {
      return
    }
    formData.value = response.data
  } finally {
  }
}
</script>

<style scoped>

</style>
