package com.dayuanit.dy9.springboot.atm.springbootatm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController extends BaseController {

    @RequestMapping("/card/toOpenAccount")
    public String toOpenAccount() {
        return "openaccount";
    }

    @RequestMapping("/card/toDeposit")
    public String toDeposit() {
        return "deposit";
    }

    @RequestMapping("/card/toFlow")
    public String toFlow() {
        return "flow";
    }

    @RequestMapping("/card/toTransfer")
    public String toTransfer() {
        return "transfer";
    }

    @RequestMapping("/")
    public String toHome() {
        return "index";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/wx/toBind")
    public String toWxBind() {
        return "wxBind";
    }
}
