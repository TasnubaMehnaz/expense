package com.example.expense.services;
import com.example.expense.Constants;
import com.example.expense.domain.Payment;
import com.example.expense.domain.Transaction;
import com.example.expense.repositories.PaymentRepository;
import com.example.expense.responseAPI.CheckStatusResponse;
import com.example.expense.responseAPI.CheckoutResponse;
import com.example.expense.responseAPI.PaymentResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    private static final String API= "http://sandbox.mynagad.com:10080/remote-payment-gateway-1.0/api/dfs/check-out/initialize/{merchantId}/{orderId}";
    private static final String checkoutAPI="http://sandbox.mynagad.com:10080/remote-payment-gateway-1.0/api/dfs/check-out/complete/{PaymentReferenceId}";
    private static final String statusAPI= "http://sandbox.mynagad.com:10080/remote-payment-gateway-1.0/api/dfs/verify/payment/{paymentRefId}";
    private final RestTemplate restTemplate;
    private final  ObjectMapper objectMapper;
    private final TransactionService transactionService;
    private final PaymentRepository paymentRepository;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;


    public PaymentServiceImpl(@Value("${public.key}") String pubKeyBase64, @Value("${private.key}") String privKeyBase64, ObjectMapper objectMapper, TransactionService transactionService,PaymentRepository paymentRepository) throws Exception {
        this.objectMapper = objectMapper;
        byte[] keyBytes = Base64.getDecoder().decode(pubKeyBase64);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey = keyFactory.generatePublic(spec);

        byte[] privKeyBytes = Base64.getDecoder().decode(privKeyBase64);
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
        this.privateKey = keyFactory.generatePrivate(privSpec);
        restTemplate = new RestTemplate();
        this.transactionService= transactionService;
        this.paymentRepository= paymentRepository;
    }

    private String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String sign(String data) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signedBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signedBytes);
    }

    @Override
    public String decrypt(String encryptedData)  {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean verifySignature(String data, String signature) {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(data.getBytes());
            return sig.verify(Base64.getDecoder().decode(signature));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    @Override
    public PaymentResponse makePayment(String merchantId,String dateTime,String orderId,String challenge) {
        try {
            // Create a JSON object
            Map<String, String> requestBody = new LinkedHashMap<>();
            requestBody.put("merchantId", merchantId);
            requestBody.put("orderId", orderId);
            requestBody.put("datetime", dateTime);
            requestBody.put("challenge", challenge);

            String plainSensitiveData = objectMapper.writeValueAsString(requestBody);
            //String plainStringSensitiveData = plainSensitiveData.toString();

            // Encrypt the JSON payload
            String sensitiveData = encrypt(plainSensitiveData);

            // Generate the signature
            String signature = sign(plainSensitiveData);

            // Prepare the final API endpoint
            String finalAPI = API.replace("{merchantId}", merchantId).replace("{orderId}", orderId);

            // Set headers
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/json");
            httpHeaders.set("X-KM-Api-Version", "v-0.2.0");
            httpHeaders.set("X-KM-IP-V4", "192.168.0.1");
            httpHeaders.set("X-KM-MC-Id", "683002007104225");
            httpHeaders.set("X-KM-Client-Type", "PC_WEB");

            // Create the final request body
            Map<String, String> finalRequestBody = new HashMap<>();
            finalRequestBody.put("sensitiveData", sensitiveData);
            finalRequestBody.put("signature", signature);
            finalRequestBody.put("dateTime", dateTime);

            // Create the request entity with encrypted data
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(finalRequestBody, httpHeaders);

            // Send the request
            HttpEntity<PaymentResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, requestEntity, PaymentResponse.class);
            PaymentResponse body = response.getBody();
            return body;

        } catch (Exception e) {
            // Handle exception
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CheckoutResponse checkout(String merchantId,String orderId,String plainReceivedSensitiveData, Integer transactionId){
        try{
            JsonNode jsonNode=objectMapper.readTree(plainReceivedSensitiveData);
            // Extract values
            String paymentReferenceId = jsonNode.get("paymentReferenceId").asText();
            String challenge = jsonNode.get("challenge").asText();
            String acceptDateTime = jsonNode.get("acceptDateTime").asText();

            String amount= transactionService.fetchAmountByTransactionId(transactionId);

            Map<String, String> body= new LinkedHashMap<>();
            body.put("merchantId",merchantId);
            body.put("orderId", orderId);
            body.put("currencyCode","050");
            body.put("amount",amount);
            body.put("challenge",challenge); //provided from initialize api response

            String plainSensitiveData = objectMapper.writeValueAsString(body);
            //String plainStringSensitiveData = plainSensitiveData.toString();

            // Encrypt the JSON payload
            String sensitiveData = encrypt(plainSensitiveData);

            // Generate the signature
            String signature = sign(plainSensitiveData);

            // Prepare the final API endpoint
            String finalCheckoutAPI = checkoutAPI.replace("{PaymentReferenceId}", paymentReferenceId);
            System.out.println(finalCheckoutAPI);

            // Set headers
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Content-Type", "application/json");
            httpHeaders.set("X-KM-Api-Version", "v-0.2.0");
            httpHeaders.set("X-KM-IP-V4", "192.168.0.1");
            httpHeaders.set("X-KM-MC-Id", "683002007104225");
            httpHeaders.set("X-KM-Client-Type", "PC_WEB");

            // Create the final request body
            Map<String, String> finalRequestBody = new HashMap<>();
            finalRequestBody.put("sensitiveData", sensitiveData);
            finalRequestBody.put("signature", signature);
            finalRequestBody.put("merchantCallbackURL","http://localhost:8080/api/users/status");//"http://sandbox.mynagad.com:10707/merchant-server/web/confirm" );

            // Create the request entity with encrypted data
            HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(finalRequestBody, httpHeaders);

            // Send the request
            HttpEntity<CheckoutResponse> response = restTemplate.exchange(finalCheckoutAPI, HttpMethod.POST, requestEntity, CheckoutResponse.class);
            CheckoutResponse responseBody = response.getBody();
            if (responseBody!= null){
               paymentRepository.addPaymentRecord(transactionId,merchantId,orderId,acceptDateTime,paymentReferenceId);
            }
            return responseBody;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public CheckStatusResponse checkStatus(String paymentReferenceId) {
        try{
            String checkStatusAPI = statusAPI.replace("{paymentRefId}", paymentReferenceId);
            // Send the request
            HttpEntity<CheckStatusResponse> response = restTemplate.exchange(checkStatusAPI, HttpMethod.GET, null, CheckStatusResponse.class);
            CheckStatusResponse responseBody = response.getBody();
            return responseBody;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }


}
