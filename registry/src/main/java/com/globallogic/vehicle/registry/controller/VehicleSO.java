package com.globallogic.vehicle.registry.controller;

import lombok.Data;

@Data
public class VehicleSO {
    private Integer id;
    private String vin;
    private Integer productionYear;
    private String brand;
    private String model;
}
