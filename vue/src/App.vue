<template>
  <ConfirmDialog />
  <router-view v-if="isFullscreenLayout" />

  <div v-else class="app-shell">
    <header class="app-header">
      <div class="brand-lockup">
        <div class="brand-mark" aria-hidden="true">GA</div>
        <h1 class="app-title">GenBox-Agent</h1>
      </div>
      <ThemeToggle />
    </header>

    <main class="app-main">
      <router-view />
    </main>

    <IcpFooter class="app-footer" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import IcpFooter from './components/IcpFooter.vue'
import ConfirmDialog from './components/ConfirmDialog.vue'
import ThemeToggle from './components/system/ThemeToggle.vue'

const route = useRoute()

const isFullscreenLayout = computed(() => route.meta?.layout === 'fullscreen')
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
  padding: 16px 24px 24px;
}

.app-header {
  max-width: 1440px;
  margin: 0 auto 12px;
  min-height: 52px;
  display: flex;
  align-items: center;
  padding: 0 2px 10px;
  border-bottom: 1px solid var(--border);
}

.brand-lockup {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.app-header :deep([data-testid='theme-toggle']) {
  margin-left: auto;
}

.brand-mark {
  width: 28px;
  height: 28px;
  flex: none;
  display: grid;
  place-items: center;
  border-radius: var(--radius-sm);
  background: var(--primary);
  color: var(--primary-foreground);
  font-size: var(--text-compact);
  font-weight: 800;
}

.app-title {
  margin: 0;
  font-size: var(--text-body);
  line-height: 1;
  font-weight: 700;
  color: var(--foreground);
}

.app-main {
  max-width: 1440px;
  margin: 0 auto;
}

.app-footer {
  max-width: 1440px;
  margin: 16px auto 0;
  padding-bottom: 2px;
}

@media (max-width: 960px) {
  .app-shell {
    padding: 14px 18px 18px;
  }

  .app-header {
    margin-bottom: 10px;
    padding-bottom: 8px;
  }
}
</style>
