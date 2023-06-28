package org.prgrms.kdt.voucher.repository;

import ch.qos.logback.classic.spi.IThrowableProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);
    private final String SELECT_BY_NAME_SQL = "select * from customers WHERE name = ?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "INSERT INTO customers(customer_id, name, email) VALUES (UUID_TO_BIN(?),?,?)";
    private final String DELETE_ALL_SQL = "DELETE FROM customers";
    private final String UPDATE_BY_ID_SQL = "UPDATE customers SET name = ? WHERE customer_id = UUID_TO_BiN(?)";

    public List<String> findNames(String name) {
        List<String> names = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/week1", "root", "0331");
                var statement = connection.prepareStatement(SELECT_BY_NAME_SQL);
        ) {
            statement.setString(1, name);
            logger.info("statement -> {}", statement);
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var customerName = resultSet.getString("name");
                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                    logger.info("customer id -> {}, createdAt -> {}", customerId, customerName, createdAt);
                    names.add(customerName);
                }
            }
        } catch (SQLException throwable) {
            logger.error("Gor error while closing connection", throwable);
        }
        return names;
        }

    public List<String> findAllNames() {
        List<String> names = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/week1", "root", "0331");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery()
        ) {
                while (resultSet.next()) {
                    var customerName = resultSet.getString("name");
                    var customerId = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                    logger.info("customer id -> {}, createdAt -> {}", customerId, customerName, createdAt);
                    names.add(customerName);
                }
            }
        catch (SQLException throwable) {
            logger.error("Gor error while closing connection", throwable);
        }
        return names;
    }

    public List<UUID> findAllIds() {
        List<UUID> uuids = new ArrayList<>();

        try (
                var connection = DriverManager.getConnection("jdbc:mysql://localhost/week1", "root", "0331");
                var statement = connection.prepareStatement(SELECT_ALL_SQL);
                var resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                var customerName = resultSet.getString("name");
                var customerId = toUUID(resultSet.getBytes("customer_id"));
                var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                logger.info("customer id -> {}, createdAt -> {}", customerId, customerName, createdAt);
                uuids.add(customerId);
            }
        }
        catch (SQLException throwable) {
            logger.error("Gor error while closing connection", throwable);
        }
        return uuids;
    }

    public int insertCustomer(UUID customerId, String name, String email) {
        try (

                var connection = DriverManager.getConnection("jdbc:mysql://localhost/week1", "root", "0331");
                var statement=connection.prepareStatement(INSERT_SQL);
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);
            return statement.executeUpdate();
        }catch(SQLException throwable){
            logger.error("Gor error while closing connection", throwable);
        }
        return 0;
    }
    public int updateCustomerName(UUID customerId, String name) {
        try (

                var connection = DriverManager.getConnection("jdbc:mysql://localhost/week1", "root", "0331");
                var statement=connection.prepareStatement(UPDATE_BY_ID_SQL);
        ) {
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());
            return statement.executeUpdate();
        }catch(SQLException throwable){
            logger.error("Gor error while closing connection", throwable);
        }
        return 0;
    }

    public int deleteAllCustomers() {
        try (

                var connection = DriverManager.getConnection("jdbc:mysql://localhost/week1", "root", "0331");
                var statement=connection.prepareStatement(DELETE_ALL_SQL);
        ) {
            return statement.executeUpdate();
        }catch(SQLException throwable){
            logger.error("Gor error while closing connection", throwable);
        }
        return 0;
    }

    static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static void main(String[] args) {
        var customerRepository = new JdbcCustomerRepository();


        var count = customerRepository.deleteAllCustomers();
        logger.info("deleted count -> {}", count);

        var customerId = UUID.randomUUID();
        logger.info("created customerId -> {}", customerId);
        customerRepository.insertCustomer(customerId, "new-user", "new-user2@gmail.com");
        customerRepository.findAllIds().forEach(v -> logger.info("Found customerId : {}",v));

    }
}
