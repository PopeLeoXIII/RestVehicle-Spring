package dev.elis.service;

import dev.elis.dto.user.UserResponseDTO;
import dev.elis.dto.user.UserSaveDTO;
import dev.elis.dto.user.UserUpdateDTO;
import dev.elis.exception.BadRequestException;
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
    public static final String NOT_FOUND_MSG = "This User does not exist!";
    public static final String EMPTY_USER_NAME = "User name cannot be empty";
    public static final String EMPTY_USER_SURNAME = "User surname cannot be empty";

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
        validateNameAndSurname(user);
        user = userRepository.save(user);
        return userDTOMapper.toDTO(user);
    }

    @Transactional
    public void update(UserUpdateDTO userUpdateDTO) throws NotFoundException {
        checkExist(userUpdateDTO.getId());
        User user = userDTOMapper.toEntityUpd(userUpdateDTO);
        validateNameAndSurname(user);
        userRepository.save(user);
    }

    @Transactional
    public UserResponseDTO findById(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() ->
                new NotFoundException(NOT_FOUND_MSG));
        return userDTOMapper.toDTO(user);
    }

    @Transactional
    public List<UserResponseDTO> findAll() {
        List<User> cities = userRepository.findAll();
        return cities.stream()
                .map(userDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id) throws NotFoundException {
        checkExist(id);
        userRepository.deleteById(id);
        return true;
    }

    private void checkExist(Long id) throws NotFoundException {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(NOT_FOUND_MSG);
        }
    }

    private static void validateNameAndSurname(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new BadRequestException(EMPTY_USER_NAME);
        }

        if (user.getSurname() == null || user.getSurname().isEmpty()) {
            throw new BadRequestException(EMPTY_USER_SURNAME);
        }
    }
}