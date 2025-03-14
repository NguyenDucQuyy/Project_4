package com.project4.restaurant.domain.entity;

import com.project4.restaurant.domain.util.Helper;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
  @Field(name = "created_at")
  private Long createdAt = Helper.getNowMillisAtUtc();

  @Field(name = "updated_at")
  private Long updatedAt = Helper.getNowMillisAtUtc();

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Helper.getNowMillisAtUtc();
  }
}
