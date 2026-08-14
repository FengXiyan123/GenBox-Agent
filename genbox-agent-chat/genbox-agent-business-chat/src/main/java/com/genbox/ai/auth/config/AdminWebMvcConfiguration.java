package com.genbox.ai.auth.config;

import com.genbox.ai.auth.support.AdminAuthInterceptor;
import com.genbox.ai.auth.support.PreviewModeInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 后台管理登录与预览模式的 MVC 配置。
 */
@Configuration
@EnableConfigurationProperties({AdminAuthProperties.class, PreviewModeProperties.class})
public class AdminWebMvcConfiguration implements WebMvcConfigurer {

    private final AdminAuthInterceptor adminAuthInterceptor;

    private final PreviewModeInterceptor previewModeInterceptor;

    public AdminWebMvcConfiguration(AdminAuthInterceptor adminAuthInterceptor,
                                    PreviewModeInterceptor previewModeInterceptor) {
        this.adminAuthInterceptor = adminAuthInterceptor;
        this.previewModeInterceptor = previewModeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor)
            .addPathPatterns("/manage/**", "/admin/auth/me");

        registry.addInterceptor(previewModeInterceptor)
            .addPathPatterns("/**");
    }
}
