package com.example.GuideServer.controller;

import com.example.GuideServer.Entity.User;
import com.example.GuideServer.repo.UserRepo;
import com.example.GuideServer.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private UserRepo userRepo;
    private JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, UserRepo userRepo, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, PasswordEncoder passwordEncoder1) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder1;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO requestDTO){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
            User user = userRepo.findByEmail(requestDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User does not exists"));
            String token = jwtTokenProvider.createToken(requestDTO.getEmail() , user.getRole().name());
            Map<Object , Object> response = new HashMap<>();
            response.put("email", requestDTO.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("gender", user.getGender());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e){
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/changePass")
    public ResponseEntity<?> changePass(@RequestBody ChangePassDTO user){
        String email = user.getEmail();
        String oldPass = user.getOldPass();
        String newPass = user.getNewPass();
        Map<Object , Object> response = new HashMap<>();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, oldPass));
            String encodedPass = passwordEncoder.encode(newPass);
            User user1 = userRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User does not exists"));
            userRepo.updatePassword(user1.getId(), encodedPass);
            response.put("status","Success");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e){
            response.put("status","bad credentials");
            return ResponseEntity.ok(response);
        }

    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PutMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody User user){
        Optional<User> existing = userRepo.findByEmail(user.getEmail());
        Map<Object , Object> response = new HashMap<>();
        if(!existing.isEmpty()) {
            response.put("status", "User with such email already exists");
            return ResponseEntity.ok(response);
        }else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            response.put("status", "User was successfully created");
            return ResponseEntity.ok(response);
        }
    }
}
