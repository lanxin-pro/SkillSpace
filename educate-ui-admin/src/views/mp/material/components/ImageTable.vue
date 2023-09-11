<template>
  <div class="waterfall" v-loading="props.loading">
    <div class="waterfall-item" v-for="item in props.list" :key="item.id">
      <a target="_blank" :href="item.url">
        <img class="material-img" :src="item.url" />
        <div class="item-name">{{ item.name }}</div>
      </a>
      <el-row justify="center">
        <el-button
            type="danger"
            circle
            @click="emit('delete', item.id)"
            v-hasPermi="['mp:material:delete']"
        >
          <font-awesome-icon icon="fa-solid fa-trash" />
        </el-button>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { propTypes } from '@/utils/propTypes.js'

const props = defineProps({
  list: propTypes.any,
  loading: propTypes.bool
})

const emit = defineEmits(["delete"])

</script>

<style lang="scss" scoped>
// 媒体查询
@media (width >= 992px) and (width <= 1300px) {
  .waterfall {
    column-count: 3;
  }

  p {
    color: red;
  }
}

@media (width >= 768px) and (width <= 991px) {
  .waterfall {
    column-count: 2;
  }

  p {
    color: orange;
  }
}

@media (width <= 767px) {
  .waterfall {
    column-count: 1;
  }
  p {
    color: #0048ff;
  }
}

.waterfall {
  width: 100%;
  column-gap: 10px;
  // 一行 5 个元素
  column-count: 5;
  margin-top: 10px;

  /* 蓝欣：增加 10px，避免顶着上面 */
}

.waterfall-item {
  // 元素们的间隔
  padding: 10px;
  margin-bottom: 10px;
  // 控制内容在分页或分栏时的断行行为
  break-inside: avoid;
  // 画框
  border: 1px solid #eaeaea;
}

.material-img {
  width: 100%;
}

p {
  line-height: 30px;
}

.item-name {
  overflow: hidden;
  font-size: 13px;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #1c84c6;
}
</style>
