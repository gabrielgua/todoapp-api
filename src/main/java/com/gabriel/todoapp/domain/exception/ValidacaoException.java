package com.gabriel.todoapp.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;


@Getter
@RequiredArgsConstructor
public class ValidacaoException extends RuntimeException {
    private BindingResult bindingResult;
}
