package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/keys")
    public Map<String, String> getKeys() throws NoSuchAlgorithmException {

        // KeyGeneratorUtil se key pair
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();

        // pehle public key object, fir private key object
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // dono keys ko raw bytes mein
        byte[] publicRawBytes = publicKey.getEncoded();
        byte[] privateRawBytes = privateKey.getEncoded();

        // raw bytes ko Base64 string mein encode
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicRawBytes);
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateRawBytes);

        Map<String, String> keys = new LinkedHashMap<>();
        keys.put("publicKey", publicKeyBase64);
        keys.put("privateKey", privateKeyBase64);
        return keys;
    }
}
