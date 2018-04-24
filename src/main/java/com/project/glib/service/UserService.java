package com.project.glib.service;

import com.project.glib.dao.implementations.UserDaoImplementation;
import com.project.glib.model.Booking;
import com.project.glib.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.project.glib.dao.implementations.UserDaoImplementation.REMOVE_USER_HAS_CHECKOUTS_EXCEPTION;
import static com.project.glib.model.User.LIBRARIANS;

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
    public static final String NAME_EXCEPTION = " name must exist ";
    public static final String SURNAME_EXCEPTION = " surname must exist ";
    public static final String PHONE_EXCEPTION = " phone number must exist ";
    public static final String PHONE_LENGTH_EXCEPTION = " phone length must equals " + PHONE_LENGTH;
    public static final String LOGIN_ALREADY_EXIST_EXCEPTION = " login already exist ";
    private final BookingService bookingService;
    private final CheckoutService checkoutService;
    private final UserDaoImplementation usersDao;
    private final MessageService messageService;
    private final LoggerService loggerService;

    public UserService(UserDaoImplementation usersDao,
                       BookingService bookingService,
                       CheckoutService checkoutService,
                       MessageService messageService,
                       LoggerService loggerService) {
        this.usersDao = usersDao;
        this.bookingService = bookingService;
        this.checkoutService = checkoutService;
        this.messageService = messageService;
        this.loggerService = loggerService;
    }

    public void add(User user) throws Exception {

        //TODO check this code ->

        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //userRepository.save(user);
        //user.setRole(roleRepository.findOne(user.getId()));
        checkValidParameters(user);
        if (usersDao.findByLogin(user.getLogin()) != null) throw new Exception(LOGIN_ALREADY_EXIST_EXCEPTION);
        try {
            //todo change to false
            user.setAuth(true);
            usersDao.add(user);
            if (Arrays.asList(LIBRARIANS).contains(user.getRole())) {
                loggerService.addLog(findByLogin(user.getLogin()).getId(),
                        0, LoggerService.ADDED_NEW_LIBRARIAN, System.currentTimeMillis(), User.TYPE, false);
            } else {
                loggerService.addLog(findByLogin(user.getLogin()).getId(),
                        0, LoggerService.ADDED_NEW_USER, System.currentTimeMillis(), User.TYPE, false);
            }
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }

    }

    public void update(User user) throws Exception {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        checkValidParameters(user);
        try {
            usersDao.update(user);
            if (Arrays.asList(LIBRARIANS).contains(user.getRole())) {
                loggerService.addLog(findByLogin(user.getLogin()).getId(),
                        0, LoggerService.MODIFIED_LIBRARAN, System.currentTimeMillis(), User.TYPE, false);
            } else {
                loggerService.addLog(findByLogin(user.getLogin()).getId(),
                        0, LoggerService.MODIFIED_USER, System.currentTimeMillis(), User.TYPE, false);
            }
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    public void remove(long userId) throws Exception {
        if (checkoutService.getCheckoutsByUser(userId).size() != 0) {
            throw new Exception(REMOVE_USER_HAS_CHECKOUTS_EXCEPTION);
        }

        messageService.removeAllByUserID(userId);
        try {
            boolean isLibrarian = Arrays.asList(LIBRARIANS).contains(getById(userId).getRole());
            removeAllBookingsByUserId(userId);
            if (isLibrarian) {
                loggerService.addLog(userId,
                        0, LoggerService.DELETED_LIBRARIAN, System.currentTimeMillis(), User.TYPE, false);
            } else {
                loggerService.addLog(userId,
                        0, LoggerService.DELETED_USER, System.currentTimeMillis(), User.TYPE, false);
            }
            usersDao.remove(userId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    /**
     * Validator for user model
     *
     * @param user user model
     * @throws Exception
     */
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

    public List<User> getListAuthUsersLib() throws Exception {
        try {
            return getList().stream()
                    .filter(user -> user.getAuth() && !Arrays.asList(LIBRARIANS).contains(user.getRole()))
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public List<User> getListNotAuthUsersLib() throws Exception {
        try {
            return getList().stream()
                    .filter(user -> !user.getAuth() && !Arrays.asList(LIBRARIANS).contains(user.getRole()))
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
            System.out.println(userId);
            String role = getById(userId).getRole();
            return role ;
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
