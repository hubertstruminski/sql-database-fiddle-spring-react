package com.example.demo.controller;


import com.example.demo.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("fiddle")
public class MainController {

    @Autowired
    private QueryService queryService;

    @PostMapping
    public ResponseEntity<?> processingQueries(@RequestBody String query) throws UnsupportedEncodingException {
        String[] splittedUrl = queryService.decodeAndSplitUrl(query);

        return new ResponseEntity<String>("Query prcessed successfully.", HttpStatus.OK);
    }

}
