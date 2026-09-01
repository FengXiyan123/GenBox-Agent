import { afterEach } from 'vitest'
import { enableAutoUnmount } from '@vue/test-utils'

enableAutoUnmount(afterEach)

// Node 26 exposes an experimental global localStorage getter. Vitest sees that key and
// does not proxy jsdom's implementation onto globalThis, so restore the native jsdom
// Storage object explicitly rather than replacing it with a mock.
Object.defineProperty(globalThis, 'localStorage', {
  configurable: true,
  value: globalThis.jsdom.window.localStorage
})

afterEach(() => {
  document.body.style.overflow = ''
  window.localStorage.clear()
})

if (!window.matchMedia) {
  window.matchMedia = (query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener() {},
    removeListener() {},
    addEventListener() {},
    removeEventListener() {},
    dispatchEvent() { return false }
  })
}
