package org.restapi.uautosapi.controller;

import org.restapi.uautosapi.model.Car;
import org.restapi.uautosapi.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:4200",
        "http://localhost:5173"
})
@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    // Crear un nuevo auto para un cliente específico
    @PostMapping("/{customerId}")
    public ResponseEntity<?> create(@PathVariable Long customerId, @RequestBody Car car) {
        try {
            Car savedCar = carService.createCar(customerId, car);
            return ResponseEntity.ok(savedCar);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // Obtener todos los autos de un cliente
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCars(@PathVariable Long customerId) {
        try {
            List<Car> cars = carService.getCarsByCustomer(customerId);
            return ResponseEntity.ok(cars);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> update(@PathVariable Long id, @RequestBody Car car) {
        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
