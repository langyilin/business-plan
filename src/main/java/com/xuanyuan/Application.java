package com.xuanyuan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration // 相当于在xml配置beans标签元素
@SpringBootApplication // 表示开启spring注解
@ServletComponentScan // 使用后，Servlet、Filter、Listener 可以直接通过
						// @WebServlet、@WebFilter、@WebListener 注解自动注册
@EnableScheduling // 表示允许执行定时器
@EnableAsync // 表示允许异步执行，即开启多线程
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	/**
	 * Linux启动命令:nohup java -jar
	 * /home/web/com.xuanyuan.test.licenseweb-3.2.1.1.war
	 * --spring.profiles.active=test > /opt/licenseweb.logs 2>&1 &
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setAdditionalProfiles();
		app.run(args);
	}

}
