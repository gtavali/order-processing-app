package com.orderprocessing.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * Entity class for the order items.
 *
 * @author Gabor Tavali
 */
@Entity
@Data
@Table(name = "order_item_tbl")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    private Integer salePrice;
}
