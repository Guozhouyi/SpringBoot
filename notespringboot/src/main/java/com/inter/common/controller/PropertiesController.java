package com.inter.common.controller;

import com.inter.common.properties.Properties;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertiesController {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
    @Autowired
    private Properties properties;

    @ApiOperation("properties 测试")
    @RequestMapping(value = "/properties", method = RequestMethod.GET)
    public String properties() {
        logger.info(properties.toString());
        return properties.toString();
    }
}
