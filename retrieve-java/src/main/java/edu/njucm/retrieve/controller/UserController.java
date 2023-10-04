package edu.njucm.retrieve.controller;

import edu.njucm.retrieve.model.User;
import edu.njucm.retrieve.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/user")
@CrossOrigin
public class UserController {


    @Autowired
    private UserService userService;

    //登录
    @PostMapping("/login")
    public @ResponseBody int login(@RequestBody User user) {
        return userService.login(user);
    }

    //注册
    @PostMapping("/register")
    public @ResponseBody int register(@RequestBody User user) {
        return userService.register(user);
    }


}
