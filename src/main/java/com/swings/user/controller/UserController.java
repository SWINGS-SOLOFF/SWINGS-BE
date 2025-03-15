package com.swings.user.controller;

import com.swings.user.entity.UserEntity;
import com.swings.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //회원가입
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "signup";
    }

    //회원가입 처리
    @PostMapping("/signup")
    public String signUser(@ModelAttribute UserEntity Id) {
        userService.registerUser(Id);
        return "redirect:/user/login";
    }

    //로그인 화면
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String loginUser(@RequestParam String Id, @RequestParam String password) {
        UserEntity user = userService.findById(Id);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/user/profile/" + Id;
        }
        return "redirect:/user/login?error";
    }

    //회원목록 조회
    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUser());
        return "list";
    }

    //회원정보 수정 화면
    @GetMapping("/edit/{Id}")
    public String editUserForm(@PathVariable String Id, Model model) {
        UserEntity userId = userService.findById(Id);
        model.addAttribute("user", userId);
        return "edit";
    }

    //회원정보 수정 처리
    @PostMapping("/edit/{username}")
    public String editUser(@PathVariable String Id, @ModelAttribute UserEntity user) {
        user.setId(Id);
        userService.updateUser(user);
        return "redirect:/user/profile/" + Id;
    }

    //회원정보 삭제
    @GetMapping("/delete/{Id}")
    public String deleteUser(@PathVariable String Id) {
        userService.deleteUser(Id);
        return "redirect:/user/list/";
    }

    //사용자 프로필
    @GetMapping("/profile/{Id}")
    public String viewUser(@PathVariable String Id, Model model) {
        UserEntity user = userService.findById(Id);
        model.addAttribute("user", user);
        return "profile";
    }


}