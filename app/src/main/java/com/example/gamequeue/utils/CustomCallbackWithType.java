package com.example.gamequeue.utils;

public interface CustomCallbackWithType<T> {
    void onSuccess(T message);
    void onError(String error);
}
