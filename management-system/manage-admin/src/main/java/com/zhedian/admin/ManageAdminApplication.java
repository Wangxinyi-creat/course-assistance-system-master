package com.zhedian.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication(scanBasePackages = {"com.zhedian.*"})
public class ManageAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageAdminApplication.class, args);
    }

}
