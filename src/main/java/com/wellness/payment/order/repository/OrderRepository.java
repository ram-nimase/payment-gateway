package com.wellness.payment.order.repository;

import com.wellness.payment.order.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface OrderRepository extends MongoRepository<Order, String> {
}
