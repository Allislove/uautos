package org.restapi.uautosapi.service;

import org.restapi.uautosapi.model.Car;
import org.restapi.uautosapi.model.Customer;
import org.restapi.uautosapi.repository.CarRepository;
import org.restapi.uautosapi.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Car createCar(Long customerId, Car car) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("El cliente especificado no existe."));
        car.setCustomer(customer);
        return carRepository.save(car);
    }

    public List<Car> getCarsByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new IllegalArgumentException("El cliente especificado no existe.");
        }
        return carRepository.findByCustomerId(customerId);
    }

    public Car updateCar(Long carId, Car updatedCar) {
        Car existingCar = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Auto no encontrado"));

        // Actualizamos los campos del auto
        existingCar.setMarca(updatedCar.getMarca());
        existingCar.setModelo(updatedCar.getModelo());
        existingCar.setAnio(updatedCar.getAnio());
        existingCar.setNumeroDePlaca(updatedCar.getNumeroDePlaca());
        existingCar.setColor(updatedCar.getColor());

        // Si se proporciona un nuevo cliente, lo asociamos
        if (updatedCar.getCustomer() != null && updatedCar.getCustomer().getId() != null) {
            Customer newCustomer = customerRepository.findById(updatedCar.getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            existingCar.setCustomer(newCustomer);
        }

        return carRepository.save(existingCar);
    }


    public void deleteCar(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new IllegalArgumentException("Auto no encontrado");
        }
        carRepository.deleteById(carId);
    }
}
