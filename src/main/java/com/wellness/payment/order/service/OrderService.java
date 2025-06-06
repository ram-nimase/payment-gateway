//package com.wellness.payment.order.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.wellness.payment.order.entity.Order;
//import com.wellness.payment.order.repository.OrderRepository;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Map;
//
//
//public class OrderService {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//
//    public String insertOrderStatus(String OrderResponse) throws JsonProcessingException {
//        Order order=convertJsonToObject(OrderResponse);
//        orderRepository.save(order);
//        return OrderResponse;
//    }
//
//    public Order convertJsonToObject(String OrderResponse) throws JsonProcessingException {
//        Order order=new Order();
//
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = mapper.readValue(OrderResponse, new TypeReference<Map<String, Object>>() {});
//
//        long timestamp = Integer.parseInt(map.get("created_at").toString());
//        //long timestamp=Integer.parseInt(json.getString("created_at"));
//        LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
//
//        // order.setAmount(Integer.parseInt(json.getString("amount")));
//         //int amount=Integer.parseInt(json.getString("amount"));
//         order.setAmount(Integer.parseInt(map.get("amount").toString()));
//         order.setOrderCreatedId(map.get("id").toString());
//         order.setAttempts(Integer.parseInt(map.get("attempts").toString()));
//         order.setReceipt(map.get("receipt").toString());
//         order.setStatus(map.get("status").toString());
//         order.setCurrency(map.get("currency").toString());
//         order.setAmount_due(Integer.parseInt(map.get("amount_due").toString()));
//         order.setCreatedAt(createdAt);
//         order.setUpdatedAt(LocalDateTime.now());
//
//         return order;
//    }
//}
