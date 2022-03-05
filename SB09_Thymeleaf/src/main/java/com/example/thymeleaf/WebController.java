package com.example.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

    @GetMapping()
    public String getHome() {
        return "index";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {

        // Tạo ra thông tin
        List<Info> profile = new ArrayList<>();
        profile.add(new Info("fullname", "Nguyễn Việt Trung"));
        profile.add(new Info("nickname", "viettrungIT3"));
        profile.add(new Info("gmail", "viettrungcntt03@gmail.com"));
        profile.add(new Info("facebook", "https://www.facebook.com/trung.nguyenviet.1508/"));
        profile.add(new Info("github", "https://github.com/viettrungIT3"));

        // Đưa thông tin vào Model
        model.addAttribute("lodaProfile", profile);

        // Trả về template profile.html
        return "profile";
    }
}
