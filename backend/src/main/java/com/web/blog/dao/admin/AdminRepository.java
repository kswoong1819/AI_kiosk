package com.web.blog.dao.admin;

import com.web.blog.model.user.Useradmin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepository extends CrudRepository<Useradmin,String> {
    
     Useradmin findByAid(String id);
    
}
