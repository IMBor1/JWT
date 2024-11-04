package com.test.JWT.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum Role {
    USER,
    MODERATOR,
    SUPER_ADMIN;
}
