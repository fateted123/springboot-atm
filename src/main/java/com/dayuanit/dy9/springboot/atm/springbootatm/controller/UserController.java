package com.dayuanit.dy9.springboot.atm.springbootatm.controller;

import com.dayuanit.dy9.springboot.atm.springbootatm.dto.ResponseDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.User;
import com.dayuanit.dy9.springboot.atm.springbootatm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.*;

@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user/uploadAvatar")
    public void uploadAvatar(MultipartFile avatar, HttpSession session, HttpServletResponse response) {

        try (OutputStream os = response.getOutputStream()){
            avatar.transferTo(new File("D:/atm/avatar/" + getUserId(session)));
            os.write("<script>parent.showAvatar();</script>".getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/show/avatar")
    public void avatar(HttpSession session, HttpServletResponse response) {

        try(OutputStream os = response.getOutputStream();
            InputStream is = new FileInputStream("D:/atm/avatar/" + getUserId(session))) {
            byte[] buff = new byte[1024];
            int length = -1;
            while (-1 != (length = is.read(buff))) {
                os.write(buff, 0, length);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/login")
    @ResponseBody
    public ResponseDTO login(String username, String pwd, HttpSession session) {
        User user = userService.login(username,pwd);

        session.setAttribute("loginUser", user);
        return ResponseDTO.sucess();
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loginUser");
        session.invalidate();
        return "redirect:/toLogin";
    }

}
