package com.demo.DBPBackend.events.register;

import com.demo.DBPBackend.email.domain.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RegisterListening {

    private final EmailService emailService;

    public RegisterListening(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    @Async
    public void handleHelloEmailEvent(RegisterEvent event) throws MessagingException {
        emailService.correoSingIn(event.getEmail(), event.getUser());
    }
}
