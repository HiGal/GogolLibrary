package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.dao.interfaces.UserRepository;
import com.project.glib.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDaoImplementation implements ModifyByLibrarian<User> {
    public static final String REMOVE_USER_HAS_CHECKOUTS_EXCEPTION = "User should return all documents before deleting";
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = User.TYPE;
    private static final String ADD_USER = TYPE + ADD;
    private static final String UPDATE_USER = TYPE + UPDATE;
    private static final String REMOVE_USER = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final UserRepository userRepository;

    @Autowired
    public UserDaoImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User user) {
        userRepository.saveAndFlush(user);
        logger.info(ADD_USER + user);
    }

    @Override
    public void update(User user) {
        userRepository.saveAndFlush(user);
        logger.info(UPDATE_USER + user);
    }

    @Override
    public void remove(long userId) {
        userRepository.delete(userId);
        logger.info(REMOVE_USER + userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getList() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            logger.info(LIST + user);
        }

        return users;
    }

    @Override
    public User getById(long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public long getId(User user) {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole().equals(user.getRole()) &&
                        u.getLogin().equals(user.getLogin()) &&
                        u.getPassword().equals(user.getPassword()) &&
                        u.getName().equals(user.getName()) &&
                        u.getSurname().equals(user.getSurname()) &&
                        u.getPhone().equals(user.getPhone()) &&
                        u.getAddress().equals(user.getAddress()))
                .findFirst().get().getId();
    }

    public User findByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

}
