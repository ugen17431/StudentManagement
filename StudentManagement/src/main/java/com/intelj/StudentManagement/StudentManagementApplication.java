package com.intelj.StudentManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@SecurityScheme(name="Student Management API",scheme="basic",type= SecuritySchemeType.HTTP,in= SecuritySchemeIn.HEADER)
@OpenAPIDefinition(info = @Info(title = "Student Management API",description = "an Api for student management",version  = "1.0.0V"))
public class StudentManagementApplication {

	public static void main(String[] args) {

		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper()
	{
       return new ModelMapper();
	}

}
