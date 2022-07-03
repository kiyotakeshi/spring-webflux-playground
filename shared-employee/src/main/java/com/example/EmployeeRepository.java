package com.example;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class EmployeeRepository {
    private static final Map<String, Employee> EMPLOYEE_MAP;

    static {
        EMPLOYEE_MAP = new HashMap<>();
        EMPLOYEE_MAP.put("1", new Employee("1", "employee 1"));
        EMPLOYEE_MAP.put("2", new Employee("2", "employee 2"));
        EMPLOYEE_MAP.put("3", new Employee("3", "employee 3"));
        EMPLOYEE_MAP.put("4", new Employee("4", "employee 4"));
        EMPLOYEE_MAP.put("5", new Employee("5", "employee 5"));
        EMPLOYEE_MAP.put("6", new Employee("6", "employee 6"));
    }

    public Mono<Employee> findEmployeeById(String id) {
        return Mono.just(EMPLOYEE_MAP.get(id));
    }

    public Flux<Employee> findAllEmployees() {
        return Flux.fromIterable(EMPLOYEE_MAP.values());
    }

    public Mono<Employee> updateEmployee(Employee employee) {
        Employee founded = EMPLOYEE_MAP.get(employee.getId());
        if(founded != null){
            founded.setName(employee.getName());
        }
        return Mono.just(founded);
    }
}
