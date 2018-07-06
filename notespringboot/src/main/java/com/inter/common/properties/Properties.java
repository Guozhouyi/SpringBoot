package com.inter.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties
public class Properties {
    private String environment;
    private String host;
    private String port;
    private String hdfsNM1Host;
    private String hdfsNM2Host;

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setHdfsNM1Host(String hdfsNM1Host) {
        this.hdfsNM1Host = hdfsNM1Host;
    }

    public void setHdfsNM2Host(String hdfsNM2Host) {
        this.hdfsNM2Host = hdfsNM2Host;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getHdfsNM1Host() {
        return hdfsNM1Host;
    }

    public String getHdfsNM2Host() {
        return hdfsNM2Host;
    }

    @Override
    public String toString() {
        return "Properties =>" + environment + ":" + host + ":" + port + ":" + hdfsNM1Host + "," + hdfsNM2Host;
    }
}
