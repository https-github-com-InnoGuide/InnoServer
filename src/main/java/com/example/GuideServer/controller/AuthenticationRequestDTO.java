package com.example.GuideServer.controller;

import lombok.Data;

@Data
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}


