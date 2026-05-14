package com.foodmarket.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/** USER SERVICE - Perfiles y Direcciones | Puerto: 8082 | BD: db_user */
@SpringBootApplication @EnableDiscoveryClient
public class UserServiceApplication { public static void main(String[] a) { SpringApplication.run(UserServiceApplication.class, a); } }
