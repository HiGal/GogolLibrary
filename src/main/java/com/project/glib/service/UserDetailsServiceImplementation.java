//package com.project.glib.service;
//
//import com.project.glib.dao.implementations.UsersDaoImplementation;
//import com.project.glib.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//public class UserDetailsServiceImplementation implements UserDetailsService {
//    private final UsersDaoImplementation usersDao;
//
//    @Autowired
//    public UserDetailsServiceImplementation( UsersDaoImplementation usersDao) {
//        this.usersDao = usersDao;
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println(" This is username " + username);
//        User user = usersDao.findLogin(username);
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
//
//        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
//    }
//}
