package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.dao.interfaces.UserRepository;
import com.project.glib.model.Booking;
import com.project.glib.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UsersDaoImplementation implements ModifyByLibrarian<User> {
    public static final String REMOVE_USER_HAS_CHECKOUTS = "User should return all documents before deleting";
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final UserRepository userRepository;
    private final BookingDaoImplementation bookingDao;
    private final CheckoutDaoImplementation checkoutDao;

    @Autowired
    public UsersDaoImplementation(UserRepository userRepository,
                                  BookingDaoImplementation bookingDao,
                                  CheckoutDaoImplementation checkoutDao) {
        this.userRepository = userRepository;
        this.bookingDao = bookingDao;
        this.checkoutDao = checkoutDao;
    }

    @Override
    public void add(User user) throws Exception {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //userRepository.save(user);
        //user.setRole(roleRepository.findOne(user.getId()));
        checkValidParameters(user);
        try {
            if (findByLogin(user.getLogin()) != null) throw new Exception();
            user.setAuth(false);
            userRepository.save(user);
            logger.info("User successfully saved. Document details : " + user);
        } catch (Exception e) {
            logger.info("Try to add user with wrong parameters. New user information : " + user);
            System.out.println(e.getMessage());
            throw new Exception("Can't add this user, some parameters are wrong");
        }

    }

    @Override
    public void update(User user) throws Exception {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        checkValidParameters(user);
        try {
            userRepository.saveAndFlush(user);
            logger.info("User successfully update. User details : " + user);
        } catch (Exception e) {
            logger.info("Try to update this user, user don't exist or some new user parameters are wrong. " +
                    "Update user information : " + user);
            throw new Exception("Can't update this user, user don't exist or some new user parameters are wrong");
        }
    }

    @Override
    public void remove(long userId) throws Exception {
        try {
            logger.info("Try to delete user with user id = " + userId);
            // TODO reduce System.out.println()
            System.out.println(checkoutDao.getCheckoutsByUser(userId));
            if (checkoutDao.getCheckoutsByUser(userId).size() == 0) {
                userRepository.delete(userId);
                removeAllBookingsByUserId(userId);
            } else {
                throw new Exception(REMOVE_USER_HAS_CHECKOUTS);
            }
        } catch (Exception e) {
            if (!e.getMessage().equals(REMOVE_USER_HAS_CHECKOUTS)) {
                logger.info("Try to delete user with wrong user id = " + userId);
                throw new Exception("Delete this patron not available, patron doesn't exist");
            } else {
                throw e;
            }
        }
    }

    private void removeAllBookingsByUserId(long userId) throws Exception {
        try {
            List<Booking> listOfBookings = bookingDao.getBookingsByUser(userId);
            for (Booking booking : listOfBookings) {
                bookingDao.remove(booking.getId());
            }
        } catch (NoSuchElementException | NullPointerException ignore) {
        }
    }

    @Override
    public void checkValidParameters(User user) throws Exception {
        if (!user.getRole().equals(User.STUDENT) && !user.getRole().equals(User.TA) &&
                !user.getRole().equals(User.INSTRUCTOR) && !user.getRole().equals(User.PROFESSOR) &&
                !user.getRole().equals(User.LIBRARIAN)) {
            throw new Exception("Invalid role");
        }

        if (user.getAddress().equals("")) {
            throw new Exception("Address must exist");
        }

        if (user.getLogin().equals("")) {
            throw new Exception("Login must exist");
        }

        if (user.getPassword().equals("")) {
            throw new Exception("Password must exist");
        }

        if (user.getPasswordConfirm().equals("")) {
            throw new Exception("Confirm password must exist");
        }

        if (user.getName().equals("")) {
            throw new Exception("Name must exist");
        }

        if (user.getSurname().equals("")) {
            throw new Exception("Surname must exist");
        }

        if (user.getPhone().equals("")) {
            throw new Exception("Phone number must exist");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getList() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            logger.info("User list : " + user);
        }

        logger.info("User list successfully printed");
        return users;
    }

    @Override
    public User getById(long userId) {
        logger.info("Try to get user with user id = " + userId);
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
        } catch (NoSuchElementException e) {
            throw new Exception("User does not exist");
        }
    }

    public User findByLogin(String login) {
        try {
            logger.info("Try to get user with user login = \"" + login + "\"");
            return userRepository.findUserByLogin(login);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public List<User> getListAuthUsers() {
        logger.info("Authorized user list successfully printed");
        return userRepository.findAll().stream().filter(User -> User.getAuth()).collect(Collectors.toList());
    }

    public List<User> getListNotAuthUsers() {
        logger.info("Authorized user list successfully printed");
        return userRepository.findAll().stream().filter(User -> !User.getAuth()).collect(Collectors.toList());
    }

    public boolean getIsAuthById(long userId) throws Exception {
        try {
            logger.info("Try to get authorize user with user id = " + userId);
            return userRepository.findOne(userId).getAuth();
        } catch (Exception e) {
            logger.info("Try to get authorize user with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public String getTypeById(long userId) throws Exception {
        try {
            logger.info("Try to get user type with user id = " + userId);
            return userRepository.findOne(userId).getRole().toString();
        } catch (Exception e) {
            logger.info("Try to get user type with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public long getIdByLogin(String login) {
        User user = userRepository.findUserByLogin(login);
        return user.getId();
    }

}
