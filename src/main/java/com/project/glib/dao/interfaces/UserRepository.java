package com.project.glib.dao.interfaces;

import com.project.glib.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);

}
