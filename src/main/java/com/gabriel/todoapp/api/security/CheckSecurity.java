package com.gabriel.todoapp.api.security;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface CheckSecurity {

    public @interface Usuarios {

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("@authorizationConfig.podeListarUsuarios()")
        public @interface CanListUsuarios {}

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("@authorizationConfig.podeConsultarUsuarios(#id)")
        public @interface CanSearchUsuario {}

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("hasRole('ROLE_ANONYMOUS') or hasAuthority('ADMIN')")
        public @interface CanWriteUsuarios {}

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("@authorizationConfig.podeGerenciarUsuarios(#id)")
        public @interface CanManageUsuarios {}

    }

    public @interface Tarefas {

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("@authorizationConfig.podeListarUsuarios()")
        public @interface CanListTarefas {}

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("@authorizationConfig.podeConsultarTarefa(#id)")
        public @interface CanSearchTarefa {}

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("@authorizationConfig.podeAdicionarTarefas()")
        public @interface CanWriteTarefas {}

        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        @PreAuthorize("@authorizationConfig.podeGerenciarTarefas(#id)")
        public @interface CanManageTarefa {}

    }


}
