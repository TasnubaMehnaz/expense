package com.example.expense.controllers;

import com.example.expense.Constants;
import com.example.expense.domain.User;
import com.example.expense.responseAPI.CheckStatusResponse;
import com.example.expense.services.PaymentService;
import com.example.expense.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody Map<String, Object> userMap) {
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        User user = userService.validateUser(email,password);
        //Map<String, String> map = new HashMap<>();
        //map.put("message", "login successful");
        //return new ResponseEntity<>(map, HttpStatus.OK);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap){
        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");
        String role= (String) userMap.get("role");
        User user = userService.registerUser( firstName,lastName,email,password,role);
//        Map<String, String> map = new HashMap<>();
//        map.put("message", "registered successfully");
//        return new ResponseEntity<>(map, HttpStatus.OK);
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Map<String, String> generateJWTToken(User user) {
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp+ Constants.TOKEN_VALIDITY))
                .claim("userId" , user.getUserId())
                .claim("email" , user.getEmail())
                .claim("firstname" , user.getFirstName())
                .claim("lastname", user.getLastName())
                .claim("roles", user.getRoles()) // for roles
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    @Autowired
    PaymentService paymentService;
    @GetMapping("/status/")
    public ResponseEntity<Map<String, String>> status(@RequestParam String merchant ,@RequestParam String order_id,@RequestParam String payment_ref_id,@RequestParam String status,@RequestParam String status_code,@RequestParam String message) {

        CheckStatusResponse checkStatusResponse=paymentService.checkStatus(payment_ref_id);
        String merchantId= checkStatusResponse.getMerchantId();
        String orderId= checkStatusResponse.getOrderId();
        String paymentRefId= checkStatusResponse.getPaymentRefId();
        String amount = checkStatusResponse.getAmount();
        String clientMobileNo= checkStatusResponse.getClientMobileNo();
        String merchantMobileNo= checkStatusResponse.getMerchantMobileNo();
        String orderDateTime= checkStatusResponse.getOrderDateTime();
        String issuerPaymentDateTime= checkStatusResponse.getIssuerPaymentDateTime();
        String issuerPaymentRefNo= checkStatusResponse.getIssuerPaymentRefNo();
        String additionalMerchantInfo =checkStatusResponse.getAdditionalMerchantInfo();
        String statusInfo= checkStatusResponse.getStatus();
        String statusCode= checkStatusResponse.getStatusCode();
        String cancelIssuerDateTime= checkStatusResponse.getCancelIssuerDateTime();
        String cancelIssuerRefNo= checkStatusResponse.getCancelIssuerRefNo();
        String serviceType= checkStatusResponse.getServiceType();
        Map<String, String> map = new HashMap<>();
        map.put("merchantId",merchantId);
        map.put("orderId", orderId);
        map.put("paymentRefId", payment_ref_id);
        map.put("amount",amount);
        map.put("clientMobileNo",clientMobileNo);
        map.put("merchantMobileNo", merchantMobileNo);
        map.put("orderDateTime", orderDateTime);
        map.put("issuerPaymentDateTime", issuerPaymentDateTime);
        map.put("issuerPaymentRefNo", issuerPaymentRefNo);
        map.put("additionalMerchantInfo", additionalMerchantInfo);
        map.put("status",statusInfo);
        map.put("statusCode", statusCode);
        map.put("cancelIssuerDateTime", cancelIssuerDateTime);
        map.put("cancelIssuerRefNo", cancelIssuerRefNo);
        map.put("serviceType",serviceType);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }

}
