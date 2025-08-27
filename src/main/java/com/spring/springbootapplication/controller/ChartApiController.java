// package com.spring.springbootapplication.controller;

// import com.spring.springbootapplication.service.SkillChartService;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;
// import java.util.Map;

// @RestController
// public class ChartApiController {

//     private final SkillChartService skillChartService;

//     public ChartApiController(SkillChartService skillChartService) {
//         this.skillChartService = skillChartService;
//     }

//     @GetMapping("/api/chart-data")
//     public Map<String, Map<String, Integer>> getChartData() {

//         return skillChartService.getChartData();
//     }
// }