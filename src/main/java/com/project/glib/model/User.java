package com.project.glib.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    public static final String FACULTY = "FACULTY";
    public static final String STUDENT = "STUDENT";
    public static final String LIBRARIAN = "LIBRARIAN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "confirm_password")
    private String passwordConfirm;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

//    @Column(name = "loggedIn")
//    private boolean loggedIn;

    @Column(name = "auth")
    private boolean auth;

    @Column(name = "role" )
    private String role;

    public User() {
    }

    public User(String login, String password, String passwordConfirm, String name, String surname, String address, String phone, boolean isAuth, String role, boolean auth) {
        this.login = login;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
//        this.loggedIn = isAuth;
        this.role = role;
        this.auth = auth;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                ", name='" + name + '\'' +
                ", surname=" + surname +
                ", address='" + address + '\'' +
                ", phone=" + phone +
//                ", isAuth=" + loggedIn +
                ", role=" + role +
                ", auth =" + auth +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public boolean getLoggedIn() {
//        return loggedIn;
//    }
//
//    public void setLoggedIn(boolean auth) {
//        this.loggedIn = auth;
//    }

    public boolean getAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

//    @ManyToMany
//    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
//    public Set<Role> getRoles() {
//        return roles;
//    }

    public String getRole() {
        return role;
    }

//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }

    public void setRole(String role) {
        this.role = role;
    }
}