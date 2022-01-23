package com.globallogic.vehicle.registry.service;

import com.globallogic.vehicle.registry.controller.VehicleSO;
import com.globallogic.vehicle.registry.entities.Vehicle;
import com.globallogic.vehicle.registry.exceptions.RegistryResourceNotFound;
import com.globallogic.vehicle.registry.repository.RegistryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistryService {

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    protected ModelMapper modelMapper;

    public VehicleSO get(String vin) {
        Vehicle found = registryRepository.findByVin(vin);

        if (found == null) {
            throw new RegistryResourceNotFound("Vehicle with given VIN does not exist.");
        }

        return modelMapper.map(found, VehicleSO.class);
    }

    public VehicleSO create(VehicleSO so) {
        Vehicle vehicle = modelMapper.map(so, Vehicle.class);
        return modelMapper.map(registryRepository.save(vehicle), VehicleSO.class);
    }

    public String delete(String vin) {
        Vehicle found = registryRepository.findByVin(vin);

        if (found == null) {
            throw new RegistryResourceNotFound("Vehicle with given VIN does not exist.");
        }

        registryRepository.delete(found);
        return "Vehicle deleted successfully";
    }

    public List<Vehicle> getAll() {
        return registryRepository.findAll();
    }

    public VehicleSO put(String vin, VehicleSO vehicleSO) {
        Vehicle vehicle = registryRepository.findByVin(vin);
        modelMapper.map(vehicleSO, vehicle);
        return modelMapper.map(registryRepository.save(vehicle), VehicleSO.class);
    }
}