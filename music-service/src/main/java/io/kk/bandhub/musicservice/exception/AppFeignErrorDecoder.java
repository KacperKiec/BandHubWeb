package io.kk.bandhub.musicservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import io.kk.bandhub.musicservice.exception.musician.MusicianError;
import io.kk.bandhub.musicservice.exception.musician.MusicianException;
import org.springframework.stereotype.Controller;

@Controller
public class AppFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        if (response.status() == 404) {
            throw new MusicianException(MusicianError.ACCOUNT_DOESNT_EXISTS);
        }
        if (response.status() == 500) {
            return new Exception("Internal server error");
        }

        return new Exception("General Feign Exception");
    }
}
