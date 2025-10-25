package com.swe307.second_project;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
@RequiredArgsConstructor
public class SecondProjectApplication{

	public static void main(String[] args) {
		SpringApplication.run(SecondProjectApplication.class, args);
	}

}
