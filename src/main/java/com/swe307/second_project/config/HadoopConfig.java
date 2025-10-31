package com.swe307.second_project.config;

import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "hadoop")
public class HadoopConfig {

    @Value("${hadoop.url}")
    private String hadoopUrl;

    @Bean
    public FileSystem hadoopFileSystem() throws IOException {
        org.apache.hadoop.conf.Configuration configuration = new org.apache.hadoop.conf.Configuration();
        configuration.set("fs.defaultFS", hadoopUrl);

        System.out.println("HADOOP IS SETTING AS A PRIMARY STORAGE :)))");
        return FileSystem.get(configuration);
    }

}
