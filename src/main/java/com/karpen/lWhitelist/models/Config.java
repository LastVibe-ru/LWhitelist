package com.karpen.lWhitelist.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    private String banUrl;
    private String unbanUrl;
    private String apiKey;

    private boolean debug;
}
