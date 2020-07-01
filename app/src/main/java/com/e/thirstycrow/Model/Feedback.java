package com.e.thirstycrow.Model;

public class Feedback {
    private String subject,phone,name,description;
    public Feedback(){}
    public Feedback(String subject, String phone, String name, String description) {
        this.subject = subject;
        this.phone = phone;
        this.name = name;
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
