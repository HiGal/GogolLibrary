package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.dao.interfaces.UserRepository;
import com.project.glib.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UsersDaoImplementation implements ModifyByLibrarian<User> {
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
        return userRepository.findOne(userId).getRole().toString();
    }

    public void add(User user) {
        userRepository.save(user);
        logger.info("User successfully saved. Document details : " + user);
    }

    public void update(User user) {
        userRepository.save(user);
        logger.info("User successfully update. User details : " + user);
    }

    public void remove(long userId) {
        userRepository.delete(userId);
    }

    public User getById(long userId) {
        return userRepository.findOne(userId);
    }

    @SuppressWarnings("unchecked")
    public List<User> getList() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            logger.info("User list : " + user);
        }

        return users;
    }

}
