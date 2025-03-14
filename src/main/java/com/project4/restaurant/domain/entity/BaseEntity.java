package com.project4.restaurant.domain.entity;

import com.project4.restaurant.domain.util.Helper;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
public class BaseEntity {
  @Field(name = "created_at")
  //  @CreatedDate
  protected long createdAt = Helper.getNowMillisAtUtc();

  @Field(name = "updated_at")
  //  @LastModifiedDate
  protected long updatedAt = Helper.getNowMillisAtUtc();
}
