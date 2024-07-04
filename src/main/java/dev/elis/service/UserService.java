package dev.elis.service;

import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserSaveDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.exception.NotFoundException;
import dev.elis.mapper.UserDTOMapper;
import dev.elis.model.User;
import dev.elis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.userDTOMapper = userDTOMapper;
    }

    @Transactional
    public UserResponseDTO save(UserSaveDTO userSaveDTO) {
        User user = userDTOMapper.toEntityInc(userSaveDTO);
        user = userRepository.save(user);
        return userDTOMapper.toDTO(user);
    }

    @Transactional
    public void update(UserUpdateDTO userUpdateDTO) throws NotFoundException {
        checkCustomerExist(userUpdateDTO.getId());
        User user = userDTOMapper.toEntityUpd(userUpdateDTO);
        userRepository.save(user);
    }

    public UserResponseDTO findById(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException("This Customer does not exist!"));
        return userDTOMapper.toDTO(user);
    }

    public List<UserResponseDTO> findAll() {
        List<User> cities = userRepository.findAll();
        return cities.stream()
                .map(userDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id) throws NotFoundException {
        checkCustomerExist(id);
        userRepository.deleteById(id);
        return true;
    }

    private void checkCustomerExist(Long id) throws NotFoundException {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("This Customer does not exist!");
        }
    }
}