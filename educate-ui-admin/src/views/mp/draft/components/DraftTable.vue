<template>

  <div class="waterfall" v-loading="props.loading">
    <template v-for="item in props.list" :key="item.articleId">
      <div class="waterfall-item" v-if="item.content && item.content.newsItem">
        <WxNews :articles="item.content.newsItem" />
        <!-- 操作按钮 -->
        <el-row class="operateButton">

          <el-button
              type="success"
              circle
              @click="emit('publish', item)"
              v-hasPermi="['mp:free-publish:submit']"
          >
            <font-awesome-icon icon="fa-solid fa-cloud-arrow-up" />
          </el-button>
          <el-button
              type="primary"
              circle
              @click="emit('update', item)"
              v-hasPermi="['mp:draft:update']"
          >
            <font-awesome-icon icon="fa-regular fa-pen-to-square" />
          </el-button>
          <el-button
              type="danger"
              circle
              @click="emit('delete', item)"
              v-hasPermi="['mp:draft:delete']"
          >
            <font-awesome-icon icon="fa-solid fa-trash-can" />
          </el-button>
        </el-row>
      </div>
    </template>
  </div>

</template>

<script setup>
import WxNews from '@/views/mp/components/wx-news/index.vue'
import { propTypes } from '@/utils/propTypes.js'

const props = defineProps({
  list: propTypes.any,
  loading: propTypes.bool
})

const emit = defineEmits(["publish","update","delete"])

</script>

<style lang="scss" scoped>
.waterfall-item .operateButton {
  display: none;
}
.waterfall-item:hover .operateButton {
  display: block;
  position: absolute;
  top: 50px;
  left: 50%;
  transform: translate(-50%, -50%);
}

.waterfall {
  width: 100%;
  column-gap: 10px;
  column-count: 5;
  margin: 0 auto;
  padding-top: 15px;

  .waterfall-item {
    padding: 10px;
    margin-bottom: 10px;
    break-inside: avoid;
    border: 1px solid #eaeaea;
    // 为了给子级元素来定位
    position: relative;

  }
}

@media (width >= 992px) and (width <= 1300px) {
  .waterfall {
    column-count: 3;
  }
}

@media (width >= 768px) and (width <= 991px) {
  .waterfall {
    column-count: 2;
  }
}

@media (width <= 767px) {
  .waterfall {
    column-count: 1;
  }
}
</style>
