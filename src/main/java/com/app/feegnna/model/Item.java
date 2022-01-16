package com.app.feegnna.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "items")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"}, allowGetters = true)
public class Item {

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
    @Getter
    @Column(columnDefinition = "TEXT", name = "description", nullable = false)
    private String description;

    @ManyToOne
    @Setter
    @Getter
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @Getter
    @Setter
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Getter
    @Setter
    @Column(name = "place", nullable = false)
    private String place;

    @Getter
    @Setter
    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "found_at", nullable = false)
    private Date foundAt;

    @Getter
    @Setter
    @Column(name = "claimed")
    private Boolean claimed = false;

    @Setter
    @Getter
    @Column(name = "claimed_by_name")
    private String claimedByName;

    @Setter
    @Getter
    @Column(name = "claimed_by_email")
    private String claimedByEmail;

    @Setter
    @Getter
    @Column(name = "claimed_by_phone")
    private String claimedByPhone;
}
