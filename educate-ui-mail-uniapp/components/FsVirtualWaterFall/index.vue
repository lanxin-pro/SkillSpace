<template>
  <view class="fs-virtual-waterfall-container">
    <view class="fs-virtual-waterfall-content" ref="contentRef">
      <view class="fs-virtual-waterfall-list" :style="contentStyle">
        <view class="fs-virtual-waterfall-item" v-for="{ item, style } in renderList" :key="item.id" :style="style">
          <slot name="item" :item="item"></slot>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { debounce, rafThrottle } from "./tool"

export default {
  name: 'fs-virtual-water-fall',
  props: {
    request: {
      type: Function,
      default: async () => ({ list: [], total: 0 })
    },
    gap: {
      type: Number,
      default: 20
    },
    column: {
      type: Number,
      default: 2
    },
    columnItemCount: {
      type: Number,
      default: 6
    },
    requestSize: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      contentRef: null,
      // 将 reactive 数据定义在这里
      dataState: {
        loading: false,
        isFinish: false,
        currentPage: 1,
        total: 0,
        list: []
      },
      columnState: {
        queue: [],
        len: 0
      },
      scrollState: {
        viewWidth: 0,
        viewHeight: 0,
        start: 0,
        // getter 方法需要转换
        get end() {
          return this.start + this.viewHeight;
        }
      },
      // 其他 state 定义...
    };
  },
  computed: {
    computedHeight(){
      let minIndex = 0,
          maxIndex = 0,
          minHeight = Infinity,
          maxHeight = -Infinity;

      this.columnState.queue.forEach(({ height }, index) => {
        if (height < minHeight) {
          minHeight = height;
          minIndex = index;
        }
        if (height > maxHeight) {
          maxHeight = height;
          maxIndex = index;
        }
      });
      // 返回一个包含 minHeight 和 maxHeight 的对象
      return {
        minIndex,
        maxIndex,
        minHeight,
        maxHeight,
      }
    },
    contentStyle(){
      // 注意这里需要直接访问 `computedHeight` 的属性，而不是 `computedHeight.value`
      return { height: `${this.computedHeight.maxHeight}px` };
    },
    columnList() {
      return this.columnState.queue.reduce((pre, { list }) => pre.concat(list), []);
    },
    renderList() {
      return this.columnList.filter(i => i.h + i.y > this.scrollState.start && i.y < this.scrollState.end);
    }
  },
  methods: {
    async init(){
      console.log("a")
      this.initScrollState();
      await this.loadDataList();
    },
    initializeQueue() {
      console.log('b')
      this.columnState.queue = Array(this.column)
          .fill(0)
          .map(() => ({ list: [], height: 0 }));
    },
    initScrollState(){
      this.scrollState.viewWidth = this.contentRef?.clientWidth || 0;
      this.scrollState.viewHeight = this.contentRef?.clientHeight || 0;
      this.scrollState.start = this.contentRef?.scrollTop || 0;
    },
    async loadDataList(){
      if (this.dataState.isFinish) return;
      this.dataState.loading = true;
      const { list, total } = await this.request(this.dataState.currentPage++, this.requestSize);
      if (!list.length) {
        this.dataState.isFinish = true;
        return;
      }
      this.dataState.list.push(...markRaw(list));
      console.log('list',list)
      this.dataState.total = total;
      this.dataState.loading = false;
    }
  },
  created: function() {
    // 初始化props中的值
    this.initializeQueue();
    this.init();
  },
}
</script>

<style scoped lang="scss">
.fs-virtual-waterfall {
  &-container {
    width: 100%;
    height: 100%;
  }

  &-content {
    width: 100%;
    height: 100%;
    overflow-y: scroll;
  }
  &-list {
    position: relative;
    width: 100%;
  }
  &-item {
    position: absolute;
    top: 0;
    left: 0;
    transition: all 0.3s;
    box-sizing: border-box;
  }
}
</style>
