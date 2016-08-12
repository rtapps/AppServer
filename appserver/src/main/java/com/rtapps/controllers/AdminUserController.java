package com.rtapps.controllers;

import com.rtapps.controllers.webdata.AdminUserData;
import com.rtapps.db.mongo.data.AdminUser;
import com.rtapps.db.mongo.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by rtichauer on 8/12/16.
 */
@Controller
public class AdminUserController {

    @Autowired
    AdminUserRepository adminUserRepository;


    @RequestMapping("/adminUser")
    @ResponseBody
    public AdminUserData getAdminUser(@RequestParam(value = "username") String username) {

        AdminUser adminUser = adminUserRepository.findByUsername(username);

        return new AdminUserData(adminUser);
    }
}
