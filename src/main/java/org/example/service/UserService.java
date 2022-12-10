package org.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.example.entity.ListResquestBody;
import org.example.entity.User;
import org.example.jwt.JwtUtil;
import org.example.mapper.UserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {

    @Autowired
    private RedisTemplate redisTemplate;
    private final String ONLINE_TOKEN = "online_token_";

    @Resource
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    public int create(User user) {
        // 无重复用户名
        if (userMapper.select(new User(user.getUsername())) == null) {
            user.setPassword(user.getPassword());
            return userMapper.insert(user);
        }
        else
            return -3;
    }

    public Map<String, Object> login(User user) {
        User _user = loginCheck(user);
        // 密码校验
        if (_user == null)
            return null;
        // 业务逻辑
        else {
            Map<String, Object> dataMap = new HashMap<>();
            String token = JwtUtil.createToken(_user);
            redisTemplate.opsForValue().set(ONLINE_TOKEN+token, _user, 5, TimeUnit.MINUTES);
            dataMap.put("user", _user);
            dataMap.put("token", token);
            return dataMap;
        }
    }

    public void logout(String token) {
        redisTemplate.delete(ONLINE_TOKEN+token);
        return;
    }

    public void deleteById(long userID) {

        userMapper.delete(userID);
    }

    public User updateInfo(User user, User dst) {
        user.setPassword(dst.getPassword());
        user.setNickname(dst.getNickname());
        userMapper.update(user);
        return user;
    }

    public int updateRole(User user, User dst, User record) {
        // 权限校验
        if (!hasPermission(user, dst, record))
            return -2;
        if (Objects.equals(record.getRole(), "null"))
            record.setRole(null);
        return userMapper.update(record);
    }

    public int resetPassword(User user, User dst) {
        // 权限校验
        if (!iSsuperAdmin(user))
            return -2;

        return userMapper.update(dst);
    }

    public User find(User user) {
        return userMapper.select(user);
    }

    public IPage<User> selectPage(ListResquestBody listResquestBody) {
        log.info("page: {}, size: {}", listResquestBody.getPage(), listResquestBody.getSize());
        IPage<User> userPage = new Page<>(listResquestBody.getPage(), listResquestBody.getSize(), false);
        QueryWrapper<User> wrapper = getWrapperFromRequestBody(listResquestBody);

        userMapper.selectPage(userPage, wrapper);
        return userPage;
    }

    public QueryWrapper<User> getWrapperFromRequestBody(ListResquestBody listResquestBody) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtil.isNullOrEmpty(listResquestBody.getUsername())) {
            wrapper.like("username", listResquestBody.getUsername());
        }
        if (!StringUtil.isNullOrEmpty(listResquestBody.getNickname())) {
            wrapper.like("nickname", listResquestBody.getNickname());
        }
        if (!StringUtil.isNullOrEmpty(listResquestBody.getRole())) {
            wrapper.eq("role", listResquestBody.getRole());
        }
        if (listResquestBody.getCreateTime() != null) {
            if (listResquestBody.getCreateTime().getMin() == null) {
                listResquestBody.getCreateTime().setMin(new Date(0));
            }
            if (listResquestBody.getCreateTime().getMax() == null) {
                listResquestBody.getCreateTime().setMax(new Date());
            }

            String strEnd= DateFormatUtils.format(listResquestBody.getCreateTime().getMax(),"yyyy-MM-dd HH:mm:ss");
            String start = DateFormatUtils.format(listResquestBody.getCreateTime().getMin(),"yyyy-MM-dd HH:mm:ss");
            wrapper.apply("UNIX_TIMESTAMP(create_time) >= UNIX_TIMESTAMP('" + start + "')");
            wrapper.apply("UNIX_TIMESTAMP(create_time) < UNIX_TIMESTAMP('" + strEnd + "')");
        }

        return wrapper;
    }

    public boolean checkPassword(User user) {
        if (StringUtil.isNullOrEmpty(user.getUsername()) || StringUtil.isNullOrEmpty(user.getPassword()))
            return false;

        return userMapper.select(new User(user.getUsername(), user.getPassword())) != null;
    }

    public User loginCheck(User user) {
        if (StringUtil.isNullOrEmpty(user.getUsername()) || StringUtil.isNullOrEmpty(user.getPassword()))
            return null;

        return userMapper.select(new User(user.getUsername(), user.getPassword()));
    }

    public boolean iSsuperAdmin(User user) {
        return Objects.equals(user.getUsername(), "admin");
    }

    public boolean hasPermission(User userSrc, User userDst, User record) {
        String roleSrc = userSrc.getRole();
        String roleDst = userDst.getRole();
        // 无role或对目标role无权限
        if (StringUtil.isNullOrEmpty(roleSrc))
            return false;
        if (!(StringUtil.isNullOrEmpty(record.getRole()) || Objects.equals(record.getRole(), "null") || roleService.findSuperiors(record.getRole()).containsSuperior(roleSrc)))
            return false;
        // 有role用户对无role用户 || 高级用户对低级用户
        return StringUtil.isNullOrEmpty(roleDst) || StringUtil.isNullOrEmpty(roleDst)|| Objects.equals(roleDst, "null") || roleService.findSuperiors(roleDst).containsSuperior(roleSrc);
    }
}
