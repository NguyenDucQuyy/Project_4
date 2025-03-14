package com.project4.restaurant.app.controller.admin;

import com.project4.restaurant.app.dto.admin.ConfigCreateDto;
import com.project4.restaurant.app.dto.admin.ConfigUpdateDto;
import com.project4.restaurant.app.response.admin.ConfigResponse;
import com.project4.restaurant.app.response.base.PageResponse;
import com.project4.restaurant.domain.service.admin.ConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "project_4/restaurant/admin/config")
@Tag(name = "Cấu hình hệ thống")
public class ConfigController {
  @Autowired
  private ConfigService configService;

  @GetMapping("")
  public ResponseEntity<PageResponse<ConfigResponse>> findAll(Pageable pageable) {
    return ResponseEntity.ok(
        PageResponse.createFrom(
            configService.findAll(pageable)
        )
    );
  }

  @GetMapping("/{id}")
  public ResponseEntity<ConfigResponse> getDetail(@PathVariable Integer id) {
    return ResponseEntity.ok(configService.getDetail(id));
  }

  @PostMapping("")
  public ResponseEntity<Boolean> create(@RequestBody @Valid ConfigCreateDto dto) {
    configService.create(dto);
    return ResponseEntity.ok(true);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Boolean> update(@PathVariable Integer id, @RequestBody @Valid ConfigUpdateDto dto) {
    configService.update(id, dto);
    return ResponseEntity.ok(true);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
    configService.delete(id);
    return ResponseEntity.ok(true);
  }
}
