package com.gabriel.todoapp.api.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties("todoapp.auth")
public class TodoAppSecurityProperties {

    @NotBlank
    private String providerUrl;

    @NotNull
    private List<String> redirectUrisPermitidas;

    @NotNull
    private JksProperties jks;

    @NotNull
    private AngularUI angular;

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

    @Getter
    @Setter
    @Validated
    public static class AngularUI {
        @NotBlank
        private String clientId;

        @NotBlank
        private String clientSecret;
    }
}
