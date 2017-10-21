package cz.fi.muni.carshop.services;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sun.org.apache.regexp.internal.RE;
import cz.fi.muni.carshop.CarShopStorage;
import cz.fi.muni.carshop.entities.Car;
import cz.fi.muni.carshop.enums.CarTypes;
import cz.fi.muni.carshop.exceptions.RequestedCarNotFoundException;

public class CarShopStorageServiceImpl implements CarShopStorageService {

	@Override
	public Optional<Car> isCarAvailable(Color color, CarTypes type) {
		Map<CarTypes, List<Car>> allCars = CarShopStorage.getInstancce().getCars();
		List<Car> carsOfSameType = allCars.get(type);
		return carsOfSameType.stream().filter(car -> car.getColor().equals(color)).findAny();
	}

	@Override
	public List<Car> getCheaperCarsOfSameTypeAndYear(Car referenceCar) {
		Map<CarTypes, List<Car>> allCars = CarShopStorage.getInstancce().getCars();
		List<Car> carsOfSameType = allCars.get(referenceCar.getType());
		return carsOfSameType.stream().filter(car -> referenceCar.getConstructionYear() == car.getConstructionYear()
				&& car.getPrice() < referenceCar.getPrice()).collect(Collectors.toList());
	}

	@Override
	public void sellCar(Car car) throws RequestedCarNotFoundException {
		if (car == null){
			throw new RequestedCarNotFoundException("Given car is null");
		}

		Map<CarTypes, List<Car>> allCars = CarShopStorage.getInstancce().getCars();

		if (!allCars.containsKey(car.getType())){
			throw new RequestedCarNotFoundException("Type of given car was not found");
		}

		List<Car> carsWithSameType = allCars.get(car.getType());

		if (!carsWithSameType.contains(car)){
			throw new RequestedCarNotFoundException("Given Car was not found");
		}

		carsWithSameType.remove(car);
	}

	@Override
	public void addCarToStorage(Car car) {
		if (car.getPrice() < 0){
			throw new IllegalArgumentException("Prize of car is negative");
		}
		CarShopStorage.getInstancce().getCars().computeIfAbsent(car.getType(), x -> new ArrayList<>()).add(car);
	}

}
