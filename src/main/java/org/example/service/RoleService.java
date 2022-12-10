package org.example.service;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public int create(Role role, User user) {
        if (StringUtil.isNullOrEmpty(user.getRole()))
            return -2;
        // 无重复Role
        if (CollectionUtils.isEmpty(roleMapper.selectByRolename(role.getRolename()))) {
            List<String> superiors = roleMapper.selectByRolename(user.getRole());
            superiors.add(user.getRole());
            return roleMapper.massiveInsert(role.getRolename(), superiors, user);
        }
        else
            return -4;
    }

    public Role findSuperiors(String rolename) {
        return new Role(rolename, roleMapper.selectByRolename(rolename));
    }
}
