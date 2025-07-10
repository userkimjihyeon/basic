package com.beyond.basic.b1_hello.controller;

public class Hello {
    private String name;
    private String email;

    public Hello(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Hello{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
