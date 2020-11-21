package com.adaptavist.example.demo;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    @GetMapping("/")
    public String getMessageOfTheDay(@AuthenticationPrincipal OidcUser user) {
        return user.getName() + ", this message of the day is boring";
    }

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }

    @GetMapping("/mail")
    public String getUserEmail(AbstractOAuth2TokenAuthenticationToken authentication) {
        // AbstractOAuth2TokenAuthenticationToken works for both JWT and opaque access tokens
        return (String) authentication.getTokenAttributes().get("sub");
    }
}
