package com.harington.devops_training.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
//        try {
//            String username = loginData.get("username");
//            String password = loginData.get("password");
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            Map<String, Object> claims = new HashMap<>();
//            claims.put("roles", userDetails.getAuthorities());
//            String token = jwtUtil.generateToken(username, claims);
//            Map<String, String> response = new HashMap<>();
//            response.put("token", token);
//            return ResponseEntity.ok(response);
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(401).body("Invalid credentials");
//        }
//    }
//
//    // Ce contrôleur ne sert que l'API /login pour JWT, utile si tu veux garder l'authentification custom.
//    // Si tu passes à Keycloak/OIDC pur, tu pourras le supprimer.
//    // Pour l'instant, on le garde si tu veux tester l'API JWT.
}
