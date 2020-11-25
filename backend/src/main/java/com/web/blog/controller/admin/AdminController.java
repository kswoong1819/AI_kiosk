package com.web.blog.controller.admin;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.blog.dao.admin.AdminDao;
import com.web.blog.dao.admin.AdminRepository;
import com.web.blog.dao.branch.BranchDao;
import com.web.blog.dao.orderlist.OrderlistDao;
import com.web.blog.jwt.JwtService;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.branch.Branch;
import com.web.blog.model.user.Useradmin;
import com.web.blog.model.visit.Visit;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
public class AdminController {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private OrderlistDao orderlistDao;

    @Autowired
    private AdminRepository AdminRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/admin/getvisitorcount")
    @ApiOperation(value = "오늘 방문자 수")
    public ResponseEntity<Integer> getVisitorCount() throws SQLException, IOException {

        int count = 0;
        
        try {

            count = adminDao.getCountvisitors();

            return new ResponseEntity<Integer>(count, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            count = -1;
            System.out.println("logger- 방문자 수 에러" + count);

            return new ResponseEntity<Integer>(count, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/admin/getvisitors")
    @ApiOperation(value = "방문기록")
    public ResponseEntity<List<Visit>> getVisitors() throws SQLException, IOException {
        // System.out.println("logger - getVisitors: ");
        List<Visit> list = null;

        try {
            list = adminDao.findAllByOrderByVidDesc();
            return new ResponseEntity<List<Visit>>(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<List<Visit>>(list, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/admin/getpayment")
    @ApiOperation(value = "결제내역")
    public ResponseEntity<List<?>> getPayment() throws SQLException, IOException {
        // System.out.println("logger - getPayment: ");
        List<?> list = null;

        try {
            list = orderlistDao.findPurchaseHistory();
            return new ResponseEntity<List<?>>(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<?>>(list, HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/admin/addmenu")
    @ApiOperation(value = "메뉴추가")
    public ResponseEntity<Boolean> addMenu(@RequestBody Branch branch) throws SQLException, IOException {
        // System.out.println("logger - AddMenu: ");

        try {
            branchDao.save(branch);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/menuinfo")
    @ApiOperation(value = "메뉴정보")
    public ResponseEntity<Branch> menuInfo(@RequestParam int menuid) throws SQLException, IOException {
        // System.out.println("logger - : menuInfo: ");
        Branch branch = null;
        try {
            branch = branchDao.findByMenuid(menuid);

            return new ResponseEntity<Branch>(branch, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Branch>(branch, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/popularmenu")
    @ApiOperation(value = "인기메뉴 5가지")
    public ResponseEntity<List<?>> popularMenu() throws SQLException, IOException {
        // System.out.println("logger - : popularmenu: ");
        List<?> list = null;
        try {
            list = orderlistDao.findPopularMenu();

            return new ResponseEntity<List<?>>(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<?>>(list, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/admin/updatemenu")
    @ApiOperation(value = "메뉴 수정")
    public ResponseEntity<Boolean> updateMenu(@RequestBody Branch branch) throws SQLException, IOException {
        // System.out.println("logger - : updateMenu: ");
        try {
            branchDao.save(branch);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @DeleteMapping("/admin/deletemenu")
    @ApiOperation(value = "메뉴 삭제")
    public ResponseEntity<Boolean> deleteMenu(@RequestParam int menuid) throws SQLException, IOException {
        try {
            branchDao.deleteByMenuid(menuid);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/getmonthincome")
    @ApiOperation(value = "월별 수익")
    public ResponseEntity<List<?>> getMonthIncome() throws SQLException, IOException {
        // System.out.println("logger - getMonthIncome method: ");
        List<?> op = null;
        try {
            op = orderlistDao.findMonthIncome();
            return new ResponseEntity<List<?>>(op, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<?>>(op, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/getmonthvisitors")
    @ApiOperation(value = "월별 방문자")
    public ResponseEntity<List<?>> getMonthVisitors() throws SQLException, IOException {
        // System.out.println("logger - getMonthVisitors method: ");
        List<?> op = null;
        try {
            op = adminDao.findMonthVisitors();
            return new ResponseEntity<List<?>>(op, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<?>>(op, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/admin/login")
    @ApiOperation(value = "관리자 로그인")
    public ResponseEntity<String> adminLogin(@RequestBody Useradmin useradmin) throws SQLException, IOException {
        // System.out.println("logger - adminLogin method");
        String token = null;
        try {
            Useradmin dbAdmin = AdminRepository.findByAid(useradmin.getAid());
            if (useradmin.getAid().equals(dbAdmin.getAid()) && useradmin.getPassword().equals(dbAdmin.getPassword())) {
                token = jwtService.createAdminLoginToken(useradmin);
            }

            return new ResponseEntity<String>(token, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(token, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/admin/isAdmin")
    @ApiOperation(value = "관리자 확인")
    public ResponseEntity<Boolean> isAdmin(HttpServletRequest request) throws SQLException, IOException {
        // System.out.println("logger - isAdmin method");
        Boolean flag = false;
        try {
            String token = request.getHeader("jwtToken");
            if (token.equals("null")) {
                System.out.println("null");
                return new ResponseEntity<Boolean>(flag, HttpStatus.OK);
            }
            System.out.println("jwtToken - " + token);
            Useradmin tokenuser = jwtService.getaUseradmin(token);
            if (tokenuser.getAid().equals("test")) {
                flag = true;
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }

            return new ResponseEntity<Boolean>(flag, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(flag, HttpStatus.FORBIDDEN);
        }
    }
}