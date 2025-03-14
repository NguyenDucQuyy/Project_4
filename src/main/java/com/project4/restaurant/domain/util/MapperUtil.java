package com.project4.restaurant.domain.util;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperUtil {

  private ModelMapper modelMapper;

  @PostConstruct
  public void init() {
    modelMapper = new ModelMapper();
  }

  public <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
    return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
  }

  public <T> Page<T> listToPage(List<T> tList, Pageable pageable) {

    int start = (int) pageable.getOffset();
    int end = (Math.min((start + pageable.getPageSize()), tList.size()));

    return new PageImpl<>(tList.subList(start, end), pageable, tList.size());
  }
}
