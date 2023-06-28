package org.prgrms.kdt;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgrms.kdt.voucher.domain.Customer;
import org.prgrms.kdt.voucher.repository.CustomerJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@SpringJUnitConfig
public class CustomerJdbcRepositoryTest {

    @Configuration
    @ComponentScan(
            basePackages = {"org.prgrms.kdt.voucher"}
    )
    static class Config{

        @Bean
        public DataSource dataSource() {
            return DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost/week1")
                    .username("root")
                    .password("0331")
                    .type(HikariDataSource.class)
                    .build();
        }
    }

    @Autowired
    CustomerJdbcRepository customerJdbcRepository;

    @Autowired
    DataSource dataSource;

    @Test
    public void testHikariConnectionPool() {
        assertThat(dataSource.getClass().getName(), is("com.zaxxer.hikari.HikariDataSource"));
    }

    @Test
    @DisplayName("전체 고객을 조회할수있다")
    public void testFindAll() throws InterruptedException {
        var customers = customerJdbcRepository.findAll();
        assertThat(customers.isEmpty(), is(false));
        Thread.sleep(5000);
    }

    @Test
    @DisplayName("이름으로 고객을 조회할수있다")
    public void testFindByName() throws InterruptedException {
        var customer = customerJdbcRepository.findByName("created-user");
        assertThat(customer.isEmpty(), is(false));
        Thread.sleep(5000);
    }

    @Test
    @DisplayName("고객을 추가할 수 있다.")
    public void testInsert() throws InterruptedException {
        var newCustomer = new Customer(UUID.randomUUID(),"test-user11","test-user11@gmail.com", LocalDateTime.now());
        customerJdbcRepository.insert(newCustomer);
        var retrievedCustomer = customerJdbcRepository.findById(newCustomer.getCustomerId());
        assertThat(retrievedCustomer.isEmpty(), is(false));
        assertThat(retrievedCustomer.get(), samePropertyValuesAs(newCustomer));
        Thread.sleep(5000);
    }
}
