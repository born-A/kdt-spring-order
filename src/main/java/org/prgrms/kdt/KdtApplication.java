package org.prgrms.kdt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = {"org.prgrms.kdt.voucher"})
public class KdtApplication {

	private static final Logger logger = LoggerFactory.getLogger(KdtApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(KdtApplication.class, args);
	}

}
