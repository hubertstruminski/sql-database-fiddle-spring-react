package com.example.demo.controller;


import com.example.demo.entity.User;
import com.example.demo.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Controller
@RequestMapping("fiddle")
public class MainController {

    @Autowired
    private QueryService queryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping
    public ResponseEntity<?> processingQueries(@RequestBody String query, Principal principal) throws Exception {
        queryService.manageQueries(query, jdbcTemplate, principal.getName());

        return new ResponseEntity<String>("Query prcessed successfully.", HttpStatus.OK);
    }

}
