package com.example.lendme.models;

public class Message {
    public String objectId;
    public String seller;
    public String buyer;
    public String lendItem;
    public String possibleBuyer;
    public String sendTime;
    public String senderName;


    public Message(String objectId, String seller, String buyer, String lendItem, String possibleBuyer,String sendTime,String senderName) {
        this.objectId = objectId;
        this.seller = seller;
        this.buyer = buyer;
        this.lendItem = lendItem;
        this.possibleBuyer = possibleBuyer;
        this.sendTime = sendTime;
        this.senderName = senderName;
    }



    public Message() {
    }

    public String getSendTime() {
        return sendTime;
    }
//    public void
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getLendItem() {
        return lendItem;
    }

    public void setLendItem(String lendItem) {
        this.lendItem = lendItem;
    }

    public String getPossibleBuyer() {
        return possibleBuyer;
    }

    public void setPossibleBuyer(String possibleBuyer) {
        this.possibleBuyer = possibleBuyer;
    }
}
