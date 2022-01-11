package com.app.feegnna.controllers;

import com.app.feegnna.enums.ERole;
import com.app.feegnna.model.Role;
import com.app.feegnna.model.User;
import com.app.feegnna.payload.request.LoginRequest;
import com.app.feegnna.payload.request.SignupRequest;
import com.app.feegnna.payload.response.JwtResponse;
import com.app.feegnna.payload.response.MessageResponse;
import com.app.feegnna.repository.RoleRepository;
import com.app.feegnna.repository.UserRepository;
import com.app.feegnna.security.jwt.JwtUtils;
import com.app.feegnna.security.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {

    final AuthenticationManager authenticationManager;

    final UserRepository userRepository;

    final RoleRepository roleRepository;

    final PasswordEncoder encoder;

    final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository permissionRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = permissionRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication); // AuthToken
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> authorities = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getPrenom(), userDetails.getNom(), userDetails.getEmail(), authorities));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(request.getEmail(), request.getPrenom(), request.getNom(),
                request.getTelephone());
        user.setEmail(request.getEmail());
        user.setAdresse(request.getAdresse());
        user.setPassword(encoder.encode(request.getPassword()));

        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();

        if (!strRoles.isEmpty()) {
            if (strRoles.contains("admin")) {
                Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.toString());
                if (adminRole.isPresent()) roles.add(adminRole.get());
                else return ResponseEntity.notFound().build();
            }
            if (strRoles.contains("mod")) {
                Optional<Role> modRole = roleRepository.findByName(ERole.ROLE_MODERATOR.toString());
                if (modRole.isPresent()) roles.add(modRole.get());
                else return ResponseEntity.notFound().build();
            }
        } else {
            Optional<Role> userRole = roleRepository.findByName(ERole.ROLE_USER.toString());
            if (userRole.isPresent()) roles.add(userRole.get());
            else return ResponseEntity.notFound().build();
        }

        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}