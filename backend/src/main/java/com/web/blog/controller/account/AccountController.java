package com.web.blog.controller.account;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.blog.dao.checkvisitor.CheckvisitorDao;
import com.web.blog.dao.user.UserDao;
import com.web.blog.dao.visit.VisitDao;
import com.web.blog.jwt.JwtService;
import com.web.blog.model.BasicResponse;
import com.web.blog.model.checkvisitor.Checkvisitor;
import com.web.blog.model.images.Images;
import com.web.blog.model.user.User;
import com.web.blog.model.visit.Visit;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.*;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@CrossOrigin(origins = { "*" })
@RestController
public class AccountController {
    // static String Paths ="C:/Users/multicampus/Desktop/pjt3//s03p31b107/";
    static String awsPath ="/home/ubuntu/s03p31b107/face_classifier";
    static String execPath = "/home/ubuntu/Jenkins/workspace/alonso/face_classifier";

    @Autowired
    UserDao userDao;

    @Autowired
    CheckvisitorDao checkvisitorDao;

    @Autowired
    VisitDao visitDao;

    @Autowired
    JwtService jwtService;

    @PostMapping("/account/kakaologin")
    @ApiOperation(value = "카카오 로그인")
    public Object viewInfo(@RequestBody User request) throws SQLException, IOException {
        String token = null;
        try {
            Optional<User> userOpt = userDao.findUserByUid(request.getUid());
            if (userOpt.isPresent()) {
                User tokenuser = new User();
                tokenuser.setUid(userOpt.get().getUid());
                tokenuser.setName(userOpt.get().getName());
                token = jwtService.createLoginToken(tokenuser);
                return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/account/signup")
    @ApiOperation(value = "가입하기")
    public Object signup(@Valid @RequestBody User request) {
        String token = null;

        User user = userDao.getUserByEmail(request.getEmail());

        if (user != null) {
            System.out.println("logger - 해당 이메일이 이미 있음 ");

        } else {
            userDao.save(request);
            User tokenuser = new User();
            tokenuser.setUid(request.getUid());
            tokenuser.setName(request.getName());
            token = jwtService.createLoginToken(tokenuser);
            return new ResponseEntity<>(token, HttpStatus.ACCEPTED);

        }
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/authuser")
    @ApiOperation(value = "토큰으로 유저정보 가져오기")
    public Object authUser(HttpServletRequest request) throws SQLException, IOException {
        String token = request.getHeader("jwtToken");
        User tokenuser = jwtService.getUser(token);
        Optional<User> userinfo = userDao.findUserByUid(tokenuser.getUid());
        try {
            if (userinfo.isPresent()) {
                return new ResponseEntity<>(userinfo.get(), HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateuser")
    @ApiOperation(value = "회원정보 수정하기")
    public void updateUser(@RequestBody User updateReq, HttpServletRequest request) throws SQLException, IOException {
        try {
            userDao.save(updateReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/account/justlearn")
    public void justlearning() {
        ResponseEntity<?> response = null;

        String[] command = new String[2];
        command[0] = "python3";
        command[1] = execPath+"/only_train.py";
        try {
            ByteArrayOutputStream out = execPython(command);
            String extact_result = out.toString();
            System.out.println(extact_result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/imageset")
    public ResponseEntity<?> sendImage(@Valid @RequestBody Images[] images) {
        if (!images[0].getFileBase64().equals("")) {
            for (int i = 1; i < images.length; i++) {
                String base64Str = new String(images[i].getFileBase64());
                String data = base64Str.split(",")[1]; // jin

                byte decode[] = Base64.decodeBase64(data);
                FileOutputStream fos;
                try {

                    String target_path = awsPath+"/train/"
                            + images[0].getFileBase64() + "/";

                    File Folder = new File(target_path);

                    // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
                    if (!Folder.exists()) {
                        try {
                            Folder.mkdir(); // 폴더 생성합니다.
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                    File target = new File(target_path + i + ".jpg");
                    target.createNewFile();
                    fos = new FileOutputStream(target);
                    fos.write(decode);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ResponseEntity<?> response = null;

        response = new ResponseEntity<>(null, HttpStatus.OK);

        return response;
    };

    @PostMapping("/decoding")
    public ResponseEntity<?> kioskDecoding(@Valid @RequestBody Images[] images) {
    // public ResponseEntity<?> kioskDecoding() {
        System.out.println("HIIIIIIIII");
        for (int i = 0; i < images.length; i++) {
            String base64Str = new String(images[i].getFileBase64());
            String data = base64Str.split(",")[1]; // jin

            byte decode[] = Base64.decodeBase64(data);
            FileOutputStream fos;
            try {

                // String target_path = Paths+"face_classifier/test/";
                String target_path = awsPath+"/test/";

                File Folder = new File(target_path);

                // 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
                if (!Folder.exists()) {
                    try {
                        Folder.mkdir(); // 폴더 생성합니다.
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                File target = new File(target_path + i + ".jpg");
                target.createNewFile();
                fos = new FileOutputStream(target);
                fos.write(decode);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ResponseEntity<?> response = null;

        response = new ResponseEntity<>(null, HttpStatus.OK);

        return response;
    };

    @PostMapping("/kiosk/recog")
    @ApiOperation(value = "회원일 때 얼굴 인식")
    public ResponseEntity<?> recog() {
        ResponseEntity<?> response = null;
        BasicResponse result = new BasicResponse();
        StringBuffer res = new StringBuffer();

        String[] command = new String[2];
        command[0] = "python3";
        // command[1] =
        // "C:\\Users\\multicampus\\Desktop\\project3\\s03p31b107\\face_classifier\\face_recognition_mlp.py";
        command[1] = execPath+"/face_recognition_knn.py";
        try {
            ByteArrayOutputStream out = execPython(command);
            String extact_result = out.toString();

            for (int i = 0; i < extact_result.length(); i++) {
                char c = extact_result.charAt(i);
                if (c == '\n' || c == '\r') {
                    break;
                } else if (c != ' ') {
                    res.append(c);
                }
            }

            if (res.toString().split(":")[0].equals("CORRECT")) {
                result.data = "가입된 유저입니다.";
                result.object = res.toString().split(":")[1];
                Visit v = new Visit();
                v.setTel(userDao.findUserByUid(Integer.parseInt(res.toString().split(":")[1])).get().getTel());
                v.setUid(Integer.parseInt(res.toString().split(":")[1]));
                visitDao.save(v);

            } else {
                result.data = "찾을 수 없는 유저입니다.";
                result.object = "Unknown";
            }

            final Checkvisitor addvisitor = new Checkvisitor();
            addvisitor.setUid(result.object.toString());
            checkvisitorDao.save(addvisitor);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(result.data);
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;

    }

    @Transactional
    @GetMapping("/tracking/start")
    public ResponseEntity<?> trackingst(@RequestParam(required = true) String tid) {
        ResponseEntity<?> response = null;
        BasicResponse result = new BasicResponse();
        String[] command = new String[3];

        command[0] = "python3";
        // command[1] =
        // "C:\\Users\\multicampus\\Desktop\\project3\\s03p31b107\\face_classifier\\face_recognition_mlp.py";
        // command[1] = "C:\\do\\face_classifier\\face_recognition_knn.py";
        command[1] = "/home/team7/s03p31b107/darknet/python/darknet_2.py";
        command[2] = tid;

        try {
            ByteArrayOutputStream out = execPython(command);
            System.out.println(out);
            String extact_result = out.toString();
            for (int i = 0; i < extact_result.length(); i++) {
                char c = extact_result.charAt(i);
                if (c == '\n' || c == '\r') {
                    break;
                } else if (c != ' ') {
                }
            }

            checkvisitorDao.deleteByUid(tid);
            result.data = extact_result.substring(extact_result.indexOf("$start") + 6,
                    extact_result.indexOf("$end") - 1);
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;

    }

    @GetMapping("/tracking")
    @ApiOperation(value = "트래킹")
    public Object tracking(@RequestParam(required = true) String tid) {
        String token = null;
        if (tid.equals("Unknown")) {
            return new ResponseEntity<>("Unknown", HttpStatus.ACCEPTED);
        }
        try {
            Optional<User> userOpt = userDao.findUserByUid(Integer.parseInt(tid));
            if (userOpt.isPresent()) {
                User tokenuser = new User();
                tokenuser.setUid(userOpt.get().getUid());
                tokenuser.setName(userOpt.get().getName());
                token = jwtService.createLoginToken(tokenuser);
                System.out.println(token);
                return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/visitor")
    @ApiOperation(value = "방문자 확인")
    public Object visitor() {

        List<Checkvisitor> visitorlist = checkvisitorDao.findAll();

        ResponseEntity<Object> response = null;

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "방문자 확인";
        result.object = visitorlist;
        response = new ResponseEntity<>(result, HttpStatus.OK);
        return response;
    }

    public static ByteArrayOutputStream execPython(String[] command) throws IOException, InterruptedException {
        CommandLine commandLine = CommandLine.parse(command[0]);
        for (int i = 1, n = command.length; i < n; i++) {
            commandLine.addArgument(command[i]);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream);
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(pumpStreamHandler);
        int[] ev = { 0, 1, 2 };
        executor.setExitValues(ev);
        int result = executor.execute(commandLine);
        return outputStream;
    }

    @PostMapping("/qr")
    @ApiOperation(value = "QR 코드 체크인")
    public ResponseEntity<Boolean> QRCheckIn() throws SQLException, IOException {
        // System.out.println("logger - AddMenu: ");
        Visit visit = new Visit();
        visit.setUid(0);
        visit.setTel("010-0000-0000");
        Checkvisitor cv = new Checkvisitor();
        cv.setUid("Unknown");
        try {
            visitDao.save(visit);
            checkvisitorDao.save(cv);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
    }
}