package com.backbase.mastercard.config;

import com.mastercard.openbanking.accounts.ApiClient;
import com.mastercard.openbanking.accounts.api.AccountBalancesApi;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccountsApiProperties.class)
public class AccountsApiConfiguration {

    @Bean
    public ApiClient apiClient(AccountsApiProperties accountsApiProperties) {
        var apiClient = new ApiClient();
        apiClient.updateBaseUri(accountsApiProperties.getBaseUri());
        return apiClient;
    }

    @Bean
    public AccountBalancesApi accountBalancesApi(ApiClient apiClient) {
        return new AccountBalancesApi(apiClient);
    }
}
