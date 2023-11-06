package model;

import java.sql.Date;

public class Customer {

    private int id;
    private String id_customer;
    private String fullname;
    private String username;
    private String email;
    private String gender;
    private Date birthday;
    private boolean status;




    public Customer(String id_customer , String fullname, String username, String email, String gender, Date birthday) {

        this.id_customer = id_customer;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;


    }
    public Customer( String id_customer, String fullname, String username, String email, String gender, Date birthday, boolean status) {
        this.id = id;
        this.id_customer = id_customer;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.status = status;
    }
    public Customer( int id,String id_customer, String fullname, String username, String email, String gender, Date birthday, boolean status) {
        this.id = id;
        this.id_customer = id_customer;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.birthday = birthday;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id_customer='" + id_customer + '\'' +
                ", fullname='" + fullname + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", status=" + status +
                '}';
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

