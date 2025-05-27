package com.demo.DBPBackend.events.register;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RegisterEvent extends ApplicationEvent {
    private final String email;
    private final String user;

    public RegisterEvent(String email, String user) {
        super(email);
        this.email = email;
        this.user = user;
    }
}
