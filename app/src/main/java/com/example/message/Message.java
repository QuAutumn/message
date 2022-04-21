package com.example.message;

public class Message {
    private String receive_mess;
    private String send_mess;

    public Message(){

    }
    public Message(String receive_mess, String send_mess) {
        this.receive_mess = receive_mess;
        this.send_mess = send_mess;
    }

    public String getReceive_mess() {
        return receive_mess;
    }

    public String getSend_mess() {
        return send_mess;
    }
}
