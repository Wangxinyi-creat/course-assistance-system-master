package com.zhedian.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class ManageAdminApplicationTests {


    @Test
    void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("000000");
        boolean matches = bCryptPasswordEncoder.matches("123123", encode);
        System.out.println(encode);
        System.out.println(matches);
    }

}
