package com.example.helpdesk;

import com.example.helpdesk.auth.AuthenticationService;
import com.example.helpdesk.auth.RegisterRequest;
import com.example.helpdesk.department.DepartmentService;
import com.example.helpdesk.user.Role;
import com.example.helpdesk.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.CompletableFuture;

import static com.example.helpdesk.user.Role.ADMIN;

@SpringBootApplication
public class HelpdeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelpdeskApplication.class, args);
    }


//    @Bean
//    public CommandLineRunner commandLineRunner(
//            AuthenticationService service,
//            UserService userService,
//            DepartmentService departmentService
//    ) {
//        return args -> {
//            CompletableFuture.supplyAsync(() -> {
//
//                departmentService.createDepartment("Bucuresti");
//                departmentService.createDepartment("Arges");
//                departmentService.createDepartment("Bihor");
//                departmentService.createDepartment("Cluj");
//                departmentService.createDepartment("Dolj");
//
//                var admin = RegisterRequest.builder()
//                        .firstname("Admin")
//                        .lastname("Admin")
//                        .email("admin@cyberman.ro")
//                        .password("XXU3cQiVouJ99IbnOC7m")
//                        .build();
//                var adminToken = service.register(admin).getToken();
//                System.out.println("Admin token: " + adminToken);
//                userService.updateUserRole("admin@cyberman.ro", Role.ADMIN);
//                return null;
//
//            }).thenAcceptAsync(ignore -> {
//                var manager = RegisterRequest.builder()
//                        .firstname("Manager")
//                        .lastname("Manager")
//                        .email("manager@cyberman.ro")
//                        .password("XXU3cQiVouJ99IbnOC7m")
//                        .build();
//                var managerToken = service.register(manager).getToken();
//                System.out.println("Manager token: " + managerToken);
//                userService.updateUserRole("manager@cyberman.ro", Role.MANAGER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Client")
//                        .lastname("Client")
//                        .email("client@cyberman.ro")
//                        .password("XXU3cQiVouJ99IbnOC7m")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("client@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("John")
//                        .lastname("Snow")
//                        .email("john.snow@cyberman.ro")
//                        .password("XXU3cQiVouJ99IbnOC7m")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("john.snow@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Robert")
//                        .lastname("Bousquet")
//                        .email("robert.bousquet@cyberman.ro")
//                        .password("AP7YnCPWHvBOegszn0BX")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("robert.bousquet@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Gregoria")
//                        .lastname("Howard")
//                        .email("gregoria.howard@cyberman.ro")
//                        .password("AkVc022qIEiGqQjACFvq")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("gregoria.howard@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Joseph")
//                        .lastname("Free")
//                        .email("joseph.free@cyberman.ro")
//                        .password("UHqaR806vu7ZQEpXkmow")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("joseph.free@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Marcos")
//                        .lastname("Mitchell")
//                        .email("marcos.mitchell@cyberman.ro")
//                        .password("0HJSr1AtGjgGRGMJ7Zg1")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("marcos.mitchell@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Ann")
//                        .lastname("Murillo")
//                        .email("ann.murillo@cyberman.ro")
//                        .password("eMJnx6dNjMUh36xNqJTx")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("ann.murillo@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Joseph")
//                        .lastname("Silver")
//                        .email("joseph.silver@cyberman.ro")
//                        .password("92yQJnfQav9caKfkSN0K")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("joseph.silver@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Sandra")
//                        .lastname("Knorr")
//                        .email("sandra.knorr@cyberman.ro")
//                        .password("mPJiACkmcCiR08grpnSz")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("sandra.knorr@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Rose")
//                        .lastname("Samuel")
//                        .email("rose.samuel@cyberman.ro")
//                        .password("hD7cQ2FJtQiyr2j3ZKxj")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("rose.samuel@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Joyce")
//                        .lastname("Hardin")
//                        .email("joyce.hardin@cyberman.ro")
//                        .password("SY2kftbXqEHfpIIUrr24")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("joyce.hardin@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Robert")
//                        .lastname("Roberts")
//                        .email("robert.roberts@cyberman.ro")
//                        .password("xtZ2ro0UkHZc7RMEQxLY")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("robert.roberts@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Angelo")
//                        .lastname("Newton")
//                        .email("angelo.newton@cyberman.ro")
//                        .password("5QPJDQJXoOiXc0ZsvILP")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("angelo.newton@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Samuel")
//                        .lastname("Martin")
//                        .email("samuel.martin@cyberman.ro")
//                        .password("SEx1XHz6fHUm66cwg1GZ")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("samuel.martin@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Michelle")
//                        .lastname("Stout")
//                        .email("michelle.stout@cyberman.ro")
//                        .password("HjQc0N8nkh1ZAjH5L2RF")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("michelle.stout@cyberman.ro", Role.USER);
//
//            }).thenAcceptAsync(ignore -> {
//                var client = RegisterRequest.builder()
//                        .firstname("Adele")
//                        .lastname("Warren")
//                        .email("adele.warren@cyberman.ro")
//                        .password("3O3nX0Gg2tM6ZRiHMb6m")
//                        .build();
//                var clientToken = service.register(client).getToken();
//                System.out.println("Client token: " + clientToken);
//                userService.updateUserRole("adele.warren@cyberman.ro", Role.USER);
//            }).join();
//        };
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowedMethods("GET", "POST", "PUT", "DELETE","ACCEPT", "FETCH");
            }
        };
    }
}
