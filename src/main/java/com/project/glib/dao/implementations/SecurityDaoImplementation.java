//package com.project.glib.dao.implementations;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SecurityDaoImplementation {
//    private static final Logger logger = LoggerFactory.getLogger(SecurityDaoImplementation.class);
//    private final AuthenticationManager authenticationManager;
//    private final UserDetailsService userDetailsService;
//
//    @Autowired
//    public SecurityDaoImplementation(AuthenticationManager authenticationManager,
//                                         UserDetailsService userDetailsService) {
//        this.authenticationManager = authenticationManager;
//        this.userDetailsService = userDetailsService;
//    }
//
//    public String findLoggedInUsername() {
//        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
//        if (userDetails instanceof UserDetails) {
//            return ((UserDetails) userDetails).getUsername();
//        }
//
//        return null;
//    }
//
//    public void autoLogin(String username, String password) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//
//        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
//
//        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
//            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            logger.debug(String.format("Auto login %s successfully!", username));
//        }
//    }
//}
