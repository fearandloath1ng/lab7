package com.daniel.client.managers;

import com.daniel.common.person.User;

public class AuthManager {
    private static User user;
    private AuthManager() {
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        AuthManager.user = user;
    }
}
