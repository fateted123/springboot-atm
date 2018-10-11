package com.dayuanit.dy9.springboot.atm.springbootatm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dayuanit.dy9.springboot.atm.springbootatm.dto.ResponseDTO;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.User;
import com.dayuanit.dy9.springboot.atm.springbootatm.entity.WxUserInfo;
import com.dayuanit.dy9.springboot.atm.springbootatm.service.UserService;
import com.dayuanit.dy9.springboot.atm.springbootatm.service.WxAuthenService;
import com.dayuanit.dy9.springboot.atm.springbootatm.util.ApiConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class WxController extends BaseController {

    @Autowired
    private WxAuthenService wxAuthenService;

    @Autowired
    private UserService userService;

    @RequestMapping("/wx/codeUrl")
    @ResponseBody
    public ResponseDTO codeUrl() {
        String url = "http://payhub.dayuanit.com/weixin/connect/qrconnect.do?" +
                "appid=2018101016575949709&redirect_uri=http://127.0.0.1:8080/wx/loginCallback&response_type=code&scope=snsapi_login";
        return ResponseDTO.sucess(url);
    }

    @RequestMapping("/wx/loginCallback")
    public String wxLoginCallback(String code, HttpSession session) {
        System.out.println("code=" + code);

        String accessTokenUrl = "http://payhub.dayuanit.com/weixin/sns/oauth2/access_token.do?appid=2018101016575949709&secret=97440330404382865996040691261594" +
                "&code="+code+"&grant_type=authorization_code";

        String result = ApiConnector.get(accessTokenUrl, null);
        System.out.println("result=" + result);

        final JSONObject jsonObject = JSON.parseObject(result);
        String accessToken = jsonObject.getString("access_token");
        String openid= jsonObject.getString("openid");

        //TODO 获取微信用户信息
        String wxUserUrl = "http://payhub.dayuanit.com/weixin/sns/userinfo.do?access_token="+accessToken+"&openid=" + openid;
        result = ApiConnector.get(wxUserUrl, null);
        System.out.println("用户信息=" + result);

        final JSONObject userInfoJsonObject = JSON.parseObject(result);

        //去绑定页面 or 自动登录去个人中心
        final WxUserInfo wxUserInfo = wxAuthenService.getWxUserInfo(openid);
        if (null == wxUserInfo) {
            //TODO 去绑定页面
            session.setAttribute("nickName", userInfoJsonObject.getString("nickname"));
            session.setAttribute("headimgurl", userInfoJsonObject.getString("headimgurl"));
            session.setAttribute("openid", openid);
            return "redirect:/wx/toBind";
        } else {
            //自动登录去个人中心
            User user = userService.getUserById(wxUserInfo.getUserId());
            session.setAttribute("loginUser", user);
            return "redirect:/";
        }

    }

    @RequestMapping("/wx/wxBind")
    @ResponseBody
    public ResponseDTO wxBind(String username, String pwd, HttpSession session) {

        try {
            User user = userService.login(username, pwd);
            // 添加绑定数据
            wxAuthenService.saveBindInfo(user.getId(), (String)session.getAttribute("openid"),
                    (String)session.getAttribute("nickName"), (String)session.getAttribute("headimgurl"));

            session.setAttribute("loginUser", user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDTO.faild(e.getMessage());
        }

        return ResponseDTO.sucess();

    }

}
