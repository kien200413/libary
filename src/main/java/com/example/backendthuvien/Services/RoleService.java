package com.example.backendthuvien.Services;

import com.example.backendthuvien.Repositories.RoleRepo;
import com.example.backendthuvien.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService implements iRoleService{
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public List<Role> getAllRole() {
        return roleRepo.findAll();
    }
}
