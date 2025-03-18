package qubiqule.cheqr.cheQR.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import qubiqule.cheqr.cheQR.utils.EncryptionUtil;

@RestController
@RequestMapping("/api")
@Slf4j
public class QrCodeController {

    public QrCodeController() {
    }

    @Value("${cheqr.app.encryption_secret_key}")
    private String secretKey;

    @PostMapping("/student/decodeQRCode")
    public ResponseEntity<Map<String, Object>> decodeQRCode(@RequestParam("qrCodeData") String encryptedData) {
        log.info("Call reached the controller method");
        try {
            // Decrypt the QR code data
            String decryptedData = EncryptionUtil.decrypt(encryptedData, secretKey);

            Map<String, Object> response = new HashMap<>();

            response.put("data", decryptedData);

            // Return the decrypted data
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.getMessage();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
