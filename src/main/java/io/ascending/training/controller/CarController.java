package io.ascending.training.controller;

import io.ascending.training.domain.Car;
import io.ascending.training.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/cars", produces = MediaType.APPLICATION_JSON_VALUE)
public class CarController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CarRepository carRepository;
    //unless = "#result.followers < 12000"
    @Cacheable(value = "cars")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Car getCars(@PathVariable(name="id") Long Id) {
        logger.info("Getting user with ID {}.", Id);
        Car result = carRepository.findById(Id).get();
        return result;
    }


    @Cacheable(value = "cars")
    @RequestMapping(method = RequestMethod.GET)
    public List<Car> getAllCars() {
        logger.info("Getting all cars");
        List<Car> result = carRepository.findAll();
        return result;
    }

    @CachePut(value = "cars", key = "#car.id", unless = "#car.brand == null")
    @RequestMapping(method = RequestMethod.POST)
//    @CacheEvict(value = "cars", allEntries = true)
    public Car generateCar(@RequestBody Car car) {
        return carRepository.save(car);
    }
}
