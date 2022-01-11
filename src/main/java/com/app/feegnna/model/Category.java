package com.app.feegnna.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter
    @Getter
    @Column(name = "label", nullable = false)
    private String label;

    @Setter
    @OneToMany(mappedBy = "category")
    private Set<Item> items;
}