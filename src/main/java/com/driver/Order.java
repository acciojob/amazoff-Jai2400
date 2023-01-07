package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        this.id=id;
        Integer hour=Integer.valueOf(deliveryTime.substring(0,2));
        Integer minutes=Integer.valueOf(deliveryTime.substring(3));
        this.deliveryTime=hour*60 + minutes;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
