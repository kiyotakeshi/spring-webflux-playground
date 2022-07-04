package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

// import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class EmployeeFunctionalConfig {

    @Bean
    EmployeeRepository employeeRepository() {
        return new EmployeeRepository();
    }

    @Bean
    RouterFunction<ServerResponse> getEmployeeRoute() {
        return route(GET("/employees/{id}"), req
                -> ok().body(employeeRepository().findEmployeeById(req.pathVariable("id")), Employee.class));
    }

    @Bean
    RouterFunction<ServerResponse> getAllEmployeeRoute() {
        return route(GET("/employees"), req
                -> ok().body(employeeRepository().findAllEmployees(), Employee.class));
        // return route(GET("/employees"), getServerResponseHandlerFunction());
    }

//    private HandlerFunction<ServerResponse> getServerResponseHandlerFunction() {
//        return req
//                -> ok().body(employeeRepository().findAllEmployees(), Employee.class);
//    }

    @Bean
    RouterFunction<ServerResponse> updateEmployeeRoute() {
        return route(POST("/employees/update"), req -> req.body(toMono(Employee.class))
                .doOnNext(employeeRepository()::updateEmployee)
                .then(ok().build()));
    }

    @Bean
    RouterFunction<ServerResponse> composedRoutes() {
        return route(GET("/employees"), req -> ok().body(employeeRepository().findAllEmployees(), Employee.class))
                .and(route(GET("/employees/{id}"), req -> ok().body(employeeRepository().findEmployeeById(req.pathVariable("id")), Employee.class)))
                .and(route(POST("/employees/update"), request -> request.body(toMono(Employee.class))
                        .doOnNext(employeeRepository()::updateEmployee)
                        .then(ok().build())));
    }

}
