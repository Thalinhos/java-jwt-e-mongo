@RestController
public class JwtController {
    @Autowired
    UserRepository userRepository;

    @Value("${JWT_SECRET}")
    String rsaPrivateKey;

    @GetMapping("/api/jwt")
    public ResponseEntity<?> jwt() {
        try {
            Algorithm algorithm = Algorithm.HMAC256(rsaPrivateKey);
            String token = JWT.create()
                .withIssuer("springTest")
                .withClaim("name", "test")
                .withClaim("role", "admin")
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
                .withIssuedAt(new Date())
                .sign(algorithm);
            return ResponseEntity.status(201).body(new JwtRequest(token));
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating JWT token");            
        }
    }

    @PostMapping("/api/jwt-verify")
    public ResponseEntity<?> jwtVerify(@RequestBody JwtRequest token) {
        if (token == null || token.getToken().isEmpty()) { return ResponseEntity.badRequest().body("Token is empty"); }
        try {
            Algorithm algorithm = Algorithm.HMAC256(rsaPrivateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("springTest")
                .build();
            DecodedJWT decodedJWT = verifier.verify(token.getToken());
            JwtResponse jwtResponse = new JwtResponse(decodedJWT.getClaim("name").asString(), decodedJWT.getClaim("role").asString());
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Token verification failed: " + e.getMessage()); 
        }
    }
}

<dependency>
			<groupId>com.auth0</groupId>
			<artifactId>java-jwt</artifactId>
			<version>4.5.0</version>
		</dependency>
