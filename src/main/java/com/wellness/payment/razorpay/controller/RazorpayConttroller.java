package com.wellness.payment.razorpay.controller;

import com.wellness.payment.razorpay.service.RazorpayService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(RazorpayConttroller.class);

    @Autowired
    private RazorpayService razorpayService;

    @PostConstruct
    public void init() {
        System.out.println("Running in container: " + System.getenv("HOSTNAME"));
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkInstance(HttpServletResponse response) {
        response.addHeader("X-Container-Id", System.getenv("HOSTNAME"));
        return ResponseEntity.status(HttpStatus.OK).body(response.getHeader("X-Container-Id"));
    }


    @GetMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam int amount,HttpServletResponse response){
        logger.info("Pay Amount::"+amount);
        logger.info("Host Name::"+System.getenv("HOSTNAME"));
        String hostname=System.getenv("HOSTNAME");
        try{
            String orderResponse=razorpayService.createOrder(amount);
            orderResponse="HostName::" +hostname+" "+"orderResponse ::"+orderResponse;
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
