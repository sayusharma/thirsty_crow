package com.e.thirstycrow.Model;

public class Feedback {
    private String subject,phone,name,description,complaintid;
    public Feedback(){}
    public Feedback(String subject, String phone, String name, String description,String complaintid) {
        this.subject = subject;
        this.phone = phone;
        this.name = name;
        this.description = description;
        this.complaintid = complaintid;
    }

    public String getComplaintid() {
        return complaintid;
    }

    public void setComplaintid(String complaintid) {
        this.complaintid = complaintid;
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
