package com.app.feegnna.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

public class SignupRequest {

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String prenom;

    @Getter
    @Setter
    private String nom;

    @Getter
    @Setter
    private String telephone;

    @Getter
    @Setter
    private String adresse;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    @Setter
    @Getter
    private Set<String> roles;

    public SignupRequest() {
        this.username = this.email;
    }
}
