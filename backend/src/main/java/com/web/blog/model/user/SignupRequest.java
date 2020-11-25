package com.web.blog.model.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
@ToString
@Data
public class SignupRequest {
    @ApiModelProperty(required = true)
    @NotNull
    int uid;
    @ApiModelProperty(required = true)
    @NotNull
    String email;
    // @ApiModelProperty(required = true)
    // @NotNull
    // @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
    // String password;
    @ApiModelProperty(required = true)
    @NotNull
    private String name;
    @ApiModelProperty(required = true)
    @NotNull
    private String gender;
    @ApiModelProperty(required = true)
    @NotNull
    private int age;
    @ApiModelProperty(required = true)
    @NotNull
    private String tel;
    @ApiModelProperty(required = true)
    @NotNull
    private String learningfile;

}
