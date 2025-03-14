//package com.project4.restaurant.app.controller.publics;
//
//import com.project4.restaurant.domain.config.security.filter.TokenInfo;
//import com.project4.restaurant.domain.service.UserAccountService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("project_4/restaurant/drop-list")
//@PreAuthorize("hasRole('ADMIN') or hasRole('LEADERSHIP') or hasRole('SECRETARIAT') or hasRole('DEPARTMENT')")
//public class DropListController {
//  @Autowired private DepartmentService departmentService;
//  @Autowired private UserAccountService userAccountService;
//
//  @GetMapping("/departments")
//  public ResponseEntity<?> getAllDepartmentsDropList() {
//    TokenInfo tokenInfo = (TokenInfo) SecurityContextHolder.getContext().getAuthentication();
//    return ResponseEntity.ok(departmentService.getAllDepartmenSDropList(tokenInfo.getUserId()));
//  }
//
//  @GetMapping("/leaderships")
//  public ResponseEntity<?> getAllLeadershipDropList() {
//    return ResponseEntity.ok(userAccountService.getAllLeadershipDropList());
//  }
//}
