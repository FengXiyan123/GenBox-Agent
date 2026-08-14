package com.genbox.config;

import java.util.List;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自动配置类。
 */
public class GenBoxAgentCommonAutoConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustom() {
        return new JacksonCustom();
    }

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnClass(WebMvcConfigurer.class)
    @ConditionalOnBean(ObjectMapper.class)
    public WebMvcConfigurer webMvcJacksonConfigurer(ObjectMapper objectMapper) {
        return new WebMvcConfigurer() {
            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

                MappingJackson2HttpMessageConverter mvcJacksonConverter =
                    new MappingJackson2HttpMessageConverter(createMvcObjectMapper(objectMapper));

                for (int i = 0; i < converters.size(); i++) {
                    if (converters.get(i) instanceof MappingJackson2HttpMessageConverter existingConverter) {

                        mvcJacksonConverter.setSupportedMediaTypes(existingConverter.getSupportedMediaTypes());
                        converters.set(i, mvcJacksonConverter);
                        return;
                    }
                }

                converters.add(0, mvcJacksonConverter);
            }
        };
    }

    private ObjectMapper createMvcObjectMapper(ObjectMapper objectMapper) {
        ObjectMapper mvcObjectMapper = objectMapper.copy();
        mvcObjectMapper.setSerializerFactory(
            mvcObjectMapper.getSerializerFactory().withSerializerModifier(new JsonCustomSerializer())
        );
        mvcObjectMapper.getFactory().configure(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature(), true);
        return mvcObjectMapper;
    }
}
