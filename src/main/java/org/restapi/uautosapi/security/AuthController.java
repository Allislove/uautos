package org.restapi.uautosapi.security;

import org.restapi.uautosapi.model.Customer;
import org.restapi.uautosapi.repository.CustomerRepository;
import org.restapi.uautosapi.security.dto.JwtRequest;
import org.restapi.uautosapi.security.dto.JwtResponse;
import org.restapi.uautosapi.security.dto.RegisterRequest;
import org.restapi.uautosapi.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:5173"
})
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    @Autowired
    private CustomerRepository customerRepository;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, AuthService authService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    @PostMapping("/login")
    public JwtResponse login(@RequestBody JwtRequest request) {
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Customer customer = customerRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String token = jwtUtil.generateToken(customer);
            return new JwtResponse(token, customer.getId(), customer.getName(), customer.getEmail());

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }




    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
