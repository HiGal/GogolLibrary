package com.project.glib.service;

import com.project.glib.model.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
