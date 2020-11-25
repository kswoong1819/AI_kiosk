package com.web.blog.controller.orderlist;

import com.web.blog.dao.branch.BranchDao;
import com.web.blog.dao.orderlist.OrderlistDao;
import com.web.blog.dao.store.StoreDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.branch.Branch;
import com.web.blog.model.branch.BranchRequest;
import com.web.blog.model.orderlist.Orderlist;
import com.web.blog.model.orderlist.OrderlistRequest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Locale.Category;

import javax.validation.Valid;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
public class OrderlistController {

    @Autowired
    UserDao userDao;

    @Autowired
    BranchDao BranchDao;

    @Autowired
    StoreDao StoreDao;

    @Autowired
    OrderlistDao OrderlistDao;

    @GetMapping("/get/orderlist")
    @ApiOperation(value = "주문 메뉴 전체 목록")
    public Object getorderlist(@RequestParam(required = true) int uid, int sid) {

        ArrayList<Orderlist> orderlistlist = OrderlistDao.findOrderlistByUidAndSidOrderByOrderdateDesc(uid, sid);

        ArrayList<Object> ret = new ArrayList<>();
        for (int i = 0; i < orderlistlist.size(); i++) {
            int menuid = orderlistlist.get(i).getMenuid();
            List<Branch> menulist = BranchDao.findBranchByMenuid(menuid);
            ret.add(menulist.get(0));
        }

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "주문 메뉴 전체 목록 조회 완료";
        result.object = ret;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/get/orderlist2")
    @ApiOperation(value = "주문 메뉴 전체 목록2")
    public Object getorderlist2(@RequestParam(required = true) int uid, int sid) {
        ArrayList<Orderlist> orderlistlist = OrderlistDao.findOrderlistByUidAndSidOrderByOrderdateDesc(uid, sid);
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "주문 메뉴 전체 목록 조회 완료";
        result.object = orderlistlist;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/get/orderlist/recent")
    @ApiOperation(value = "최신 주문 메뉴")
    public Object getorderlistrecent(@RequestParam(required = true) int uid, int sid) {
        Orderlist orderlistrecent = new Orderlist();
        ResponseEntity<Object> response = null;

        ArrayList<Orderlist> asdf;
        // orderlistrecent =
        // OrderlistDao.find1OrderlistByUidAndSidOrderByOrderdateDesc(uid, sid);

        asdf = OrderlistDao.findOrderlistByUidAndSidOrderByOrderdateDesc(uid, sid);

        BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "최신 주문 메뉴 조회 완료";
        result.object = asdf;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @PostMapping("/create/order")
    @ApiOperation(value = "주문 메뉴")
    public Object orderMenu(@Valid @RequestBody final OrderlistRequest[] orderlistRequest) {
        ResponseEntity<Object> response = null;
        // Optional<User> user = userDao.findUserByUid(orderlistRequest);
        int orderuid = orderlistRequest[0].getUid();
        for (int i = 0; i < orderlistRequest.length; i++) {
            final Orderlist orderlist = new Orderlist();
            // orderlist.setUid(user.get().getUid());
            orderlist.setUid(orderuid);
            orderlist.setMenuid(orderlistRequest[i].getMenuid());
            orderlist.setSid(orderlistRequest[i].getSid());
            final Orderlist saveOrderlist = this.OrderlistDao.save(orderlist);
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "주문 메뉴 생성 완료";
            result.object = saveOrderlist;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/mypage/searchorder")
    @ApiOperation(value = "주문 조회")
    public Object searchOrder(@RequestParam(required = true) int uid, int sid) {
        List<Orderlist> orderlist = OrderlistDao.findOrderlistByUidAndSidOrderByOrderdateDesc(uid, sid);
        List<List<String>> resultlist = new LinkedList<>();
        for (int i = 0; i < orderlist.size(); i++) {
            List<String> sublist = new LinkedList<>();
            sublist.add(orderlist.get(i).getOrderdate());
            sublist.add(StoreDao.test(orderlist.get(i).getSid()));
            sublist.add(BranchDao.findBranchBySidAndMenuid(orderlist.get(i).getSid(), orderlist.get(i).getMenuid())
                    .getName());
            sublist.add(BranchDao.findBranchBySidAndMenuid(orderlist.get(i).getSid(), orderlist.get(i).getMenuid())
                    .getImage());
            sublist.add(Integer.toString(BranchDao
                    .findBranchBySidAndMenuid(orderlist.get(i).getSid(), orderlist.get(i).getMenuid()).getPrice()));
            resultlist.add(sublist);
        }
        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "개인 주문 메뉴 전체 목록 조회 완료";
        result.object = resultlist;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    @GetMapping("/order/mymenu")
    @ApiOperation(value = "최근 먹은 주문 메뉴")
    public Object orderlistrecent(@RequestParam(required = true) int uid, int sid) {
        System.out.println("logger - 최신 주문 메뉴");

        List<Orderlist> orderlistlist = OrderlistDao.findOrderlistByUidAndSidOrderByOrderdateDesc(uid, sid);
        List<Branch> menulist = BranchDao.findBranchBySid(sid);
        // System.out.println(orderlistlist);
        HashSet<Object> ret = new HashSet<>();
        for (int i = 0; i < menulist.size(); i++) {
            for (int j = 0; j < orderlistlist.size(); j++) {
                if (menulist.get(i).getMenuid() == orderlistlist.get(j).getMenuid()) {
                    ret.add(menulist.get(i));
                }
            }
        }
        System.out.println(ret);
        return ret;
    }

    @GetMapping("/order/hotcurrentdrink")
    @ApiOperation(value = "시간대 별 음료 추천 메뉴")
    public Object hotmenudrink() {
        ArrayList<Orderlist> list = null;
        ArrayList<Branch> hotmenudrink = new ArrayList<>();
        Date date_now = new Date(System.currentTimeMillis());
        list = OrderlistDao.hotmenutimes();
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyyMMddHHmmss");
        int ha = Integer.parseInt(fourteen_format.format(date_now).substring(8, 10));
        int cnt = 0;
        for (int j = 0; j < list.size(); j++) {
            if (ha < 12 && Integer.parseInt(list.get(j).getOrderdate().substring(11, 13)) < 12) {
                ArrayList<Branch> ls = BranchDao.findBranchByMenuidAndCategory1(list.get(j).getMenuid(), 1);
                for (int i = 0; i < ls.size(); i++) {
                    if (ls.get(i) != null) {
                        if (cnt == 3)
                            break;
                        hotmenudrink.add(ls.get(i));
                        cnt++;
                    }
                }
            } else {
                ArrayList<Branch> ls = BranchDao.findBranchByMenuidAndCategory1(list.get(j).getMenuid(), 1);
                for (int i = 0; i < ls.size(); i++) {
                    if (ls.get(i) != null) {
                        if (cnt == 3)
                            break;
                        hotmenudrink.add(ls.get(i));
                        cnt++;
                    }
                }
            }

        }

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "시간대별 음료 추천 완료";
        result.object = hotmenudrink;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;

    }

    @GetMapping("/order/hotcurrentfood")
    @ApiOperation(value = "시간대 별 푸드 메뉴")
    public Object hotmenufood() {
        ArrayList<Orderlist> list = null;
        ArrayList<Branch> hotmenufoods = new ArrayList<>();
        Date date_now = new Date(System.currentTimeMillis());
        list = OrderlistDao.hotmenutimes();
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyyMMddHHmmss");
        int ha = Integer.parseInt(fourteen_format.format(date_now).substring(8, 10));
        int cnt = 0;
        for (int j = 0; j < list.size(); j++) {
            if (ha < 12 && Integer.parseInt(list.get(j).getOrderdate().substring(11, 13)) < 12) {
                ArrayList<Branch> ls = BranchDao.findBranchByMenuidAndCategory1(list.get(j).getMenuid(), 2);
                for (int i = 0; i < ls.size(); i++) {
                    if (ls.get(i) != null) {
                        if (cnt == 3)
                            break;
                        hotmenufoods.add(ls.get(i));
                        cnt++;
                    }
                }
            } else {

                ArrayList<Branch> ls = BranchDao.findBranchByMenuidAndCategory1(list.get(j).getMenuid(), 2);
                for (int i = 0; i < ls.size(); i++) {
                    if (ls.get(i) != null) {
                        if (cnt == 3)
                            break;
                        hotmenufoods.add(ls.get(i));
                        cnt++;
                    }
                }
            }

        }

        ResponseEntity<Object> response = null;
        BasicResponse result = new BasicResponse();

        result.status = true;
        result.data = "시간대별 푸드 추천 완료";
        result.object = hotmenufoods;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;

    }

}
