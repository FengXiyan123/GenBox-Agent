import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import { describe, expect, it } from 'vitest'
import {
  ADMIN_TOKEN_KEY,
  ADMIN_USER_KEY,
  clearAdminAuth,
  getAdminToken,
  getAdminUsername,
  saveAdminAuth
} from './utils/adminAuthStorage'

const PACKAGE_NAME = 'genbox-agent-business-chat-ui'
const DISPLAY_NAME = 'GenBox-Agent'
const EVIDENCE_DIR_ENV = 'GENBOX_AGENT_PLAYWRIGHT_EVIDENCE_DIR'
const LEGACY_DISPLAY_PATTERN = new RegExp(['nex', 'us|su', 'per-', 'agent|超', '级智能'].join(''), 'i')
const LEGACY_STORAGE_PATTERN = new RegExp(['(?:nex', 'us|su', 'per)-agent-admin-(?:token|user)'].join(''), 'i')
const LEGACY_EVIDENCE_ENV_PATTERN = new RegExp(['NEX', 'US_PLAYWRIGHT_EVIDENCE_DIR'].join(''))

function readProjectFile(path) {
  return readFileSync(resolve(process.cwd(), path), 'utf8')
}

describe('frontend branding contract', () => {
  it('uses the canonical package name in package metadata', () => {
    const packageJson = JSON.parse(readProjectFile('package.json'))
    const packageLock = JSON.parse(readProjectFile('package-lock.json'))

    expect(packageJson.name).toBe(PACKAGE_NAME)
    expect(packageLock.name).toBe(PACKAGE_NAME)
    expect(packageLock.packages[''].name).toBe(PACKAGE_NAME)
  })

  it('uses the supplied PNG as the browser favicon', () => {
    expect(readProjectFile('index.html')).toContain('href="/GenBox_ico.png"')
    expect(readProjectFile('index.html')).toContain('type="image/png"')
  })

  it('uses the canonical display name on every owned brand surface', () => {
    const surfaces = [
      ['index.html', `<title>${DISPLAY_NAME}</title>`],
      ['design-system.html', `<title>${DISPLAY_NAME} Design System Lab</title>`],
      ['src/App.vue', `>${DISPLAY_NAME}</h1>`],
      ['src/views/AdminLoginView.vue', `>${DISPLAY_NAME}</p>`],
      ['src/views/admin/AdminLayoutView.vue', `>${DISPLAY_NAME}</strong>`],
      ['src/design-system/DesignSystemLab.vue', `title="${DISPLAY_NAME} 设计系统实验台"`]
    ]

    for (const [path, expected] of surfaces) {
      const source = readProjectFile(path)
      expect(source, path).toContain(expected)
      expect(source, path).not.toMatch(LEGACY_DISPLAY_PATTERN)
    }
  })

  it('uses canonical runtime identifiers in application and e2e support code', () => {
    expect(ADMIN_TOKEN_KEY).toBe('genbox-agent-admin-token')
    expect(ADMIN_USER_KEY).toBe('genbox-agent-admin-user')

    saveAdminAuth({ token: 'signed-token', username: 'operator' })
    expect(getAdminToken()).toBe('signed-token')
    expect(getAdminUsername()).toBe('operator')
    clearAdminAuth()
    expect(getAdminToken()).toBe('')
    expect(getAdminUsername()).toBe('admin')

    const storageSource = readProjectFile('src/utils/adminAuthStorage.js')
    expect(storageSource).toContain(`'${ADMIN_TOKEN_KEY}'`)
    expect(storageSource).toContain(`'${ADMIN_USER_KEY}'`)

    for (const path of ['src/api/api.js', 'src/utils/adminAuth.js']) {
      const source = readProjectFile(path)
      expect(source, path).toContain('adminAuthStorage')
      expect(source, path).not.toContain(`'${ADMIN_TOKEN_KEY}'`)
      expect(source, path).not.toContain(`'${ADMIN_USER_KEY}'`)
      expect(source, path).not.toMatch(LEGACY_STORAGE_PATTERN)
    }

    const fixtureSource = readProjectFile('e2e/fixtures/mockApp.js')
    expect(fixtureSource).toContain(`'${ADMIN_TOKEN_KEY}'`)
    expect(fixtureSource).toContain(`'${ADMIN_USER_KEY}'`)
    expect(fixtureSource).not.toMatch(LEGACY_STORAGE_PATTERN)

    for (const path of ['playwright.config.js', 'e2e/f09.spec.js']) {
      const source = readProjectFile(path)
      expect(source, path).toContain(EVIDENCE_DIR_ENV)
      expect(source, path).not.toMatch(LEGACY_EVIDENCE_ENV_PATTERN)
    }
  })
})
