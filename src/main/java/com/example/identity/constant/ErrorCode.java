package com.example.identity.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Uncategorized error"),
    INVALID_KEY(400, "Invalid key provided"),
    USER_EXISTED(409, "User already exists"),
    USERNAME_INVALID(400, "Username must be at least 3 characters"),
    PASSWORD_INVALID(400, "Password must be at least 8 characters"),
    USER_NOT_EXISTED(400, "User not exists"),
    UNAUTHENTICATED(401, "Unauthenticated");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

}
