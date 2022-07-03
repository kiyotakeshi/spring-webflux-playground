package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = AnnotationBasedApplication.class)
class EmployeeControllerTests {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    void getEmployeeById() {
        Employee employee = new Employee("1", "mike");
        Mono<Employee> employeeMono = Mono.just(employee);

        given(employeeRepository.findEmployeeById("1")).willReturn(employeeMono);

        testClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class)
                // .isEqualTo(employee);
                .value(e -> {
                    assertThat(e.getId()).isEqualTo("1");
                    assertThat(e.getName()).isEqualTo("mike");
                });
    }

    @Test
    void getAllEmployees() {
        List<Employee> employees = Arrays.asList(
                new Employee("1", "employee1"),
                new Employee("2", "employee2"),
                new Employee("3", "employee3")
        );
        Flux<Employee> employeeFlux = Flux.fromIterable(employees);

        given(employeeRepository.findAllEmployees()).willReturn(employeeFlux);

        testClient.get()
                .uri("/employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Employee.class)
                .isEqualTo(employees);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateEmployee() {
        var employee = new Employee("100", "updated employee");

        given(employeeRepository.updateEmployee(employee)).willReturn(Mono.just(employee));

        testClient.post()
                .uri("/employees/update")
                .body(Mono.just(employee), Employee.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Employee.class).isEqualTo(employee);

        verify(employeeRepository).updateEmployee(employee);

    }

    @Test
    @WithMockUser
    void failUpdateEmployee() {
        var employee = new Employee("100", "updated employee");

        testClient.post()
                .uri("/employees/update")
                .body(Mono.just(employee), Employee.class)
                .exchange()
                .expectStatus().isForbidden();

        verifyNoInteractions(employeeRepository);
    }
}