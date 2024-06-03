package com.example.expense.controllers;
import com.example.expense.responseAPI.CheckoutResponse;
import com.example.expense.responseAPI.PaymentResponse;
import com.example.expense.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/categories/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/{transactionId}")
    public ResponseEntity<Map<String, String>> payment(@RequestBody Map<String, Object> userMap,@PathVariable("transactionId") Integer transactionId){
        String merchantId = (String) userMap.get("merchantId");
        String dateTime = (String) userMap.get("datetime");
        String orderId = (String) userMap.get("orderId");
        String challenge = (String) userMap.get("challenge");
        PaymentResponse paymentResponse = paymentService.makePayment( merchantId, dateTime, orderId, challenge);
        String receivedSensitiveData=paymentResponse.getSensitiveData();
        String receivedSignature=paymentResponse.getSignature();
        Map<String, String> map = new HashMap<>();
        try {
            String plainReceivedSensitiveData = paymentService.decrypt(receivedSensitiveData);

            boolean isSignatureValid = paymentService.verifySignature(plainReceivedSensitiveData, receivedSignature);
            if (!isSignatureValid) {
                map.put("message", "Invalid signature");
                return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
            }

            CheckoutResponse checkoutResponse=paymentService.checkout(merchantId,orderId,plainReceivedSensitiveData,transactionId);
            String responseURL= checkoutResponse.getCallBackURL();
            String responseStatus= checkoutResponse.getStatus();
            map.put("response URL", responseURL);
            map.put("Response Status", responseStatus);
            map.put("message", "Checkout successful");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            map.put("message", "Error processing payment response");
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
