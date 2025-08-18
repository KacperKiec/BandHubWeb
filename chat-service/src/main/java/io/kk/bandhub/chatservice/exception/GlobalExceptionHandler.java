package io.kk.bandhub.chatservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatException.class)
    ResponseEntity<ErrorResponse> handleChatException(ChatException e) {
        if (e.getStatus().equals(ChatError.CHAT_NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404", e.getMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("400", e.getMessage()));
        }
    }

    @ExceptionHandler(MessageException.class)
    ResponseEntity<ErrorResponse> handleMessageException(MessageException e) {
        if (e.getStatus().equals(MessageError.MESSAGE_NOT_FOUND)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("404", e.getMessage()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("400", e.getMessage()));
        }
    }

    @ExceptionHandler(AccountException.class)
    ResponseEntity<ErrorResponse> handleAccountException(AccountException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("400", e.getMessage()));
    }
}
