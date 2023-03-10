package com.highload.userservice.service;

import com.highload.feign.dto.UserDto;
import com.highload.feign.dto.RoleDto;
import com.highload.feign.exceptions.NoSuchEntityException;
import com.highload.feign.exceptions.PermissionDeniedException;
import com.highload.feign.model.User;
import com.highload.userservice.exceptions.UserExistException;
import com.highload.userservice.jdbc.UserDao;
import com.highload.userservice.jdbc.UserDaoImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserDao userDao = new UserDaoImpl();
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public List<UserDto> getAllUsers(Integer pageSize, Integer pageNum) {
        return userDao
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    public UserDto getUser(UUID userId) {
        return modelMapper.map(getUserById(userId), UserDto.class);
    }

    @SneakyThrows
    public UserDto getUserByUsername(String username) {
        return modelMapper.map(userDao
                .findByUsername(username)
                .orElseThrow(() -> new NoSuchEntityException("User with username " + username + " not found")), UserDto.class);
    }

    @SneakyThrows
    public User getUserById(UUID userId) {
        return userDao
                .findUserByUserId(userId)
                .orElseThrow(() -> new NoSuchEntityException("User with id " + userId + " not found"));
    }

    @SneakyThrows
    public Optional<UserDto> signUpUser(UserDto userDto) {
        Optional<User> userDb = userDao.findByUsername(userDto.getUsername());
        if (userDb.isPresent()) {
            throw new UserExistException("User already exists");
        }
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return Optional.of(modelMapper
                .map(userDao
                        .insertUser(modelMapper
                                    .map(userDto, User.class)), UserDto.class));
    }

    @SneakyThrows
    public UserDto updateUser(UserDto userDto, UUID userId, UserDetails userAuth) {
        getUserById(userId);
        checkAuthorities(userDto, userAuth);
        return modelMapper.map(userDao.insertUser(modelMapper.map(userDto, User.class)), UserDto.class);
    }

    @Transactional
    @SneakyThrows
    public UserDto createUser(UserDto userDto, UserDetails userAuth) {
        checkAuthorities(userDto, userAuth);
        return modelMapper.map(userDao.insertUser(modelMapper.map(userDto, User.class)), UserDto.class);
    }

    private void checkAuthorities(UserDto userDto, UserDetails userAuth) throws PermissionDeniedException {
        if (userAuth.getAuthorities()
                .stream()
                .noneMatch(grantedAuthority -> List
                        .of("SUPER_ADMIN", "ADMIN")
                        .contains(grantedAuthority.getAuthority()))
                || !userAuth
                .getUsername()
                .equals(userDto.getUsername())
                || userAuth
                .getAuthorities()
                .stream()
                .noneMatch(grantedAuthority -> userDto
                        .getRoles()
                        .stream()
                        .map(RoleDto::getRolename)
                        .toList()
                        .contains(grantedAuthority.getAuthority()))) {
            throw new PermissionDeniedException("You haven't rights for updating this user");
        }
    }

    @Transactional
    public void deleteUser(UUID userId) {
        userDao.deleteUserByUserId(userId);
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) {
        return userDao
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username = " + username + " not found"));
    }
}
