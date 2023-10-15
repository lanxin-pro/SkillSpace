<template>
  <ElDropdown
      ref="elDropdownMenuRef"
      :trigger="trigger"
      placement="bottom-start"
      popper-class="v-context-menu-popper"
      @command="command"
      @visible-change="visibleChange"
  >
    <slot></slot>
    <template #dropdown>
      <ElDropdownMenu>
        <ElDropdownItem
            v-for="(item, index) in schema"
            :key="`dropdown${index}`"
            :command="item"
            :disabled="item.disabled"
            :divided="item.divided"
        >
          <Icon :icon="item.icon" />
          {{ item.label }}
        </ElDropdownItem>
      </ElDropdownMenu>
    </template>
  </ElDropdown>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['visibleChange'])

const props = defineProps({
  schema: {
    type: Array,
    default: () => []
  },
  trigger: {
    type: String,
    default: 'contextmenu'
  },
  tagItem: {
    type: Object,
    default: () => ({})
  }
})

const command = (item) => {
  item.command && item.command(item)
}

const visibleChange = (visible) => {
  emit('visibleChange', visible, props.tagItem)
}

const elDropdownMenuRef = ref()

defineExpose({
  elDropdownMenuRef,
  tagItem: props.tagItem
})
</script>

<style scoped>

</style>
