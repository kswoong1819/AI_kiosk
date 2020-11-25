package com.web.blog.model.checkvisitor;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Valid
@Data
public class CheckvisitorRequest {
    @ApiModelProperty(required = true)
    private Integer cvidxid;

    @ApiModelProperty(required = true)
    private Integer uid;

}
