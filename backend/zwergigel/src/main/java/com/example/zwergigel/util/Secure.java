package com.example.zwergigel.util;

import lombok.SneakyThrows;

import java.security.MessageDigest;

public final class Secure {

    private Secure() {
    }

    @SneakyThrows
    public static String encode(final String password) {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        return new String(md.digest());
    }
}
