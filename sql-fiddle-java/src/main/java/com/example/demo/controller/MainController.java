package com.example.demo.controller;


import com.example.demo.entity.TableQuery;
import com.example.demo.entity.User;
import com.example.demo.service.QueryService;
import com.example.demo.service.TableQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@RestController
@RequestMapping("fiddle")
public class MainController {

    @Autowired
    private QueryService queryService;

    @Autowired
    private TableQueryService tableQueryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/run")
    public ResponseEntity<?> processingQueries(@RequestBody String query, Principal principal) throws Exception {
        queryService.manageQueries(query, jdbcTemplate, principal.getName());

        return new ResponseEntity<String>("Query prcessed successfully.", HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<TableQuery> getAllButtons(Principal principal) {
        Iterable<TableQuery> allButtons = tableQueryService.findAllButtons(principal.getName());
        return allButtons;
    }

    @GetMapping("/button/{id}")
    public ResponseEntity<?> getButtonById(@PathVariable Long id, Principal principal) {
        TableQuery button = tableQueryService.findByIdAndUserName(id, principal.getName());
        return new ResponseEntity<TableQuery>(button, HttpStatus.OK);
    }

    @GetMapping("/table/{id}")
    public ResponseEntity<?> getTable(@PathVariable Long id, Principal principal) {

    }

}
