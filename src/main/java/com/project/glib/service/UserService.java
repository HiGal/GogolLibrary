package com.project.glib.service;

import com.project.glib.dao.implementations.UserDaoImplementation;
import com.project.glib.model.Booking;
import com.project.glib.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.project.glib.dao.implementations.UserDaoImplementation.REMOVE_USER_HAS_CHECKOUTS_EXCEPTION;

@Service
public class UserService implements ModifyByLibrarianService<User> {
    public static final int PHONE_LENGTH = 11;
    public static final String TYPE = User.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    public static final String ROLE_EXCEPTION = " invalid role ";
    public static final String ADDRESS_EXCEPTION = " address must exist ";
    public static final String LOGIN_EXCEPTION = " login must exist ";
    public static final String PASSWORD_EXCEPTION = " password must exist ";
    public static final String CONFIRM_PASSWORD_MUST_EXCEPTION = " confirm password must exist ";
    public static final String NAME_EXCEPTION = " name must exist ";
    public static final String SURNAME_EXCEPTION = " surname must exist ";
    public static final String PHONE_EXCEPTION = " phone number must exist ";
    public static final String PHONE_LENGTH_EXCEPTION = " phone length must equals " + PHONE_LENGTH;
    private final BookingService bookingService;
    private final CheckOutService checkoutService;
    private final UserDaoImplementation usersDao;

    public UserService(UserDaoImplementation usersDao,
                       BookingService bookingService,
                       CheckOutService checkoutService) {
        this.usersDao = usersDao;
        this.bookingService = bookingService;
        this.checkoutService = checkoutService;
    }

    public void add(User user) throws Exception {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //userRepository.save(user);
        //user.setRole(roleRepository.findOne(user.getId()));
        checkValidParameters(user);
        try {
            if (usersDao.findByLogin(user.getLogin()) != null) throw new Exception();
            user.setAuth(false);
            usersDao.add(user);
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }

    }

    public void update(User user) throws Exception {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        checkValidParameters(user);
        try {
            usersDao.update(user);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    public void remove(long userId) throws Exception {
        try {
            if (checkoutService.getCheckoutsByUser(userId).size() == 0) {
                removeAllBookingsByUserId(userId);
                usersDao.remove(userId);
            } else {
                throw new Exception(REMOVE_USER_HAS_CHECKOUTS_EXCEPTION);
            }
        } catch (Exception e) {
            if (e.getMessage().equals(REMOVE_USER_HAS_CHECKOUTS_EXCEPTION)) {
                throw e;
            } else {
                throw new Exception(REMOVE_EXCEPTION);
            }
        }
    }

    @Override
    public void checkValidParameters(User user) throws Exception {
        if (!isRole(user.getRole())) {
            throw new Exception(ROLE_EXCEPTION);
        }

        if (user.getAddress().equals("")) {
            throw new Exception(ADDRESS_EXCEPTION);
        }

        if (user.getLogin().equals("")) {
            throw new Exception(LOGIN_EXCEPTION);
        }

        if (user.getPassword().equals("")) {
            throw new Exception(PASSWORD_EXCEPTION);
        }

        if (user.getPasswordConfirm().equals("")) {
            throw new Exception(CONFIRM_PASSWORD_MUST_EXCEPTION);
        }

        if (user.getName().equals("")) {
            throw new Exception(NAME_EXCEPTION);
        }

        if (user.getSurname().equals("")) {
            throw new Exception(SURNAME_EXCEPTION);
        }

        if (user.getPhone().equals("")) {
            throw new Exception(PHONE_EXCEPTION);
        }

        if (user.getPhone().length() != PHONE_LENGTH) {
            throw new Exception(PHONE_LENGTH_EXCEPTION);
        }
    }

    private void removeAllBookingsByUserId(long userId) {
        try {
            List<Booking> listOfBookings = bookingService.getBookingsByUser(userId);
            for (Booking booking : listOfBookings) {
                bookingService.remove(booking.getId());
            }
        } catch (Exception ignore) {
        }
    }

    private boolean isRole(String role) {
        for (String r : User.ROLES) {
            if (r.equals(role)) return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getList() {
        try {
            return usersDao.getList();
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public User getById(long userId) {
        return usersDao.getById(userId);
    }

    @Override
    public long getId(User user) throws Exception {
        try {
            return usersDao.getId(user);
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public User findByLogin(String login) throws Exception {
        try {
            return usersDao.findByLogin(login);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public List<User> getListAuthUsers() {
        try {
            return getList().stream()
                    .filter(User::getAuth)
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public List<User> getListNotAuthUsers() throws Exception {
        try {
            return getList().stream()
                    .filter(User -> !User.getAuth())
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public boolean getIsAuthById(long userId) throws Exception {
        try {
            return getById(userId).getAuth();
        } catch (Exception e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getTypeById(long userId) throws Exception {
        try {
            return getById(userId).getRole();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public long getIdByLogin(String login) throws Exception {
        try {
            return findByLogin(login).getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
