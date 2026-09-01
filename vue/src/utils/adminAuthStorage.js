export const ADMIN_TOKEN_KEY = 'genbox-agent-admin-token'
export const ADMIN_USER_KEY = 'genbox-agent-admin-user'

export function getAdminToken() {
  return window.localStorage.getItem(ADMIN_TOKEN_KEY) || ''
}

export function getAdminUsername() {
  return window.localStorage.getItem(ADMIN_USER_KEY) || 'admin'
}

export function saveAdminAuth(payload = {}) {
  if (payload.token) {
    window.localStorage.setItem(ADMIN_TOKEN_KEY, payload.token)
  }
  if (payload.username) {
    window.localStorage.setItem(ADMIN_USER_KEY, payload.username)
  }
}

export function clearAdminAuth() {
  window.localStorage.removeItem(ADMIN_TOKEN_KEY)
  window.localStorage.removeItem(ADMIN_USER_KEY)
}
