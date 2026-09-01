import { defineComponent, h } from 'vue'
import { mount } from '@vue/test-utils'
import { describe, expect, it, vi } from 'vitest'
import AdminLayoutView from './AdminLayoutView.vue'

const mocks = vi.hoisted(() => ({
  replace: vi.fn(),
  logout: vi.fn()
}))

vi.mock('vue-router', () => ({
  useRoute: () => ({ path: '/admin/dashboard', meta: { title: '运营总览' } }),
  useRouter: () => ({ replace: mocks.replace }),
  RouterLink: defineComponent({
    name: 'RouterLink',
    props: { to: { type: [String, Object], default: '' } },
    setup(_props, { slots }) {
      return () => h('a', slots.default?.())
    }
  }),
  RouterView: defineComponent({ name: 'RouterView', setup: () => () => h('div') })
}))

vi.mock('../../api/api', () => ({
  adminAuthApi: { logout: mocks.logout }
}))

const slotStub = defineComponent({
  inheritAttrs: false,
  setup(_props, { attrs, slots }) {
    return () => h('div', attrs, slots.default?.())
  }
})

describe('AdminLayoutView branding', () => {
  it('renders the canonical brand in desktop and mobile navigation surfaces', () => {
    const wrapper = mount(AdminLayoutView, {
      global: {
        stubs: {
          Drawer: slotStub,
          DrawerContent: slotStub,
          DrawerHeader: slotStub,
          DrawerTitle: slotStub,
          DrawerDescription: slotStub
        }
      }
    })

    expect(wrapper.get('[data-brand-surface="desktop"]').text()).toBe('GenBox-Agent')
    expect(wrapper.get('[data-brand-surface="mobile"]').text()).toBe('GenBox-Agent')
    expect(wrapper.findAll('img[src="/GenBox_ico.png"]')).toHaveLength(2)
    expect(wrapper.get('[data-testid="theme-toggle"]').attributes('aria-label')).toBe('切换为深色模式')
    for (const tone of ['overview', 'knowledge', 'observability']) {
      expect(wrapper.get(`[data-nav-group="${tone}"]`).classes()).toContain('admin-nav-group')
    }
  })
})
