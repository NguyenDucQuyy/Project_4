package com.project4.restaurant.app.response.base;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {
  public final List<T> data;
  public final Metadata metadata;

  public static <T> PageResponse<T> createFrom(Page<T> pageData) {
    return (PageResponse<T>) new PageResponse(pageData.getContent(), Metadata.createFrom(pageData));
  }
}
