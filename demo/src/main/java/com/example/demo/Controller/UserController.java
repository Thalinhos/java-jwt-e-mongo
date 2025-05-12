package com.example.demo.Controller;


import java.time.Instant;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import com.example.demo.DB.DB;
import com.example.demo.Model.JWTResponse;
import com.example.demo.Model.LoginRequest;
import com.example.demo.Model.PessoaDto;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${jwt.secret}")
    private String base64Secret;

    @GetMapping("/")
    public RedirectView index() {
        return new RedirectView("index.html");
    }

    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView("login.html");
    }

 @PostMapping("/login")
    public ResponseEntity<JWTResponse> loginPost(@RequestBody LoginRequest body) {
        for (PessoaDto user : DB.users) {
            if (user.getUserName().equals(body.getUsername()) && user.getPassword().equals(body.getPassword())) {
                System.out.println("Login successful for user: " + user);
                
                var decodedKey = Base64.getDecoder().decode(base64Secret);

                String jws = Jwts.builder()
                        .setIssuer("Stormpath")
                        .setSubject(user.getUserName()) 
                        .claim("name", user.getUserName()) 
                        .claim("scope", "admins")
                        .setIssuedAt(new Date()) 
                        .setExpiration(Date.from(Instant.now().plusSeconds(80)))  
                        .signWith(SignatureAlgorithm.HS256, decodedKey)
                        .compact();
                System.out.println("Generated JWT: " + jws);
                return ResponseEntity.status(200).body(new JWTResponse(jws));
            }
        }

        System.out.println("Login failed for user: " + body.getUsername());
        // return ResponseEntity.status(401).body(null);    
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }

    @GetMapping("/verifyToken")
    public ResponseEntity<String> verifyToken(@RequestBody JWTResponse token) {
        System.out.println("Verifying token: " + token);
        try {
            var decodedKey = Base64.getDecoder().decode(base64Secret);
            var claims = Jwts.parser()
                    .setSigningKey(decodedKey)
                    .parseClaimsJws(token.getToken())
                    .getBody();
         

            String userName = claims.get("name", String.class);
            System.out.println("Token verified for user: " + userName);
            System.out.println("Token claims role: " + claims.get("scope", String.class));
            return ResponseEntity.ok("Token is valid for user: " + userName);

        } catch (Exception e) {
            System.out.println("Token verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid");
        }
    }

    @GetMapping("hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        System.out.println("Attempting to fetch all users...");
        List<User> users = userRepository.findAll();
        System.out.println("Fetched users: " + users);
        return users;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        System.out.println("Attempting to create user: " + user);
        User savedUser = userRepository.save(user);
        System.out.println("Created user: " + savedUser);
        
        return savedUser;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/userJson")
    public String createUserJson(@RequestBody String userJson) {
        Document document = Document.parse(userJson);
        var insertedUser = mongoTemplate.insert(document, "users");
        return insertedUser.toJson();
    }

    @GetMapping("/userJson/{id}")
    public ResponseEntity<User> getUserJsonById(@PathVariable String id) {
        Document user = mongoTemplate.findById(new ObjectId(id), Document.class, "users");
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var userDto = new User();
        userDto.setId(user.getObjectId("_id").toHexString());
        userDto.setUserName(user.getString("userName"));
        System.out.println(user);
        System.out.println(userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/pessoas-dto")
    public List<PessoaDto> getAllPessoasDto() {
        var users = DB.users;
        System.out.println(users);
        return users;
    }
}