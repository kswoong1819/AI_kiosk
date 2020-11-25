package com.web.blog.model.orderlist;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.Valid;

@Valid
@Data
public class OrderlistRequest {
    @ApiModelProperty(required = true)
    int oid;

    @ApiModelProperty(required = true)
    int uid;

    @ApiModelProperty(required = true)
    int sid;

    @ApiModelProperty(required = true)
    int menuid;

    @ApiModelProperty(required = true)
    String orderlist;


}
