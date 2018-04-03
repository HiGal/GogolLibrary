package com.project.glib.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")

public class User {
    public static final String TYPE = "USER";
    public static final String LIBRARIAN = "LIBRARIAN";
    public static final String STUDENT = "STUDENT";
    public static final String INSTRUCTOR = "INSTRUCTOR";
    public static final String TA = "TEACHER_ASSISTANT";
    public static final String PROFESSOR = "PROFESSOR";
    public static final String PROFESSOR_VISITING = "VISITING_PROFESSOR";
    public static final String[] FACULTY = {INSTRUCTOR, TA, PROFESSOR};
    public static final String[] PATRONS = {STUDENT, INSTRUCTOR, TA, PROFESSOR, PROFESSOR_VISITING};
    public static final String[] ROLES = {LIBRARIAN, STUDENT, INSTRUCTOR, TA, PROFESSOR, PROFESSOR_VISITING};

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

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

    @Column(name = "role")
    private String role;

    @Column(name = "picture")
    private String picture;

    public User() {
    }

    public User(String login, String password, String name, String surname,
                String address, String phone, String role, boolean auth, String picture) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
//        this.loggedIn = isAuth;
        this.role = role;
        this.auth = auth;
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                auth == user.auth &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(address, user.address) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, login, password, name, surname, address, phone, auth, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname=" + surname +
                ", address='" + address + '\'' +
                ", phone=" + phone +
//                ", isAuth=" + loggedIn +
                ", role=" + role +
                ", auth =" + auth +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}