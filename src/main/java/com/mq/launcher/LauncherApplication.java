package com.mq.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*

This is the SpringBoot launcher application which will run in the embedded
tomcat server and then it checks for the Router.
 */

@SpringBootApplication
public class LauncherApplication {

	public static void main(String[] args) {
		SpringApplication.run(LauncherApplication.class, args);
	}

}
