package com.example.demo.Model;

public class JWTResponse {
    private String token;

    public JWTResponse(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
