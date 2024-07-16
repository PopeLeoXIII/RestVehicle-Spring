package dev.elis.service;

import dev.elis.dto.city.*;
import dev.elis.exception.BadRequestException;
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

    public static final String CITY_DOES_NOT_EXIST = "This City does not exist!";
    public static final String EMPTY_CITY_NAME = "City name cannot be empty";
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
        validateName(city);
        city = cityRepository.save(city);
        return cityDTOMapper.toDTO(city);
    }

    @Transactional
    public void update(CityUpdateDTO cityUpdateDTO) throws NotFoundException {
        checkExistById(cityUpdateDTO.getId());
        City city = cityDTOMapper.toEntityUpd(cityUpdateDTO);
        validateName(city);
        cityRepository.save(city);
    }

    @Transactional
    public CityResponseDTO findById(Long id) throws NotFoundException {
        City city = cityRepository.findById(id).orElseThrow(() ->
                new NotFoundException(CITY_DOES_NOT_EXIST));

        return cityDTOMapper.toDTO(city);
    }

    @Transactional
    public List<CityResponseDTO> findAll() {
        List<City> cities = cityRepository.findAll();
        return cities.stream()
                .map(cityDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id) throws NotFoundException {
        checkExistById(id);
        cityRepository.deleteById(id);
        return true;
    }

    private void checkExistById(Long id) throws NotFoundException {
        if (!cityRepository.existsById(id)) {
            throw new NotFoundException(CITY_DOES_NOT_EXIST);
        }
    }

    private static void validateName(City city) {
        if (city.getName() == null || city.getName().isEmpty()) {
            throw new BadRequestException(EMPTY_CITY_NAME);
        }
    }
}