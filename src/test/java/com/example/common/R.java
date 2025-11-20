package com.example.common;

import lombok.Data;

/**
 * @author luminion
 */
@Data
public class R<T> {
    private T data;
    public R(T data) {
        this.data = data;
    }
    public static <T> R<T> of(T data) {
        return new R<>(data);
    }
}
