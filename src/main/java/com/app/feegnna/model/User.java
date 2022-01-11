package com.app.feegnna.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class User {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    @Getter
    @Column(name = "username", nullable = false)
    private String username;

    @Setter
    @Getter
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Setter
    @Getter
    @Column(name = "nom", nullable = false)
    private String nom;

    @Setter
    @Getter
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Setter
    @Getter
    @Column(name = "adresse")
    private String adresse;

    @Setter
    @Getter
    @Column(name = "email", nullable = false)
    private String email;

    @JsonIgnore
    @Setter
    @Getter
    @Column(name = "password", nullable = false)
    private String password;

    @Setter
    @OneToMany(mappedBy = "user") // One user can declare many articles
    private Set<Item> items;

    @Setter
    @Getter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;


    @Getter
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Getter
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;

    public User(String username, String prenom, String nom, String telephone) {
        this.username = username;
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
    }

    public User() {
    }
}
