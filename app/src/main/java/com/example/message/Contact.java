package com.example.message;

public class Contact {
    private String name;
    private String number;

    public Contact(){

    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

}
