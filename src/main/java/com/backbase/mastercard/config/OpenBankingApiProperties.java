package com.backbase.mastercard.config;

import java.util.Optional;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mastercard.mcob.api")
public class OpenBankingApiProperties {

    Optional<String> baseUri = Optional.empty();
    Proxy proxy = new Proxy();

    @Data
    public static class Proxy {

        Boolean enabled = false;
        String host;
        Integer port;
    }

}
