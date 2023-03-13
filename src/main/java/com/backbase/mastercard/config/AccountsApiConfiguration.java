package com.backbase.mastercard.config;

import com.backbase.mastercard.config.AccountsApiProperties.Proxy;
import com.mastercard.mcob.ais.ApiClient;
import com.mastercard.mcob.ais.api.AccountBalancesApi;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(AccountsApiProperties.class)
public class AccountsApiConfiguration {

    @Bean
    public ApiClient apiClient(AccountsApiProperties accountsApiProperties) {
        ApiClient apiClient = new ApiClient();
        accountsApiProperties.getBaseUri()
            .ifPresent(uri -> {
                log.debug("Configuring api with Base Uri: {}", uri);
                apiClient.updateBaseUri(uri);
            });
        return configureProxy(apiClient, accountsApiProperties.getProxy());
    }

    @Bean
    public AccountBalancesApi accountBalancesApi(ApiClient apiClient) {
        return new AccountBalancesApi(apiClient);
    }

    private ApiClient configureProxy(ApiClient apiClient, Proxy proxy) {
        if (proxy.getEnabled()) {
            log.debug("Enabling proxy configuration: {}", proxy);
            HttpClient.Builder builder = HttpClient.newBuilder();
            builder.proxy(ProxySelector.of(new InetSocketAddress(proxy.getHost(), proxy.getPort())));
            return apiClient.setHttpClientBuilder(builder);
        }
        return apiClient;
    }
}
