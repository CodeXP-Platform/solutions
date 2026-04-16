package com.codexp.solutions.shared.domain.model.valueobjects;

public record NickName(String value) {

    public static NickName fromString(String nickName) {
        if (nickName == null || nickName.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be blank or empty.");
        }

        return new NickName(nickName.trim());
    }

    @Override
    public String toString() {
        return value;
    }
}
