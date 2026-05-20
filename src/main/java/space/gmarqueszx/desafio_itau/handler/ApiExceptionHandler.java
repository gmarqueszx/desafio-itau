package space.gmarqueszx.desafio_itau.handler;

import jakarta.annotation.Nullable;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import space.gmarqueszx.desafio_itau.exception.InvalidTransactionException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected @Nullable ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                             HttpHeaders headers,
                                                                             HttpStatusCode status,
                                                                             WebRequest request) {
        ProblemType problemType = ProblemType.BAD_REQUEST;
        String detail = String.format("The resource '%s' that you tried to access does not exist.",
                ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail, null).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                                   HttpHeaders headers,
                                                                                   HttpStatusCode status,
                                                                                   WebRequest request) {
        ProblemType problemType = ProblemType.BAD_REQUEST;
        String detail = "HTTP method not accepted at this endpoint.";

        Problem problem = createProblemBuilder(status, problemType, detail, null).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        ProblemType problemType = ProblemType.BAD_REQUEST;
        String detail = "URL with a non-expected type.";

        Problem problem = createProblemBuilder(status, problemType, detail, null).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    @Override
    public @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        ProblemType problemType = ProblemType.BAD_REQUEST;
        String detail = "The request body is invalid or incorrectly formatted.";


        Problem problem = createProblemBuilder(status, problemType, detail, null).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    public @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        ProblemType problemType = ProblemType.BAD_REQUEST;
        String detail = "The argument validation failed.";

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        Problem problem = createProblemBuilder(status, problemType, detail, errors).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<?> handleInvalidTransactionException(InvalidTransactionException ex,
                                                                 WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ProblemType problemType = ProblemType.INVALID_TRANSACTION_EXCEPTION;
        String detail = ex.getMessage();

        Problem problem = createProblemBuilder(status, problemType, detail, null).build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }



    @Override
    protected @Nullable ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                                       @Nullable Object body,
                                                                       HttpHeaders headers,
                                                                       HttpStatusCode status,
                                                                       WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .title(HttpStatus.valueOf(status.value()).getReasonPhrase())
                    .status(status.value())
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatusCode status,
                                                        ProblemType problemType, String detail,
                                                        Map<String, String> errors) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail)
                .errors(errors);
    }
}
