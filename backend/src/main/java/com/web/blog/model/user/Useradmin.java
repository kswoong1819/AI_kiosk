package com.web.blog.model.user;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity
public class Useradmin {
    @Id
    private String aid;
    private String password;
}
