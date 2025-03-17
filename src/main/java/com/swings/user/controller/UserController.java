package com.swings.user.controller;

import com.swings.user.entity.UserEntity;
import com.swings.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //회원가입
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "signup";
    }

    //회원가입 처리
    @PostMapping("/signup")
    public String signUser(@ModelAttribute UserEntity user) {
        userService.registerUser(user);
        return "redirect:/user/login";
    }

    //로그인 화면
    @GetMapping("/login")
    public String loginForm(){
        return "login";
    }

    //로그인 처리
    @PostMapping("/login")
    public String loginUser(@RequestParam String id, @RequestParam String password) {
        UserEntity user = userService.findById(id);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return "redirect:/user/profile/" + id;
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
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable String id, Model model) {
        UserEntity userId = userService.findById(id);
        model.addAttribute("user", userId);
        return "edit";
    }

    //회원정보 수정 처리
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable String id, @ModelAttribute UserEntity user) {
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/user/profile/" + id;
    }

    //회원정보 삭제
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "redirect:/user/list/";
    }

    //사용자 프로필
    @GetMapping("/profile/{id}")
    public String viewUser(@PathVariable String id, Model model) {
        UserEntity user = userService.findById(id);
        model.addAttribute("user", user);
        return "profile";
    }


}