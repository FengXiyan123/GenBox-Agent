<template>
  <Button
    variant="ghost"
    size="sm"
    type="button"
    data-testid="theme-toggle"
    :aria-label="actionLabel"
    :title="actionLabel"
    @click="toggle"
  >
    <SunIcon v-if="isDark" data-icon="inline-start" aria-hidden="true" />
    <MoonIcon v-else data-icon="inline-start" aria-hidden="true" />
    <span class="hidden sm:inline">{{ isDark ? '浅色模式' : '深色模式' }}</span>
  </Button>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { MoonIcon, SunIcon } from '@heroicons/vue/24/outline'
import { Button } from '@/components/ui/button'
import { getStoredTheme, toggleTheme } from '@/utils/theme'

const isDark = ref(false)
const actionLabel = computed(() => isDark.value ? '切换为浅色模式' : '切换为深色模式')

onMounted(() => {
  isDark.value = getStoredTheme() === 'dark'
})

function toggle() {
  isDark.value = toggleTheme() === 'dark'
}
</script>
