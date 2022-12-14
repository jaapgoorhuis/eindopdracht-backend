package com.example.Eindopdracht.service;

import com.example.Eindopdracht.dto.UserDto;
import com.example.Eindopdracht.dto.UserInputDto;
import com.example.Eindopdracht.exceptions.DuplicatedEntryException;
import com.example.Eindopdracht.exceptions.RecordNotFoundException;
import com.example.Eindopdracht.model.Role;
import com.example.Eindopdracht.model.User;
import com.example.Eindopdracht.repository.RoleRepository;
import com.example.Eindopdracht.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepos;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepos;

    public UserService(UserRepository userRepos, PasswordEncoder encoder, RoleRepository roleRepos) {
        this.userRepos = userRepos;
        this.encoder = encoder;
        this.roleRepos = roleRepos;
    }

    public UserDto createUser(UserInputDto newUserDto, Principal principal) {
        if(userRepos.existsByUsername(newUserDto.getUsername())) {
            throw new DuplicatedEntryException("Username already exists");
        }else {
            User user = transferToUser(newUserDto, principal);
            userRepos.save(user);

            return transferToDto(user);
        }
    }

    public List<UserDto> getAllUsers() {
        List<User> userList = userRepos.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            UserDto dto = transferToDto(user);
            userDtoList.add(dto);
        }
        return userDtoList;
    }

    public UserDto getUser(Long id) {
        Optional<User> user = userRepos.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            return transferToDto(u);
        } else {
            throw new RecordNotFoundException("User not found");
        }
    }

    public UserDto updateUser(Long id, UserInputDto dto, Principal principal) {
        if (userRepos.findById(id).isPresent()) {
            User user = userRepos.findById(id).get();

            System.out.println(principal.getName());

            User existingUser = userRepos.findUserByUsername(dto.getUsername());

            if(existingUser != null && !existingUser.getId().equals(user.getId())) {
                throw new DuplicatedEntryException("Username already exists");
            } else {
                if(!principal.getName().equals(user.getUsername())) {
                    dto.setUsername(dto.getUsername());
                } else {
                    dto.setUsername(principal.getName());
                }
                User updatedUser = transferToUser(dto,principal);
                updatedUser.setId(user.getId());
                userRepos.save(updatedUser);

                return transferToDto(updatedUser);
            }
        } else {
            throw new RecordNotFoundException("No user id found");
        }
    }

    public void deleteUser(@RequestBody Long id) {
        if(userRepos.findById(id).isPresent()) {
            userRepos.deleteById(id);
        } else {
          throw new RecordNotFoundException("no user found");
        }
    }

    public UserDto transferToDto(User user) {
        UserDto dto = new UserDto();
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        Collection<Role> userRoles = user.getRoles();
        dto.setRoles(userRoles);

        return dto;
    }

    public User transferToUser(UserInputDto dto, Principal principal) {
        var u = new User();
        u.setFirstname(dto.firstname);
        u.setLastname(dto.lastname);
        u.setUsername(dto.username);
        u.setEmail(dto.email);
        u.setPassword(encoder.encode(dto.password));

        List<Role> userRoles = new ArrayList<>();
        for (Long roles : UserInputDto.roles) {
            Optional<Role> or = roleRepos.findById(roles);
            userRoles.add(or.get());
        }
        u.setRoles(userRoles);

        return u;
    }
}
