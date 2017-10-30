package com.library.model;

import org.springframework.test.util.ReflectionTestUtils;

public class UserBuilder {
    private User model;

    public UserBuilder() {
        model = new User();
    }

    public UserBuilder userId(Long userId) {
        ReflectionTestUtils.setField(model, "userId", userId);
        return this;
    }

    public UserBuilder login(String login) {
        ReflectionTestUtils.setField(model, "login", login);
        return this;
    }

    public UserBuilder password(String password) {
        ReflectionTestUtils.setField(model, "password", password);
        return this;
    }

    public User build() {
        return model;
    }
}
