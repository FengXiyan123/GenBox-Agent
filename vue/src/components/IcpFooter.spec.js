import { mount } from '@vue/test-utils'
import { describe, expect, it } from 'vitest'
import IcpFooter from './IcpFooter.vue'

describe('IcpFooter', () => {
  it('renders the updated ICP record as an external filing link', () => {
    const wrapper = mount(IcpFooter)

    expect(wrapper.get('footer').attributes('aria-label')).toBe('网站备案信息')
    expect(wrapper.get('a').text()).toBe('晋ICP备2026011999号-1')
    expect(wrapper.get('a').attributes('href')).toBe('https://beian.miit.gov.cn/')
  })
})
