package com.hargrove.services;

import com.hargrove.dao.RoleDAO;
import com.hargrove.models.Role;

public class RoleService {
    private RoleDAO dao;

    public Role getRole(String roleName) {
        dao = new RoleDAO();
        Role role = dao.queryRole(roleName);

        return role;
    }
}
