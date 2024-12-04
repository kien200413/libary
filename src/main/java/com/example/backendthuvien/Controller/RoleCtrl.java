package com.example.backendthuvien.Controller;

import com.example.backendthuvien.Repositories.RoleRepo;
import com.example.backendthuvien.Services.RoleService;
import com.example.backendthuvien.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleCtrl {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepo roleRepo;
    @GetMapping("")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(roleRepo.findAll());
    }
}
