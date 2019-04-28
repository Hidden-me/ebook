package org.ebook.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "order_info")
public class Order {
    private int orderId;
    private int buyerUid;
    private Timestamp timeCreate;
    private boolean done;

    public Order(){
        done = false;
        timeCreate = new Timestamp(System.currentTimeMillis());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Column(name = "buyer_uid")
    public int getBuyerUid() {
        return buyerUid;
    }

    public void setBuyerUid(int buyerUid) {
        this.buyerUid = buyerUid;
    }

    @Column(name = "time_create")
    public Timestamp getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Timestamp timeCreate) {
        this.timeCreate = timeCreate;
    }

    @Column(name = "done")
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
