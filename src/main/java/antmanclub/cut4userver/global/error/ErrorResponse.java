package antmanclub.cut4userver.global.error;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class ErrorResponse {
    private final String message;
    private final String code;
    private final int httpStatus;
    private String detail;
    private final LocalDateTime timestamp;


    public ErrorResponse(ErrorCode ErrorCode, LocalDateTime localDateTime) {
        this.code = ErrorCode.getCode();
        this.message = ErrorCode.getMessage();
        this.httpStatus = ErrorCode.getStatus();
        this.timestamp = localDateTime;
    }

    public static ErrorResponse of(ErrorCode code, LocalDateTime localDateTime) {
        return new ErrorResponse(code, localDateTime);
    }

    public void setDetail(String detailMessage) {
        this.detail = detailMessage;
    }
}
