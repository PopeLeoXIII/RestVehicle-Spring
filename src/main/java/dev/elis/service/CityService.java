package dev.elis.service;

import dev.elis.dto.city.*;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.CityDTOMapper;
import dev.elis.model.City;
import dev.elis.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityDTOMapper cityDTOMapper;

    @Autowired
    public CityService(CityRepository cityRepository, CityDTOMapper cityDTOMapper) {
        this.cityRepository = cityRepository;
        this.cityDTOMapper = cityDTOMapper;
    }

    @Transactional
    public CityResponseDTO save(CitySaveDTO citySaveDTO) {
        City city = cityDTOMapper.toEntityInc(citySaveDTO);
        city = cityRepository.save(city);
        return cityDTOMapper.toDTO(city);
    }

    @Transactional
    public void update(CityUpdateDTO cityUpdateDTO) throws NotFoundException {
        checkCustomerExist(cityUpdateDTO.getId());
        City city = cityDTOMapper.toEntityUpd(cityUpdateDTO);
        cityRepository.save(city);
    }

    @Transactional
    public CityResponseDTO findById(Long id) throws NotFoundException {
        City city = cityRepository.findById(id).orElseThrow(() ->
                new NotFoundException("This Customer does not exist!"));

        CityResponseDTO dto = cityDTOMapper.toDTO(city);
        return dto;
    }

    public List<CityResponseDTO> findAll() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(cityDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id) throws NotFoundException {
        checkCustomerExist(id);
        cityRepository.deleteById(id);
        return true;
    }

    private void checkCustomerExist(Long id) throws NotFoundException {
        if (!cityRepository.existsById(id)) {
            throw new NotFoundException("This Customer does not exist!");
        }
    }
}