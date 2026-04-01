package com.deep.moneymanager.controller;


import com.deep.moneymanager.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DashBordController {


    private final DashBoardService dashBoardService;

    public ResponseEntity<Map<String,Object>> getDashboardData(){
        Map<String,Object>dashboardData=dashBoardService.getDashboardData();
        return  ResponseEntity.ok(dashboardData);
    }
}
