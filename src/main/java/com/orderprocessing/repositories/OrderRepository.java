package com.orderprocessing.repositories;

import com.orderprocessing.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the orders.
 *
 * @author Gabor Tavali
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
}
