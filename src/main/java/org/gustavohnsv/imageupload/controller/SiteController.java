package org.gustavohnsv.imageupload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class SiteController {

    @GetMapping("/")
    public String home() {
        return "Home";
    }

    @GetMapping("/img/")
    public String images() {
        return "Images";
    }

}
