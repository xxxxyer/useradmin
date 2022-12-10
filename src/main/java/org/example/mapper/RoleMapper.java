package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Role;
import org.example.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleMapper {

    int insert(Role role);

    int massiveInsert(String rolename, List<String> superiors, User user);

    List<String> selectByRolename(String rolename);

    List<String> selectBySuperior(String superior);

    int deleteByRole(String role);
}
