package com.example.negotino_project.services;

import com.example.negotino_project.common.factories.UserRoleFactory;
import com.example.negotino_project.entities.User;
import com.example.negotino_project.entities.UserRole;
import com.example.negotino_project.model.binding.UserRegisterBindingModel;
import com.example.negotino_project.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final UserRoleFactory userRoleFactory;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder, UserRoleFactory userRoleFactory, UserRoleService userRoleService)
    {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRoleFactory = userRoleFactory;
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User foundUser = this.userRepository
                                            .findByUsername(username)
                                            .orElse(null);

        if(foundUser == null)
        {
            throw new UsernameNotFoundException("User not found.");
        }

        return foundUser;
    }

    @Override
    public boolean createUser(UserRegisterBindingModel userRegisterBindingModel)
    {
        User userEntity = this.modelMapper.map(userRegisterBindingModel, User.class);

        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        Set<UserRole> authorities = new HashSet<>();

        if(this.userRepository.findAll().isEmpty())
        {
            authorities.add(userRoleFactory.createUserRole("ROOT-ADMIN"));
            authorities.add(userRoleFactory.createUserRole("ADMIN"));
            authorities.add(userRoleFactory.createUserRole("MODERATOR"));
            authorities.add(userRoleFactory.createUserRole("ROLE_USER"));
        }
        else
        {
            authorities.add(this.userRoleService.findRole("ROLE_USER"));
        }

        userEntity.setAuthorities(authorities);

        try
        {
            this.userRepository.save(userEntity);
            return true;
        }
        catch (DataIntegrityViolationException e)
        {
//            System.out.println("Username already exist");
            return false;
        }
    }

    @Override
    public Set<User> getAllUsers()
    {
        return this.userRepository.findAll().stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean changeRole(String username, String newRole)
    {
        User user = (User)loadUserByUsername(username);
        Set<UserRole> authorities = new HashSet<>();

        if(newRole.equals("ADMIN"))
        {
            authorities.add(this.userRoleService.findRole("ADMIN"));
            authorities.add(this.userRoleService.findRole("MODERATOR"));
            authorities.add(this.userRoleService.findRole("ROLE_USER"));
        }
        else if(newRole.equals("MODERATOR"))
        {
            authorities.add(this.userRoleService.findRole("MODERATOR"));
            authorities.add(this.userRoleService.findRole("ROLE_USER"));
        }
        else if(newRole.equals("ROLE_USER"))
        {
            authorities.add(this.userRoleService.findRole("ROLE_USER"));
        }
        else
            return false;

        user.setAuthorities(authorities);


        return this.userRepository.save(user) != null;
    }

    @Override
    public void deleteUser(String username)
    {
        User user = (User)loadUserByUsername(username);
        user.setAuthorities(null);
        this.userRepository.delete(user);
    }


}
