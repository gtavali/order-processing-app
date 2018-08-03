package com.orderprocessing.entities;

import com.orderprocessing.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long orderItemId;

    private double salePrice;

    private double shippingPrice;

    private double totalItemPrice;

    private String sku;

    @Enumerated(EnumType.STRING)
    private Status status;

    public OrderItem(Long orderItemId, double salePrice, double shippingPrice, String sku, Status status) {
        this.orderItemId = orderItemId;
        this.salePrice = salePrice;
        this.shippingPrice = shippingPrice;
        this.sku = sku;
        this.status = status;
        this.totalItemPrice = salePrice + shippingPrice;
    }

    public OrderItem() { }
}
