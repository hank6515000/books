package com.example.books.OAth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


public class CustomerOAth2User implements OAuth2User {

    private String name;

    private String email;

    private String clientName;
    private OAuth2User oAuth2User;

    public String getClientName() {
        return this.clientName;
    }


    /**
     * 第三方登入類
     */
    public CustomerOAth2User(OAuth2User oAuth2User , String clientName) {
        this.oAuth2User = oAuth2User;
        this.clientName = clientName;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("email");
    }

    public String getUsername() {
        return oAuth2User.getAttribute("name");
    }
}
