package com.github.paulosalonso.notification.adapter.api.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

import static java.util.function.Predicate.not;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.*;

@Configuration
@EnableWebMvc
@EnableOpenApi
public class OpenApiConfiguration implements WebMvcConfigurer {

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .ignoredParameterTypes(ServletWebRequest.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any().and(not(PathSelectors.ant("/error"))))
                .build()
                .globalResponses(GET, globalGetDeleteResponseMessages())
                .globalResponses(DELETE, globalGetDeleteResponseMessages())
                .globalResponses(POST, globalPostPutResponseMessages())
                .globalResponses(PUT, globalPostPutResponseMessages())
                .useDefaultResponseMessages(false)
                .apiInfo(buildApiInfo());

        addTags(docket);

        return docket;
    }

    private void addTags(Docket docket) {
        docket.tags(new Tag("Notifications", "Notifications Operations"));
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("Notification Scheduler API")
                .description("Schedule notifications for future shipping")
                .version("0.0.1-SNAPSHOT")
                .contact(new Contact("Paulo Alonso",
                        "https://www.linkedin.com/in/paulo-alonso-67b082149/", "paulo_alonso_@hotmail.com"))
                .build();
    }

    private List<Response> globalPostPutResponseMessages() {
        return List.of(
                new ResponseBuilder()
                        .code(getHttpStatus(NOT_ACCEPTABLE))
                        .description("Resource has no representation that could be accepted by the consumer")
                        .build(),
                new ResponseBuilder()
                        .code(getHttpStatus(UNSUPPORTED_MEDIA_TYPE))
                        .description("Request declined because the payload media type is not supported")
                        .build()
                ,new ResponseBuilder()
                        .code(getHttpStatus(INTERNAL_SERVER_ERROR))
                        .description("Internal server error")
                        .build());
    }

    private List<Response> globalGetDeleteResponseMessages() {
        return List.of(new ResponseBuilder()
                        .code(getHttpStatus(INTERNAL_SERVER_ERROR))
                        .description("Internal server error")
                        .build());
    }

    private String getHttpStatus(HttpStatus status) {
        return Integer.toString(status.value());
    }

}
