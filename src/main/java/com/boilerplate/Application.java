package com.boilerplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class Application implements ApplicationListener<ApplicationReadyEvent> {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		System.out.println("=== Ứng dụng đã khởi động thành công ===");
	}

}
//http://localhost:3000/
//http://localhost:8080/swagger-ui/index.html