package com.project4.restaurant.domain.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import feign.Contract;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.client.JettyClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

@Configuration
public class AppConfig {
  @Bean
  public Contract feignContract() {
    return new Contract.Default();
  }

//  @Bean
//  public TokenInterceptor tokenInterceptor(AuthClientFactory authClient) {
//    return new TokenInterceptor(authClient);
//  }

  @Bean
  public RestClient restClient() {

    HttpClient httpClient = new HttpClient();

    JettyClientHttpRequestFactory requestFactory = new JettyClientHttpRequestFactory(httpClient);
    requestFactory.setConnectTimeout(10 * 1000);
    requestFactory.setReadTimeout(10 * 1000L);

    return RestClient.builder().requestFactory(requestFactory).build();
  }

  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

  @Bean
  public LocaleResolver localeResolver() {
    AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
    acceptHeaderLocaleResolver.setDefaultLocale(Locale.forLanguageTag("vi"));
    return acceptHeaderLocaleResolver;
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("ValidationMessages");
    messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
  }

  @Bean
  public RestTemplate rest(RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public Encoder feignEncoder() {
    return new SpringEncoder(messageConverters(this.objectMapper()));
  }

  @Bean
  public Decoder feignDecoder() {
    return new SpringDecoder(messageConverters(this.objectMapper()));
  }

  private ObjectFactory<HttpMessageConverters> messageConverters(ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
    return () -> new HttpMessageConverters(converter);
  }

  private ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(new ParameterNamesModule())
        .registerModule(new Jdk8Module())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new JavaTimeModule());

    SimpleModule module = new SimpleModule();
    module.addSerializer(Double.class, new DoubleSerializer());
    mapper.registerModule(module);

    return mapper;
  }

  static class DoubleSerializer extends JsonSerializer<Double> {
    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
      if (value != null) {
        gen.writeString(BigDecimal.valueOf(value).toPlainString());
      }
    }
  }
}
