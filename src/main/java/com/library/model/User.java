package com.library.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class User {
    private Long userId;
    private String login;
    private String password;

    public User() {
    }

    public User(Long userId, String login, String password) {
        this.userId = userId;
        this.login = login;
        this.password = password;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getUserId())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof User))
            return false;
        User user = (User) o;
        return new EqualsBuilder()
                .append(getUserId(), user.getUserId())
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("Login", getLogin())
                .toString();
    }
}