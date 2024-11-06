package com.backend.server.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.config.JWT;
import com.backend.server.entity.Payment;
import com.backend.server.entity.PaymentInfoRequest;
import com.backend.server.entity.User;
import com.backend.server.service.PaymentService;
import com.backend.server.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/payment/secure")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userser;

    @Autowired 
    private JWT jwt;

    @PostMapping("/payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest)
            throws StripeException {

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
        String paymentStr = paymentIntent.toJson();

        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }

    @PutMapping("/payment-complete")
    public ResponseEntity<?> stripePaymentComplete(@RequestHeader(value="Authorization") String token, @RequestBody PaymentInfoRequest paymentInfoRequest)
            throws Exception {
        String userId = jwt.payloadJWTExtraction(token, "sub");
        System.out.println("User ID: " + userId);
        if (userId == null) {
            throw new Exception("User email is missing");
        }
        Long id = Long.valueOf(userId);
        User u = userser.findById(id);
        float amount = paymentInfoRequest.getAmount() / 100;
        u.setAmount(u.getAmount()+amount);
        userser.save(u);
        return paymentService.stripePayment(u,amount);
    }
    @GetMapping("/get/user/{ID}")
    public ResponseEntity<?> getPaymentByUserID(@PathVariable Long ID )
    {
        try{
            User u = userser.findById(ID);
            List<Payment> payments = paymentService.findByUser(u);
            return ResponseEntity.ok(payments);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }


}
