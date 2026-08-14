package com.genbox.ai.chatagent.support;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

/**
 * 支撑组件。
 */
public final class RestClientFactorySupport {

    private RestClientFactorySupport() {
    }

    public static RestClient create(String baseUrl, int connectTimeoutMs, int readTimeoutMs) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        if (connectTimeoutMs > 0) {
            requestFactory.setConnectTimeout(connectTimeoutMs);
        }
        if (readTimeoutMs > 0) {
            requestFactory.setReadTimeout(readTimeoutMs);
        }

        RestClient.Builder builder = RestClient.builder().requestFactory(requestFactory);
        if (StringUtils.hasText(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        return builder.build();
    }
}
