package com.app.feegnna.controllers;

import com.app.feegnna.model.User;
import com.app.feegnna.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/current")
    public ResponseEntity<User> currentUserDetails() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        return optionalUser.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        return optionalUser.map(user1 -> {
            user1.setUsername(user.getUsername());
            user1.setPrenom(user.getPrenom());
            user1.setNom(user.getNom());
            user1.setAdresse(user.getAdresse());
            user1.setTelephone(user.getTelephone());
            return ResponseEntity.ok().body(user1);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
