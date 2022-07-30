package com.example.lendme.models;

public class Chat {
    public String cId;
    public String seller;
    public String items;
    public String possible_buyer;
    public String chatId;
    public String isRead;
    public String sender;
    public String senderName;
    public String message;

    public Chat(String cId, String seller, String items, String possible_buyer, String chatId, String isRead, String sender, String senderName, String message) {
        this.cId = cId;
        this.seller = seller;
        this.items = items;
        this.possible_buyer = possible_buyer;
        this.chatId = chatId;
        this.isRead = isRead;
        this.sender = sender;
        this.senderName = senderName;
        this.message = message;
    }
    public Chat(){}

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getPossible_buyer() {
        return possible_buyer;
    }

    public void setPossible_buyer(String possible_buyer) {
        this.possible_buyer = possible_buyer;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
