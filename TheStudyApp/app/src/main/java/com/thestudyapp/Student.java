package com.thestudyapp;

import java.util.List;

/**
 * Created by Soneer Sainion on 3/26/2017.
 */

public class Student {
    private String uid;
    private String name;
    private String email;
    private List<String> courses;

    public Student(String userUID, String userName, String userEmail, List<String> userCourses)
    {
        uid = userUID;
        name = userName;
        email = userEmail;
        courses = userCourses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
}
