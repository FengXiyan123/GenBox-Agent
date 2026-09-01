import { afterEach, describe, expect, it } from 'vitest'
import {
  THEME_STORAGE_KEY,
  applyTheme,
  getStoredTheme,
  initializeTheme,
  toggleTheme
} from './theme'

afterEach(() => {
  document.documentElement.classList.remove('dark')
  document.documentElement.style.colorScheme = ''
  window.localStorage.clear()
})

describe('manual theme preference', () => {
  it('defaults to light and never derives the preference from the operating system', () => {
    expect(getStoredTheme()).toBe('light')
    expect(initializeTheme()).toBe('light')
    expect(document.documentElement.classList.contains('dark')).toBe(false)
  })

  it('restores only a valid persisted preference', () => {
    window.localStorage.setItem(THEME_STORAGE_KEY, 'dark')
    expect(initializeTheme()).toBe('dark')
    expect(document.documentElement.classList.contains('dark')).toBe(true)

    window.localStorage.setItem(THEME_STORAGE_KEY, 'system')
    expect(getStoredTheme()).toBe('light')
  })

  it('toggles the document class and persists the next preference', () => {
    applyTheme('light')

    expect(toggleTheme()).toBe('dark')
    expect(document.documentElement.classList.contains('dark')).toBe(true)
    expect(document.documentElement.style.colorScheme).toBe('dark')
    expect(window.localStorage.getItem(THEME_STORAGE_KEY)).toBe('dark')

    expect(toggleTheme()).toBe('light')
    expect(document.documentElement.classList.contains('dark')).toBe(false)
    expect(window.localStorage.getItem(THEME_STORAGE_KEY)).toBe('light')
  })
})
