package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.dao.interfaces.RoleRepository;
import com.project.glib.dao.interfaces.UserRepository;
import com.project.glib.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersDaoImplementation implements ModifyByLibrarian<User> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UsersDaoImplementation(UserRepository userRepository,
                                     RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean getIsAuthById(long userId) {
        return userRepository.findOne(userId).getLoggedIn();
    }

    public String getTypeById(long userId) {
        return userRepository.findOne(userId).getRole().toString();
    }

    public void add(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      //  userRepository.save(user);
       //user.setRole(roleRepository.findOne(user.getId()));
        userRepository.save(user);
        logger.info("User successfully saved. Document details : " + user);
    }

    public void update(User user) {
//        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(roleRepository.findOne(user.getId()));
        userRepository.save(user);
        logger.info("User successfully update. User details : " + user);
    }

    public void remove(long userId) {
        userRepository.delete(userId);
    }

    public User getById(long userId) {
        return userRepository.findOne(userId);
    }


    public User findLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    @SuppressWarnings("unchecked")
    public List<User> getList() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            logger.info("User list : " + user);
        }

        return users;
    }

    public List<User> authUsers(){
        return userRepository.findAll().stream().filter(User::getAuth).collect(Collectors.toList());
    }

}
