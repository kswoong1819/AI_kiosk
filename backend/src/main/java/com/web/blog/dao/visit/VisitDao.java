
package com.web.blog.dao.visit;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.blog.model.visit.Visit;

public interface VisitDao extends JpaRepository<Visit, Integer> {
    // save(Visit visit);
}
