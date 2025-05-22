
package com.rra.vehicle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "RRA Vehicle Tracking System API",
        version = "1.0",
        description = "API for Rwanda Revenue Authority Vehicle Tracking Management System",
        contact = @Contact(name = "RRA Development Team", email = "support@rra.gov.rw")
    )
)
public class VehicleTrackingApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehicleTrackingApplication.class, args);
    }
}
