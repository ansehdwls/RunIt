package com.ssafy.runit.exception.code;

import com.ssafy.runit.exception.ErrorCodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GroupErrorCode implements ErrorCodeType {

    GROUP_NOT_FOUND_ERROR(HttpStatus.BAD_REQUEST, "GROUP-001", "존재하지 않는 그룹입니다."),
    GROUP_NO_USERS_ERROR(HttpStatus.BAD_REQUEST, "GROUP-002", "그룹 내에 존재하지 않는 사용자입니다."),
    INVALID_RANK_TYPE_ERROR(HttpStatus.BAD_REQUEST,"GROUP-003","적절하지 않은 순위 조회입니다");


    private final HttpStatus status;
    private final String errorCode;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return this.status;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public String errorCode() {
        return this.errorCode;
    }
}
