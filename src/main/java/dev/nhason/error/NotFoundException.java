package dev.nhason.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
public class NotFoundException extends HotelsException{

    private final String resourceName;

    public NotFoundException(String resourceName) {
        super("%s was invalid".formatted(resourceName));
        this.resourceName = resourceName;
    }

    public NotFoundException(String resourceName,String message) {
        super(message);
        this.resourceName = resourceName;
    }

}
