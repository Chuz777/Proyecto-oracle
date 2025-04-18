package com.oracle.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

@Configuration
public class DatabaseConnectionTester {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionTester.class);

    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public CommandLineRunner testDatabaseConnection() {
        return args -> {
            logger.info("======= VERIFICANDO CONEXIÓN A ORACLE =======");
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                logger.info("Conexión exitosa a Oracle");
                logger.info("Versión del servidor de base de datos: {}", metaData.getDatabaseProductVersion());
                logger.info("Nombre del servidor de base de datos: {}", metaData.getDatabaseProductName());
                logger.info("Driver JDBC: {} {}", metaData.getDriverName(), metaData.getDriverVersion());
                
                // Verificar también con una consulta
                String result = jdbcTemplate.queryForObject("SELECT 'Consulta exitosa' FROM dual", String.class);
                logger.info("Consulta de prueba: {}", result);
                
                logger.info("=============== CONEXIÓN VERIFICADA ===============");
            } catch (Exception e) {
                logger.error("Error al conectar a Oracle: {}", e.getMessage(), e);
                logger.error("=============== ERROR DE CONEXIÓN ===============");
            }
        };
    }
}
