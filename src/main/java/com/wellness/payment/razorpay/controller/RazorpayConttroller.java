package com.wellness.payment.razorpay.controller;

import com.wellness.payment.razorpay.service.RazorpayService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/razorpay")
public class RazorpayConttroller {

    @Autowired
    private RazorpayService razorpayService;


    @GetMapping("/check")
    public ResponseEntity<String> checkInstance(HttpServletResponse response) {
        response.addHeader("X-Container-Id", System.getenv("HOSTNAME"));
        return ResponseEntity.status(HttpStatus.OK).body(response.getHeader("X-Container-Id"));
    }


    @GetMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam int amount){
        try{
            String orderResponse=razorpayService.createOrder(amount);
            if(orderResponse !=null){
                return ResponseEntity.ok(orderResponse);
            }else{
                return ResponseEntity.badRequest().body("Failed to create Razorpay order.");
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred while creating the order.");
        }
    }

    @PutMapping("/verify-payment")
    public ResponseEntity<String> verifyPayment(@RequestBody Map<String,String> payload){
        return razorpayService.verifyPaymemt(payload);
    }
}
