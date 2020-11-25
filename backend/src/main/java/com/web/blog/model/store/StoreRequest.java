package com.web.blog.model.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;


@Data
@Valid
public class StoreRequest {
    @ApiModelProperty(required = true)
    private int sid;

    @ApiModelProperty(required = true)
    private String storename;
    


}
