package org.drver.vocabulary.tool.config.jdbc;

import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;

import org.drver.vocabulary.tool.config.ApplicationConfig;

@Configuration
@EnableTransactionManagement
public class JDBCConfig {

  private static final String NAME = ApplicationConfig.getConfig("jdbc.name");
  private static final String URL = ApplicationConfig.getConfig("jdbc.url");
  private static final String USER_NAME = ApplicationConfig.getConfig("jdbc.userName");
  private static final String PASSWORD = ApplicationConfig.getConfig("jdbc.password");

  @Bean(name = "dataSource")
  public DruidDataSource dataSourceBean() throws SQLException {

    DruidDataSource dataSource = new DruidDataSource();

    dataSource.setName(NAME);
    dataSource.setUrl(URL);
    dataSource.setUsername(USER_NAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setDriverClassName(ApplicationConfig.getConfig("com.mysql.jdbc.Driver"));
    dataSource.setFilters("stat");
    dataSource.setMaxActive(Integer.valueOf(ApplicationConfig.getConfig("jdbc.maxActive")));
    dataSource.setInitialSize(1);
    dataSource.setMaxWait(Long.valueOf(ApplicationConfig.getConfig("jdbc.maxWait")));
    dataSource.setMinIdle(Integer.valueOf(ApplicationConfig.getConfig("jdbc.minIdle")));
    dataSource.setTimeBetweenEvictionRunsMillis(60000);
    dataSource.setMinEvictableIdleTimeMillis(300000);
    dataSource.setValidationQuery("select 'x'");
    dataSource.setTestWhileIdle(true);
    dataSource.setTestOnBorrow(false);
    dataSource.setTestOnReturn(false);
    dataSource.setPoolPreparedStatements(true);
    dataSource.setMaxOpenPreparedStatements(20);

    return dataSource;
  }
}
