package com.web.blog.dao.checkvisitor;

import java.util.*;

import com.web.blog.model.checkvisitor.Checkvisitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckvisitorDao extends JpaRepository<Checkvisitor, Integer> {
    List<Checkvisitor> findAll();

	void deleteByUid(String tid);
}
