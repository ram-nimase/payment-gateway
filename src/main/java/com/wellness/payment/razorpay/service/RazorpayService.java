package com.wellness.payment.razorpay.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.wellness.payment.order.service.OrderService;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

@Service
public class RazorpayService {


    private RazorpayClient razorpayClient;

    @Value("${payment.gateway.secretKey}")
    private  String keySecret;
   // private final String publicKey;

    @Autowired
    private OrderService orderService;


//    public RazorpayService(
//            @Value("${payment.gateway.secretKey}") String secretKey,
//            @Value("${payment.gateway.publishableKey}") String publicKey) {
//        this.secretKey = secretKey;
//        this.publicKey = publicKey;
//    }
//
//    @PostConstruct
//    void init() {
//        try {
//            this.razorpayClient = new RazorpayClient(publicKey, secretKey);
//           // log.info("RazorpayClient initialised successfully");
//        } catch (RazorpayException ex) {
//            // re-throw as unchecked so Spring stops and the root cause is obvious
//            throw new IllegalStateException("Failed to create RazorpayClient – check API keys", ex);
//        }
//    }

    public  RazorpayService(
            @Value("${payment.gateway.secretKey}") String secretKey,
            @Value("${payment.gateway.publishableKey}") String publicKey
    ){
        try{
            razorpayClient=new RazorpayClient(secretKey,publicKey);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String createOrder(int amount){
        String orderResponse="";
        JSONObject options=new JSONObject();
        options.put("amount",amount);
        options.put("currency","INR");
        options.put("receipt","order_928457");
        options.put("payment_capture",1);

        try{
         orderResponse =orderService.insertOrderStatus(razorpayClient.orders.create(options).toString());

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return  orderResponse;
    }

    public ResponseEntity<String> verifyPaymemt(Map<String, String> payload) {
        String orderId=payload.get("razorpay_order_id");
        String paymentId=payload.get("razorpay_payment_id");
        String signature=payload.get("razorpay_signature");

        String generatedSignature="";

        try{
            String data=orderId+ "|" + paymentId;
            Mac mac=Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
            mac.init(secretKey);
            byte[] hmac=mac.doFinal(data.getBytes());
            generatedSignature= Base64.getEncoder().encodeToString(hmac);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Verification error");
        }

        if (generatedSignature.equals(signature)) {
            return ResponseEntity.ok("Payment verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid payment signature");
        }
    }
}
