package com.example.GuideServer.controller;

import lombok.Data;

@Data
public class ChangePassDTO {
    private String email;
    private String oldPass;
    private String newPass;
}
