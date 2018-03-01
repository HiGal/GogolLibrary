package com.project.glib.model;

import javax.persistence.*;
import java.util.Set;

//@Entity
//public class User {
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(name = "login")
//    private String login;
//
//    @Column(name = "password")
//    private String password;
//
//    @Column(name = "confirm_password")
//    private String confirmPassword;
//
//    @Column(name = "name")
//    private String userName;
//
//    @Column(name = "surname")
//    private int userSurname;
//
//    @Column(name = "address")
//    private String address;
//
//    @Column(name = "phone")
//    private int phone;
//
//    @Column(name = "type")
//    private String type;
//
//    protected User() {
//    }
//
//    public User(String login, String password, String userName, int userSurname, String address, int phone, String type) {
//        this.login = login;
//        this.password = password;
//        this.userName = userName;
//        this.userSurname = userSurname;
//        this.address = address;
//        this.phone = phone;
//        this.type = type;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", login='" + login + '\'' +
//                ", password='" + password + '\'' +
//                ", userName='" + userName + '\'' +
//                ", userSurname=" + userSurname +
//                ", address='" + address + '\'' +
//                ", phone=" + phone +
//                ", type='" + type + '\'' +
//                '}';
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getLogin() {
//        return login;
//    }
//
//    public void setLogin(String login) {
//        this.login = login;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public int getUserSurname() {
//        return userSurname;
//    }
//
//    public void setUserSurname(int userSurname) {
//        this.userSurname = userSurname;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public void setAddress(String address) {
//        this.address = address;
//    }
//
//    public int getPhone() {
//        return phone;
//    }
//
//    public void setPhone(int phone) {
//        this.phone = phone;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//}

@Entity
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "confirm_password")
    private String passwordConfirm;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private int surname;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private int phone;


    private Set<Role> roles;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public int getSurname() {
        return surname;
    }

    public void setSurname(int surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}