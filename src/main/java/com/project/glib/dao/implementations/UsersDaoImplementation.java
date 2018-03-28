package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.dao.interfaces.UserRepository;
import com.project.glib.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UsersDaoImplementation implements ModifyByLibrarian<User> {
    public static final String REMOVE_USER_HAS_CHECKOUTS_EXCEPTION = "User should return all documents before deleting";
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = User.TYPE;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private static final String ADD_USER = TYPE + ADD;
    private static final String UPDATE_USER = TYPE + UPDATE;
    private static final String REMOVE_USER = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final UserRepository userRepository;

    @Autowired
    public UsersDaoImplementation(UserRepository userRepository) {
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
        try {
            List<User> users = userRepository.findAll();

            for (User user : users) {
                logger.info(LIST + user);
            }

            return users;
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public User getById(long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public long getId(User user) throws Exception {
        try {
            return userRepository.findAll().stream()
                    .filter(u -> u.getRole().equals(user.getRole()) &&
                            u.getLogin().equals(user.getLogin()) &&
                            u.getPassword().equals(user.getPassword()) &&
                            u.getPasswordConfirm().equals(user.getPasswordConfirm()) &&
                            u.getName().equals(user.getName()) &&
                            u.getSurname().equals(user.getSurname()) &&
                            u.getPhone().equals(user.getPhone()) &&
                            u.getAddress().equals(user.getAddress()))
                    .findFirst().get().getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public User findByLogin(String login) throws Exception {
        try {
            return userRepository.findUserByLogin(login);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public List<User> getListAuthUsers() {
        try {
            return userRepository.findAll().stream()
                    .filter(User::getAuth)
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public List<User> getListNotAuthUsers() throws Exception {
        try {
            return userRepository.findAll().stream()
                    .filter(User -> !User.getAuth())
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public boolean getIsAuthById(long userId) throws Exception {
        try {
            return userRepository.findOne(userId).getAuth();
        } catch (Exception e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getTypeById(long userId) throws Exception {
        try {
            return userRepository.findOne(userId).getRole();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public long getIdByLogin(String login) throws Exception {
        try {
            return userRepository.findUserByLogin(login).getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

}
