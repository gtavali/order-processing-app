package com.orderprocessing.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Entity class for the orders.
 *
 * @author Gabor Tavali
 */
@Entity
@Data
@Table(name = "order_tbl")
public class Order {

    @Id
    private Long orderId;

    private String buyerName;

    private String buyerEmail;

    private Date orderDate;

    private double orderTotalValue;

    private String address;

    private Integer postcode;

    @OneToMany(targetEntity = OrderItem.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    public Order(Long orderId, String buyerName, String buyerEmail, Date orderDate, String address, Integer postcode, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.orderDate = orderDate;
        this.address = address;
        this.postcode = postcode;
        this.orderItems = orderItems;
        this.orderTotalValue = orderItems.stream().mapToDouble(item -> item.getTotalItemPrice()).sum();
    }

    public Order() { }
}
