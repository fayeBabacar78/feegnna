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
            if(!user.getPrenom().isEmpty()) {
                user1.setPrenom(user.getPrenom());
            }
            if(!user.getNom().isEmpty()) {
                user1.setNom(user.getNom());
            }
            if(!user.getTelephone().isEmpty()) {
                user1.setTelephone(user.getTelephone());
            }
            if(!user.getAdresse().isEmpty()) {
                user1.setAdresse(user.getAdresse());
            }

            userRepository.save(user1);
            return ResponseEntity.ok().body(user1);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
