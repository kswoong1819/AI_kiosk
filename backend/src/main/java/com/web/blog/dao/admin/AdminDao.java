package com.web.blog.dao.admin;

import java.time.LocalDateTime;
import java.util.List;
import com.web.blog.model.visit.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AdminDao extends JpaRepository<Visit,Integer> {

    Integer countByCurrenttimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query(nativeQuery = true, value="select count(*) from visit v WHERE DATE_FORMAT(v.currenttime ,'%Y%m%d') = DATE_FORMAT(NOW(),'%Y%m%d')")
    Integer getCountvisitors();
    @Query(nativeQuery=true,value="SELECT DATE_FORMAT(currenttime,'%m'),count(*) FROM visit v GROUP BY DATE_FORMAT(currenttime,'%Y%m')")
    List<Visit> findVisitors();
    List<Visit> findAllByOrderByVidDesc();
    @Query(nativeQuery=true,value="SELECT DATE_FORMAT(currenttime,'%m'),count(*) FROM visit v GROUP BY DATE_FORMAT(currenttime,'%Y%m')")
    List<?> findMonthVisitors();

    
}
