package dev.elis.service;

import dev.elis.TestData;
import dev.elis.dto.city.CityResponseDTO;
import dev.elis.dto.city.CitySaveDTO;
import dev.elis.dto.city.CityUpdateDTO;
import dev.elis.exception.BadRequestException;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.CityDTOMapperImpl;
import dev.elis.model.City;
import dev.elis.repository.CityRepository;
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
public class CityServiceTest {
    private final CityRepository cityRepository = mock(CityRepository.class);
    private final CityDTOMapperImpl cityDTOMapper = new CityDTOMapperImpl();
    private CityService cityService;

    private City city;
    private CitySaveDTO saveDTO;
    private CityUpdateDTO updateDTO;

    @BeforeEach
    public void setUp() {
        cityService = new CityService(cityRepository, cityDTOMapper);

        TestData testData = new TestData();
        city = testData.getCity();
        updateDTO = testData.getCityDto();
        saveDTO = new CitySaveDTO(city.getName());
    }

    @Test
    public void testGetAlls() {
        cityService.findAll();

        verify(cityRepository).findAll();
    }

    @Test
    public void testSave() {
        when(cityRepository.save(any(City.class))).thenReturn(city);

        cityService.save(saveDTO);

        Assertions.assertEquals(city.getName(), saveDTO.getName());
    }

    @Test
    public void testSave_BadRequest() {
        CitySaveDTO citySaveDTO = new CitySaveDTO();
        citySaveDTO.setName("");

        Assertions.assertThrows(BadRequestException.class, () -> cityService.save(citySaveDTO));
    }

    @Test
    public void testSave_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(cityRepository).save(any());

        try {
            cityService.save(saveDTO);
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }

    @Test
    public void testUpdate() throws NotFoundException {
        Mockito.when(cityRepository.existsById(anyLong())).thenReturn(true);

        cityService.update(updateDTO);

        verify(cityRepository).save(any(City.class));
    }

    @Test
    public void testUpdate_BadRequest() {
        CityUpdateDTO cityUpdateDTO = new CityUpdateDTO(4L, null);
        Mockito.when(cityRepository.existsById(cityUpdateDTO.getId())).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class, () -> cityService.update(cityUpdateDTO));
    }

    @Test
    public void testUpdate_RepositoryError() {
        Mockito.doThrow(new RuntimeException()).when(cityRepository).save(any());
        Mockito.when(cityRepository.existsById(anyLong())).thenReturn(true);

        try {
            cityService.update(updateDTO);
            Assertions.fail();
        } catch (NotFoundException e) {
            Assertions.fail();
        } catch (RuntimeException e) {
            Assertions.assertNull(e.getMessage());
        }
    }


    @Test
    public void testFindById_Success() throws NotFoundException {
        Mockito.when(cityRepository.findById(city.getId())).thenReturn(Optional.of(city));
        CityResponseDTO findCity = cityService.findById(city.getId());

        Assertions.assertEquals(city.getId(), findCity.getId());
        Assertions.assertEquals(city.getName(), findCity.getName());
    }

    @Test
    public void testFindById_NotFound() {
        Mockito.when(cityRepository.findById(city.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> cityService.findById(city.getId()));
    }

    @Test
    public void testDelete_Success() throws NotFoundException {
        Mockito.when(cityRepository.existsById(anyLong())).thenReturn(true);
        boolean result = cityService.delete(city.getId());
        Assertions.assertTrue(result);
    }

    @Test
    public void testDelete_NotFound() {
        Mockito.when(cityRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> cityService.delete(city.getId()));
    }
}
