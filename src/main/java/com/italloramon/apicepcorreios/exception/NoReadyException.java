package com.italloramon.apicepcorreios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Installation is running. Please wait for some seconds")
public class NoReadyException extends Exception {
}
