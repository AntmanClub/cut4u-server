package antmanclub.cut4userver.global.error.exception;

import antmanclub.cut4userver.global.error.ErrorCode;

public class EntityNotFoundException extends BusinessException{
    public EntityNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
