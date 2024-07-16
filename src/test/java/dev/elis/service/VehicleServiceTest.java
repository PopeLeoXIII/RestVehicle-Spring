package dev.elis.service;

import dev.elis.TestData;
import dev.elis.dto.city.CityUpdateDTO;
import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleSaveDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.BadRequestException;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.VehicleDTOMapperImpl;
import dev.elis.model.Vehicle;
import dev.elis.repository.VehicleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {
    private final VehicleRepository vehicleRepository = mock(VehicleRepository.class);
    private final VehicleDTOMapperImpl vehicleDTOMapper = new VehicleDTOMapperImpl();
    private VehicleService vehicleService;

    private Vehicle vehicle;
    private VehicleSaveDTO saveDTO;
    private VehicleUpdateDTO updateDTO;
    private CityUpdateDTO cityUpdateDTO;

    @BeforeEach
    public void setUp() {
        vehicleService = new VehicleService(vehicleRepository, vehicleDTOMapper);

        TestData testData = new TestData();
        cityUpdateDTO = testData.getCityDto();
        vehicle = testData.getVehicle();
        updateDTO = testData.getVehicleDto();
        saveDTO = new VehicleSaveDTO(vehicle.getName(), cityUpdateDTO);
    }

    @Test
    public void testGetAlls() {
        vehicleService.findAll();

        verify(vehicleRepository).findAll();
    }

    @Test
    public void testSave() {
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        vehicleService.save(saveDTO);

        Assertions.assertEquals(vehicle.getName(), saveDTO.getName());
    }

    @Test
    public void testSave_BadRequest_Name() {
        VehicleSaveDTO vehicleSaveDTO = new VehicleSaveDTO();
        vehicleSaveDTO.setName("");

        Assertions.assertThrows(BadRequestException.class, () -> vehicleService.save(vehicleSaveDTO));
    }

    @Test
    public void testSave_BadRequest_City() {
        VehicleSaveDTO vehicleSaveDTO = new VehicleSaveDTO();
        vehicleSaveDTO.setCity(null);

        Assertions.assertThrows(BadRequestException.class, () -> vehicleService.save(vehicleSaveDTO));
    }

    @Test
    public void testSave_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(vehicleRepository).save(any());

        try {
            vehicleService.save(saveDTO);
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }

    @Test
    public void testUpdate() throws NotFoundException {
        Mockito.when(vehicleRepository.existsById(anyLong())).thenReturn(true);

        vehicleService.update(updateDTO);

        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    public void testUpdate_BadRequest_Name() {
        VehicleUpdateDTO vehicleUpdateDTO = new VehicleUpdateDTO(4L, null, cityUpdateDTO);
        Mockito.when(vehicleRepository.existsById(vehicleUpdateDTO.getId())).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class, () -> vehicleService.update(vehicleUpdateDTO));
    }

    @Test
    public void testUpdate_BadRequest_Surname() {
        VehicleUpdateDTO vehicleUpdateDTO = new VehicleUpdateDTO(4L, "null", null);
        Mockito.when(vehicleRepository.existsById(vehicleUpdateDTO.getId())).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class, () -> vehicleService.update(vehicleUpdateDTO));
    }

    @Test
    public void testUpdate_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(vehicleRepository).save(any());
        Mockito.when(vehicleRepository.existsById(anyLong())).thenReturn(true);

        try {
            vehicleService.update(updateDTO);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }


    @Test
    public void testFindById_Success() throws NotFoundException {
        Mockito.when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.of(vehicle));
        VehicleResponseDTO findVehicle = vehicleService.findById(vehicle.getId());

        Assertions.assertEquals(vehicle.getId(), findVehicle.getId());
        Assertions.assertEquals(vehicle.getName(), findVehicle.getName());
    }

    @Test
    public void testFindById_NotFound() {
        Mockito.when(vehicleRepository.findById(vehicle.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> vehicleService.findById(vehicle.getId()));
    }

    @Test
    public void testDelete_Success() throws NotFoundException {
        Mockito.when(vehicleRepository.existsById(anyLong())).thenReturn(true);
        boolean result = vehicleService.delete(vehicle.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void testDelete_NotFound() {
        Mockito.when(vehicleRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> vehicleService.delete(vehicle.getId()));
    }
}
