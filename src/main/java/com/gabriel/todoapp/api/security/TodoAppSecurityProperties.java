package com.gabriel.todoapp.api.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties("todoapp.auth")
public class TodoAppSecurityProperties {

    @NotBlank
    private String providerUrl;

    @NotBlank
    private String redirectUrisPermitidas;

    @NotNull
    private JksProperties jks;


    @Getter
    @Setter
    @Validated
    public static class JksProperties {
        @NotBlank
        private String keypass;

        @NotBlank
        private String storepass;

        @NotBlank
        private String alias;

        @NotBlank
        private String path;
    }
}
