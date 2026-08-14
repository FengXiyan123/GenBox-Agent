package com.genbox.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger 配置类。
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("前端使用")
                        .version("1.0")
                        .description("项目学习"));
    }
}
