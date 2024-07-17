package com.library.literatura.service;

public interface IDataConversor {
    <T> T getData(String json, Class<T> clase);
}