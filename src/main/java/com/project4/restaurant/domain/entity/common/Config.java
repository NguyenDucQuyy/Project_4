package com.project4.restaurant.domain.entity.common;

import com.project4.restaurant.domain.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "config")
public class Config extends BaseEntity {
  @Transient
  public static final String SEQUENCE_NAME = "config_sequence";

  @Id
  private Integer id;

  @Field(name = "key_config")
  private String keyConfig;

  @Field(name = "value_config")
  private String valueConfig;
}