package com.globallogic.vehicle.registry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.vehicle.registry.entities.Vehicle;
import com.globallogic.vehicle.registry.service.RegistryService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistryApplicationTests {

    protected ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private RegistryService registryService;

    private VehicleSO vehicleSO, vehicleSO1;
    private Vehicle vehicleTest1, vehicleTest2, vehicleTest3;

    @Before
    public void setUp() {
        vehicleSO = new VehicleSO();
        vehicleSO.setBrand("VW");
        vehicleSO.setModel("Polo");
        vehicleSO.setVin("vinSO");
        vehicleSO.setProductionYear(2000);

        vehicleSO1 = new VehicleSO();
        vehicleSO1.setBrand("VW");
        vehicleSO1.setModel("Polo");
        vehicleSO1.setVin("updatedVinSO");
        vehicleSO1.setProductionYear(2020);

        vehicleTest1 = new Vehicle();
        vehicleTest1.setBrand("Fiat");
        vehicleTest1.setModel("Punto");
        vehicleTest1.setVin("vintest0");
        vehicleTest1.setProductionYear(2001);

        vehicleTest2 = new Vehicle();
        vehicleTest2.setBrand("Skoda");
        vehicleTest2.setModel("Octavia");
        vehicleTest2.setVin("vintest1");
        vehicleTest2.setProductionYear(2002);

        vehicleTest3 = new Vehicle();
        vehicleTest3.setBrand("Opel");
        vehicleTest3.setModel("Astra");
        vehicleTest3.setVin("vintest2");
        vehicleTest3.setProductionYear(2003);
    }


    @Test
    public void testGet() throws Exception {
        Mockito.when(registryService.get(vehicleSO.getVin())).thenReturn(vehicleSO);

        mockMvc
                .perform(get("/vehicles/" + vehicleSO.getVin()).header("Content-Type", "application/json")) //
                .andDo(print()) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)) //
                .andExpect(jsonPath("$.productionYear", Matchers.is(2000))) //
                .andExpect(jsonPath("$.brand", Matchers.is("VW"))) //
                .andExpect(jsonPath("$.model", Matchers.is("Polo"))) //
                .andExpect(jsonPath("$.vin", Matchers.is("vinSO")));
    }

    @Test
    public void testPost() throws Exception {
        Mockito.when(registryService.create(vehicleSO)).thenReturn(vehicleSO);

        mockMvc
                .perform(post("/vehicles") //
                .content(jsonMapper.writeValueAsString(vehicleSO)) //
                .contentType(MediaType.APPLICATION_JSON)) //
                .andDo(print()) //
                .andExpect(status().isCreated()) //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)) //
                .andExpect(jsonPath("$", Matchers.notNullValue())) //
                .andExpect(jsonPath("$.vin", Matchers.is("vinSO"))) //;
                .andExpect(jsonPath("$.brand", Matchers.is("VW"))) //;
                .andExpect(jsonPath("$.model", Matchers.is("Polo"))) //;
                .andExpect(jsonPath("$.productionYear", Matchers.is(2000)));
    }

    @Test
    public void testDelete() throws Exception {
        Mockito.when(registryService.delete(vehicleSO.getVin())).thenReturn("Vehicle deleted successfully");

        mockMvc
                .perform(delete("/vehicles/" + vehicleSO.getVin()).header("Content-Type", "text/plain;charset=UTF-8")) //
                .andDo(print()) //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")));
    }

    @Test
    public void testGetAll() throws Exception {
        List<Vehicle> records = new ArrayList<>(Arrays.asList(vehicleTest1, vehicleTest2, vehicleTest3));

        Mockito.when(registryService.getAll()).thenReturn(records);

        mockMvc
                .perform(get("/vehicles") //
                .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$", Matchers.hasSize(3))) //
                .andExpect(jsonPath("$[0].brand", Matchers.is("Fiat"))) //
                .andExpect(jsonPath("$[1].model", Matchers.is("Octavia"))) //
                .andExpect(jsonPath("$[2].vin", Matchers.is("vintest2")));
    }

    @Test
    public void testPut() throws Exception {
        Mockito.when(registryService.put(vehicleSO.getVin(), vehicleSO1)).thenReturn(vehicleSO1);

        mockMvc.perform(put("/vehicles/" + vehicleSO.getVin(), vehicleSO1) //
                .content(jsonMapper.writeValueAsString(vehicleSO1)) //
                .contentType(MediaType.APPLICATION_JSON)) //
                .andExpect(status().isOk()) //
                .andExpect(jsonPath("$", Matchers.notNullValue())) //
                .andExpect(jsonPath("$.vin", Matchers.is("updatedVinSO"))) //;
                .andExpect(jsonPath("$.brand", Matchers.is("VW"))) //;
                .andExpect(jsonPath("$.model", Matchers.is("Polo"))) //;
                .andExpect(jsonPath("$.productionYear", Matchers.is(2020)));
    }
}