package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UsersDaoImplementation {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final UserRepository userRepository;

    @Autowired
    public UsersDaoImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean getIsAuthById(long userId) {
        return userRepository.findOne(userId).getAuth();
    }

    public String getTypeById(long userId) {
        return userRepository.findOne(userId).getRoles().toString();
    }

}
