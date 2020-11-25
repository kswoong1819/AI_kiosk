package com.web.blog.model.orderlist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.blog.model.branch.Branch;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Orderlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int oid;
    private int uid;
    private int sid;
    private int menuid;

    @ManyToOne
    @JoinColumn(insertable = false, updatable = false, name = "menuid")
    Branch branch;

    @Column(insertable = false, updatable = false, name = "orderdate")
    private String orderdate;
}