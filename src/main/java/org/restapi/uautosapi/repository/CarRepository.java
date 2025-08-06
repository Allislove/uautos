package org.restapi.uautosapi.repository;

import org.restapi.uautosapi.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByCustomerId(Long customerId);
}
