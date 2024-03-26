package com.backbase.mastercard.config;

import com.backbase.mastercard.config.OpenBankingApiProperties.Proxy;
import com.mastercard.mcob.ApiClient;
import com.mastercard.mcob.ais.api.AccountBalancesApi;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Configuration
@EnableConfigurationProperties(OpenBankingApiProperties.class)
public class OpenBankingApiConfiguration {

    @Bean
    public ApiClient apiClient(OpenBankingApiProperties openBankingApiProperties) {
        ApiClient apiClient = new ApiClient();
        openBankingApiProperties.getBaseUri()
            .ifPresent(uri -> {
                log.debug("Configuring MCOB API client with Base Uri: {}", uri);
                apiClient.updateBaseUri(uri);
            });
        return configureApiClientProxy(apiClient, openBankingApiProperties.getProxy());
    }

    @Bean
    public AccountBalancesApi accountBalancesApi(ApiClient apiClient) {
        return new AccountBalancesApi(apiClient);
    }

    private static ApiClient configureApiClientProxy(ApiClient apiClient, Proxy proxy) {
        if (proxy.getEnabled()) {
            log.debug("Enabling proxy configuration: {}", proxy);
            HttpClient.Builder builder = HttpClient.newBuilder();
            builder.proxy(ProxySelector.of(new InetSocketAddress(proxy.getHost(), proxy.getPort())));
            return apiClient.setHttpClientBuilder(builder);
        }
        return apiClient;
    }
}
