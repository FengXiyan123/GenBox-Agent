package com.genbox.ai.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import com.genbox.ai.auth.dto.AdminLoginRequest;
import com.genbox.ai.auth.vo.AdminLoginVo;
import com.genbox.ai.auth.vo.AdminProfileVo;

/**
 * 后台登录认证服务。
 */
public interface AdminAuthService {

    AdminLoginVo login(AdminLoginRequest request);

    AdminProfileVo currentProfile(HttpServletRequest request);
}
