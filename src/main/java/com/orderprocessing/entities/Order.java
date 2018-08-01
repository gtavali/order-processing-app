package com.orderprocessing.entities;

import lombok.Data;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String buyerName;

    private String buyerEmail;

    private Date orderDate;

    private Integer orderTotalValue;

    private String address;

    private Integer postcode;

    @OneToMany(targetEntity = OrderItem.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;
}
