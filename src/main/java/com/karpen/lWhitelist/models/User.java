package com.karpen.lWhitelist.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String name;
    private boolean access;
    private boolean baned;
}
