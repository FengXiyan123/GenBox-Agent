export const THEME_STORAGE_KEY = 'genbox-agent-theme'

const LIGHT_THEME = 'light'
const DARK_THEME = 'dark'

function normalizeTheme(theme) {
  return theme === DARK_THEME ? DARK_THEME : LIGHT_THEME
}

function getStorage() {
  return typeof window === 'undefined' ? null : window.localStorage
}

function getDocumentRoot() {
  return typeof document === 'undefined' ? null : document.documentElement
}

export function getStoredTheme() {
  return normalizeTheme(getStorage()?.getItem(THEME_STORAGE_KEY))
}

export function applyTheme(theme, root = getDocumentRoot()) {
  const nextTheme = normalizeTheme(theme)
  if (root) {
    root.classList.toggle('dark', nextTheme === DARK_THEME)
    root.style.colorScheme = nextTheme
  }
  return nextTheme
}

export function initializeTheme() {
  return applyTheme(getStoredTheme())
}

export function toggleTheme() {
  const nextTheme = getDocumentRoot()?.classList.contains('dark') ? LIGHT_THEME : DARK_THEME
  getStorage()?.setItem(THEME_STORAGE_KEY, nextTheme)
  return applyTheme(nextTheme)
}
