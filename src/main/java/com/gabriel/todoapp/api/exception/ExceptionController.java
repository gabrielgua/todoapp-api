package com.gabriel.todoapp.api.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.gabriel.todoapp.domain.exception.EntidadeEmUsoException;
import com.gabriel.todoapp.domain.exception.EntidadeNaoEncontradaException;
import com.gabriel.todoapp.domain.exception.NegocioException;
import com.gabriel.todoapp.domain.exception.ValidacaoException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class ExceptionController extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;
    private final String ERRO_GENERICO = "Ocorreu um erro interno inesperado no sistema. Tente novamente mais tarde.";
    private final String ERRO_DADOS_INVALIDOS = "Um ou mais campos estão inválidos. Favor corrigir e tentar novamente.";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUncaught(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErroTipo tipo = ErroTipo.ERRO_DE_SISTEMA;
        String detail = ERRO_GENERICO;

        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(detail).build();
        log.error(ex.getMessage(), ex);

        return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {
        String detail = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErroTipo tipo = ErroTipo.NEGOCIO_ERRO;

        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(detail).build();

        return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
    }


    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {
        String detail = ex.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErroTipo tipo = ErroTipo.RECURSO_NAO_ENCONTRADO;

        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(detail).build();

        return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
    }


    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
        String detail = ex.getMessage();
        HttpStatus status = HttpStatus.CONFLICT;
        ErroTipo tipo = ErroTipo.ENTIDADE_EM_USO;

        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(detail).build();

        return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        String detail = ex.getMessage();
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErroTipo tipo = ErroTipo.ACESSO_NEGADO;

        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage("Você não possui acesso para realizar essa operação.")
                .build();

        return handleExceptionInternal(ex, erro, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    @ExceptionHandler(ValidacaoException.class)
    private ResponseEntity<?> handleValidacaoException(ValidacaoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), status, request);
    }

    private Erro.ErroBuilder createErroBuilder(HttpStatus status, ErroTipo tipo, String detail) {
        return Erro.builder()
                .status(status.value())
                .type(tipo.getUri())
                .title(tipo.getTitle())
                .detail(detail)
                .timestamp(OffsetDateTime.now());
    }

    private Erro.ErroBuilder createErroBuilder(HttpStatus status, String title, String detail) {
        return Erro.builder()
                .status(status.value())
                .title(title)
                .detail(detail)
                .timestamp(OffsetDateTime.now());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            body = createErroBuilder(status, status.getReasonPhrase(), ex.getMessage()).build();
        } else if (body instanceof String) {
            body = Erro.builder()
                    .timestamp(OffsetDateTime.now())
                    .status(status.value())
                    .title(status.getReasonPhrase())
                    .detail(ex.getMessage())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request);
        }

        String detail = "Corpo da requisição está inválido. Verifique o erro de sintaxe.";
        ErroTipo tipo = ErroTipo.CORPO_NAO_LEGIVEL;
        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(ERRO_GENERICO)
                .build();

        return handleExceptionInternal(ex, erro, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ErroTipo tipo = ErroTipo.CORPO_NAO_LEGIVEL;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', que é inválido. Informe um valor compatível com o tipo '%s'.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(ERRO_GENERICO).build();

        return handleExceptionInternal(ex, erro, headers, status, request);
    }


    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ErroTipo tipo = ErroTipo.CORPO_NAO_LEGIVEL;
        String className = ex.getReferringClass().getSimpleName();
        String detail = String.format("A propriedade '%s' não existe para '%s'. Corrija ou remova essa propriedade e tente novamente.", path, className);
        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(ERRO_GENERICO).build();

        return handleExceptionInternal(ex, erro, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String detail = ERRO_DADOS_INVALIDOS;
        ErroTipo tipo = ErroTipo.DADOS_INVALIDOS;
        List<Erro.Field> erroFields = fieldErrorsToList(bindingResult);

        Erro erro = createErroBuilder(status, tipo, detail)
                .userMessage(detail)
                .fields(erroFields)
                .build();

        return handleExceptionInternal(ex, erro, headers, status, request);
    }

    private List<Erro.Field> fieldErrorsToList(BindingResult bindingResult) {

        return bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    String name = objectError.getObjectName();
                    String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                    if (objectError instanceof FieldError) {
                        name = ((FieldError) objectError).getField();
                    }

                    return Erro.Field.builder()
                            .name(name)
                            .userMessage(message)
                            .build();

                })
                .collect(Collectors.toList());

    }

}
