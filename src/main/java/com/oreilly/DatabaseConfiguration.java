package com.oreilly;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

  @Autowired
  private Environment env;

  @Bean("database")
  @Profile("dev")
  DataSource devDataSource() {
    HikariConfig ds = new HikariConfig();
    ds.setDriverClassName("org.h2.Driver");
    ds.setJdbcUrl("jdbc:h2:mem:.;MODE=PostgreSQL");
    ds.setUsername("sa");

    return new HikariDataSource(ds);
  }

  @Bean("database")
  @Profile("prod")
  DataSource prodDataSource() {
    HikariConfig postgres = new HikariConfig();
    postgres.setDriverClassName(env.getProperty("DB_DRIVER", "org.postgresql.Driver"));
    postgres.setJdbcUrl(env.getProperty("DB_URI", "jdbc:postgresql://localhost:5432/portal"));
    postgres.setUsername("postgres");
    postgres.setPassword(env.getProperty("DB_PASSWORD", "12345678"));

    return new HikariDataSource(postgres);
  }

  @Bean("database")
  @Profile("test")
  DataSource noTestDataSource() {
    HikariConfig postgres = new HikariConfig();
    postgres.setDriverClassName(env.getProperty("DB_DRIVER", "org.postgresql.Driver"));
    postgres.setJdbcUrl("jdbc:postgresql://localhost:5432/portal_test");
    postgres.setUsername("postgres");
    postgres.setPassword(env.getProperty("DB_PASSWORD", "12345678"));

    return new HikariDataSource(postgres);
  }

  @Bean("entityManagerFactory")
  @Profile("prod")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    System.out.println("creating prod------------------------------------------------" + String.join(",", env.getActiveProfiles()));
    HibernateJpaVendorAdapter hibernateJpa = new HibernateJpaVendorAdapter();
    hibernateJpa.setDatabasePlatform(env.getProperty("hibernate.dialect"));
    //hibernateJpa.setShowSql(env.getProperty("hibernate.show_sql", Boolean.class));

    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setDataSource(prodDataSource());
    emf.setPersistenceUnitName("postgresql");
    emf.setPackagesToScan("com.oreilly.entities");
    emf.setJpaVendorAdapter(hibernateJpa);
    //emf.setJpaPropertyMap(Collections.singletonMap("javax.persistence.validation.mode", "none"));
    return emf;
  }

  @Bean("entityManagerFactory")
  @Profile("test")
  public LocalContainerEntityManagerFactoryBean myTestEntityManagerFactory() {
    System.out.println("creating test------------------------------------------------"+ String.join(",", env.getActiveProfiles()));
    HibernateJpaVendorAdapter hibernateJpa = new HibernateJpaVendorAdapter();
    hibernateJpa.setDatabasePlatform(env.getProperty("hibernate.dialect"));
        //hibernateJpa.setShowSql(env.getProperty("hibernate.show_sql", Boolean.class));

    LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
    emf.setPersistenceUnitName("postgresql-test");
    emf.setDataSource(noTestDataSource());
    emf.setPackagesToScan("com.oreilly.entities");
    emf.setJpaVendorAdapter(hibernateJpa);

    //emf.setJpaPropertyMap(Collections.singletonMap("javax.persistence.validation.mode", "none"));
    return emf;
  }
}
