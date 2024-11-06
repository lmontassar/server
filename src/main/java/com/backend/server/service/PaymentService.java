package com.backend.server.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.server.entity.Payment;
import com.backend.server.entity.PaymentInfoRequest;
import com.backend.server.entity.User;
import com.backend.server.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    
    public PaymentService( @Value("${stripe.key.secret}") String secretKey) {
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList <>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfoRequest.getAmount());
        params.put("currency", paymentInfoRequest.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
    }

    public ResponseEntity<String> stripePayment(User user,float amount) throws Exception {
        Payment p = new Payment();
        p.setAmount(amount);
        p.setUser(user);
        p.setPaymentTime(new Date());
        System.out.println(p);
        paymentRepository.save(p);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    public List<Payment> findByUser(User user){
        return paymentRepository.findByUser(user);
    }

}