import { describe, expect, it } from 'vitest'

describe('test environment storage contract', () => {
  it('provides native Storage semantics at a non-opaque origin', () => {
    expect(window.location.origin).toBe('http://localhost:3000')
    expect(window.localStorage).toBeInstanceOf(Storage)
    expect(window.localStorage).toBe(globalThis.jsdom.window.localStorage)

    window.localStorage.setItem('numeric-key', 42)
    expect(window.localStorage.getItem('numeric-key')).toBe('42')
    expect(window.localStorage.key(0)).toBe('numeric-key')
    expect(window.localStorage.length).toBe(1)

    window.localStorage.removeItem('numeric-key')
    expect(window.localStorage.getItem('numeric-key')).toBeNull()
  })
})
