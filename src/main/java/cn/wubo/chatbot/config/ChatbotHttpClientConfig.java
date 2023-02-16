/*
 * Copyright ©2015-2022 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.wubo.chatbot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class ChatbotHttpClientConfig {

    /**
     * 连接超时时间
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private final Duration connectTimeout = Duration.ofSeconds(30);
    /**
     * 读超时时间
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private final Duration readTimeout = Duration.ofSeconds(30);

    @Bean(name = "chatbotRestTemplate")
    public RestTemplate chatbotRestTemplate(@Qualifier("chatbotClientHttpRequestFactory") ClientHttpRequestFactory chatbotClientHttpRequestFactory) {
        return new RestTemplate(chatbotClientHttpRequestFactory);
    }

    @Bean(name = "chatbotClientHttpRequestFactory")
    public ClientHttpRequestFactory chatbotClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout((int) readTimeout.toMillis());
        factory.setConnectTimeout((int) connectTimeout.toMillis());
        return factory;
    }
}