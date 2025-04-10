package com.karpen.lWhitelist.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "players")
@Getter
@Setter
public class User {

    @Id
    @Column(name = "playerName", length = 100)
    private String name;

    @Column(name = "access", nullable = false)
    private boolean access;

    @Column(name = "reason", length = 100)
    private String reason;

    @Column(name = "banned", nullable = false)
    private boolean baned;
}
