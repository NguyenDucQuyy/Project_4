package com.project4.restaurant.domain.config.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.log4j.Log4j2;
import org.redisson.codec.JsonJacksonCodec;

@Log4j2
public class CustomJsonJacksonCodec extends JsonJacksonCodec {
  public CustomJsonJacksonCodec() {
    super(objectMapper());
  }

  public static ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();

    mapper
        .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JavaTimeModule());
    return mapper;
  }

//  @Override
//  protected void initTypeInclusion(ObjectMapper mapObjectMapper) {
//    log.debug("=========initTypeInclusion");
//  }
}
