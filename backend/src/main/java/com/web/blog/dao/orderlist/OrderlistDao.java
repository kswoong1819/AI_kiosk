package com.web.blog.dao.orderlist;

import java.util.*;

import com.web.blog.model.orderlist.Orderlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderlistDao extends JpaRepository<Orderlist, String> {

	List<Orderlist> findOrderlistByUidAndSid(int uid, int sid);
	// List<Branch> findBranchBySid(int sid);

	Orderlist findTop1OrderlistByUidAndSidOrderByOrderdateDesc(int uid, int sid);

	// Orderlist[] findOrderlistByUidAndSidOrderByOrderdateDesc(int uid, int sid);
	ArrayList<Orderlist> findOrderlistByUidAndSidOrderByOrderdateDesc(int uid, int sid);
	ArrayList<Orderlist> findOrderlistByUidAndSidOrderByOrderdateAsc(int uid, int sid);
	ArrayList<Orderlist> findOrderlistByMenuid(int menuid);


	@Query(nativeQuery = true, value = "SELECT *,count(menuid) AS c FROM orderlist WHERE DATE_FORMAT(orderdate,'%h') GROUP BY menuid ORDER BY c desc")
	ArrayList<Orderlist> hotmenutimes();

	@Query(nativeQuery = true,value="SELECT DATE_FORMAT(o.orderdate,'%m'), SUM(b.price) FROM orderlist o join branch b on o.menuid = b.menuid GROUP BY DATE_FORMAT(o.orderdate,'%Y%m')")
	List<?> findMonthIncome();

	@Query(nativeQuery = true, value="SELECT o.oid, o.uid, b.name, b.price, DATE_FORMAT(o.orderdate ,'%Y-%m-%d %H:%i:%s') FROM orderlist o JOIN branch b ON o.menuid = b.menuid WHERE DATE_FORMAT(o.orderdate ,'%Y%m%d') = DATE_FORMAT(NOW(),'%Y%m%d')")
	List<?> findPurchaseHistory();	

	@Query(nativeQuery = true, value="SELECT b.name ,count(b.name) AS c FROM orderlist o JOIN branch b ON o.menuid = b.menuid GROUP BY b.name ORDER BY c desc LIMIT 5")
	List<?> findPopularMenu();	
}	
