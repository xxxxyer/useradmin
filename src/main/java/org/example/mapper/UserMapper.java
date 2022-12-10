package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@Mapper
//public interface UserMapper extends BaseMapper<User> {
//
//}

public interface UserMapper {

    int delete(long id);

    int insert(User user);

    User select(User user);

    IPage<User> selectPage(IPage<User> page, @Param(Constants.WRAPPER) QueryWrapper<User> wrapper);

    List<User> selectByNickname(String nickname);

    int updateByUsernameSelective(User record);

    int update(User user);
}