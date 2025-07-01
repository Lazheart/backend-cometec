package com.demo.DBPBackend.auth.domain;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RecoveryCodeStore {
    private static class CodeData {
        String code;
        LocalDateTime expiresAt;
        CodeData(String code, LocalDateTime expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }
    }

    private final Map<String, CodeData> codeMap = new ConcurrentHashMap<>();

    public void storeCode(String email, String code, int minutesValid) {
        codeMap.put(email, new CodeData(code, LocalDateTime.now().plusMinutes(minutesValid)));
    }

    public boolean isValid(String email, String code) {
        CodeData data = codeMap.get(email);
        return data != null && data.code.equals(code) && LocalDateTime.now().isBefore(data.expiresAt);
    }

    public void invalidate(String email) {
        codeMap.remove(email);
    }
} 