import { defineComponent, h } from 'vue'
import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import ConversationNavigation from './ConversationNavigation.vue'

const slotStub = defineComponent({
  inheritAttrs: false,
  setup(_props, { attrs, slots }) {
    return () => h('div', attrs, slots.default?.())
  }
})

describe('ConversationNavigation branding', () => {
  it('renders the GenBox icon in the desktop sidebar header', () => {
    const wrapper = mount(ConversationNavigation, {
      global: {
        stubs: {
          Drawer: slotStub,
          DrawerContent: slotStub,
          DrawerHeader: slotStub,
          DrawerTitle: slotStub,
          DrawerDescription: slotStub,
          DrawerClose: slotStub
        }
      }
    })

    const logo = wrapper.get('aside header img[src="/GenBox_ico.png"]')
    expect(logo.attributes('alt')).toBe('GenBox-Agent')
    expect(wrapper.get('aside [data-testid="chat-sidebar-icp"]').text()).toContain('晋ICP备2026011999号-1')
  })
})
