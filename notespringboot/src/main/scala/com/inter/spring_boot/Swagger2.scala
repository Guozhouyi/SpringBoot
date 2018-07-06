package com.inter.spring_boot

import org.springframework.context.annotation.{Bean, Configuration}
import springfox.documentation.builders.{ApiInfoBuilder, PathSelectors, RequestHandlerSelectors}
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class Swagger2 {
  @Bean
  def createRestApi: Docket = new Docket(DocumentationType.SWAGGER_2)
    .apiInfo(apiInfo)
    .select
    .apis(RequestHandlerSelectors.basePackage("com.inter.spring_boot.controller"))
    .paths(PathSelectors.any)
    .build

  private def apiInfo = new ApiInfoBuilder()
    .title("RESTful APIs")
    .version("1.0")
    .build

}
