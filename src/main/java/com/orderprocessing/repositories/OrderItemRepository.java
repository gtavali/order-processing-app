package com.orderprocessing.repositories;

import com.orderprocessing.entities.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the order items.
 *
 * @author Gabor Tavali
 */
@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
