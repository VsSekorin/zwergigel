package com.example.zwergigel.service;

import com.example.zwergigel.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenService {

    private final Map<UUID, LastVerifying> verifiers;

    public TokenService() {
        this.verifiers = new ConcurrentHashMap<>();
    }

    public UUID add(final User user) {
        final UUID token = UUID.randomUUID();
        verifiers.put(token, new LastVerifying(user));
        return token;
    }

    public User get(final UUID token) {
        return verifiers.get(token).getUser();
    }

    public Optional<User> verify(final UUID token) {
        final LastVerifying lf = verifiers.get(token);
        if (lf != null) {
            lf.update();
            return Optional.of(lf.getUser());
        }
        return Optional.empty();
    }

    @Getter
    private static class LastVerifying {

        private final User user;
        private LocalDateTime time = LocalDateTime.now();

        private LastVerifying(User user) {
            this.user = user;
        }

        public void update() {
            this.time = LocalDateTime.now();
        }
    }
}
