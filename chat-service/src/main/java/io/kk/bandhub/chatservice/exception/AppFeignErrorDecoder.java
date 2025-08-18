package io.kk.bandhub.chatservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Controller;

@Controller
public class AppFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        if (response.status() == 404) {
            throw new AccountException("Account was not found");
        }
        if (response.status() == 500) {
            return new Exception("Internal server error");
        }

        return new Exception("General Feign Exception");
    }
}
