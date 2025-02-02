package org.gustavohnsv.imageupload.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfTokenController {

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(CsrfToken token) {
        return token;
    }

}
