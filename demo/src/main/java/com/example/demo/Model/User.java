package com.example.demo.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
public class User {
    
    @Id
    private String id;
    private String userName;

    public User() {
    }

    public User(String id, String userName) {
        super();
        this.id = id;
        this.userName = userName;
    }

    public User(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id.equals(user.id);
    }

}
