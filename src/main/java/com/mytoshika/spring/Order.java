package com.mytoshika.spring;

import javax.persistence.*;

@Entity
@Table(name = "order_table")
public class Order {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int order_id;
    //defining name as column name
    @Column
    private String item;
    //defining age as column name
    @Column
    private int amount;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Order() {
    }

    public Order(int order_id, String item, int amount) {
        this.order_id = order_id;
        this.item = item;
        this.amount = amount;
    }
}
