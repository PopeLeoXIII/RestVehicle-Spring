package dev.elis.service;

import dev.elis.dto.vehicle.VehicleResponseDTO;
import dev.elis.dto.vehicle.VehicleSaveDTO;
import dev.elis.dto.vehicle.VehicleUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.VehicleDTOMapper;
import dev.elis.model.Vehicle;
import dev.elis.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleDTOMapper vehicleDTOMapper;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository, VehicleDTOMapper vehicleDTOMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleDTOMapper = vehicleDTOMapper;
    }

    @Transactional
    public VehicleResponseDTO save(VehicleSaveDTO vehicleSaveDTO) {
        Vehicle vehicle = vehicleDTOMapper.toEntityInc(vehicleSaveDTO);
        vehicle = vehicleRepository.save(vehicle);
        return vehicleDTOMapper.toDTO(vehicle);
    }

    @Transactional
    public void update(VehicleUpdateDTO vehicleUpdateDTO) throws NotFoundException {
        checkCustomerExist(vehicleUpdateDTO.getId());
        Vehicle vehicle = vehicleDTOMapper.toEntityUpd(vehicleUpdateDTO);
        vehicleRepository.save(vehicle);
    }

    public VehicleResponseDTO findById(Long id) throws NotFoundException {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() ->
                new NotFoundException("This Customer does not exist!"));
        return vehicleDTOMapper.toDTO(vehicle);
    }

    public List<VehicleResponseDTO> findAll() {
        List<Vehicle> cities = vehicleRepository.findAll();
        return cities.stream()
                .map(vehicleDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id) throws NotFoundException {
        checkCustomerExist(id);
        vehicleRepository.deleteById(id);
        return true;
    }

    private void checkCustomerExist(Long id) throws NotFoundException {
        if (!vehicleRepository.existsById(id)) {
            throw new NotFoundException("This Customer does not exist!");
        }
    }
}