package com.project4.restaurant.app.response.base;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class Metadata {
  private final int page;
  private final long pageSize;
  private final long count;
  private final long totalElements;
  private final long totalPages;

  public static Metadata createFrom(Page<?> page) {
    return new Metadata(
        page.getNumber(),
        page.getSize(),
        page.getNumberOfElements(),
        page.getTotalElements(),
        page.getTotalPages());
  }
}
