package org.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.ListResquestBody;
import org.example.entity.User;
import org.example.service.ResultGenerator;
import org.example.service.UserService;
import org.example.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static org.example.jwt.JwtFilter.getTokenUserInfo;

@Api(tags = "用户模块")
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "创建用户")
    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public Map<String, Object> create(@RequestBody @Valid UserCreateVO userCreateVO, BindingResult bindingResult) {
        // 如果有参数校验失败，会将错误信息封装成对象组装在BindingResult里
        if (bindingResult.hasErrors())
            return ResultGenerator.genFailResult(bindingResult);

        User user = new User();
        BeanUtils.copyProperties(userCreateVO, user);
        int state = userService.create(user);
        if (state < 0)
            return ResultGenerator.genFailResult(state);

        return ResultGenerator.genSuccessResult(userService.find(new User(user.getUsername())));
    }

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/signin", produces = "application/json;charset=UTF-8")
    public Map<String, Object> singIn(@RequestBody @Valid UserLoginVO userLoginVO, BindingResult bindingResult) {
        // 如果有参数校验失败，会将错误信息封装成对象组装在BindingResult里
        if (bindingResult.hasErrors())
            return ResultGenerator.genFailResult(bindingResult);

        User user = new User();
        BeanUtils.copyProperties(userLoginVO, user);
        Map<String, Object> dataMap = userService.login(user);
        if (dataMap == null)
            return ResultGenerator.genFailResult(-1);
        else {
            User _user = (User) dataMap.get("user");
            log.info("login: {{id={}, username={}}}", _user.getUserID(), _user.getUsername());
            return ResultGenerator.genSuccessResult(dataMap);
        }
    }

    @ApiOperation(value = "用户退出登陆")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping(value = "/signout", produces = "application/json;charset=UTF-8")
    public Map<String, Object> singOut(HttpServletRequest request) {
        log.info("sign out: {{id={}, username={}}}", request.getAttribute("id"), request.getAttribute("username"));
        userService.logout((String) request.getAttribute("token"));
        return ResultGenerator.genSuccessResult(getTokenUserInfo(request));
    }

    @ApiOperation(value = "用户注销")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping(value = "/delete", produces = "application/json;charset=UTF-8")
    public Map<String, Object> delete(HttpServletRequest request) {
        log.info("delete user: {{id={}, username={}}}", request.getAttribute("id"), request.getAttribute("username"));
        userService.deleteById((long) request.getAttribute("id"));
        return ResultGenerator.genSuccessResult(getTokenUserInfo(request));
    }

    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping(value = "/updateinfo", produces = "application/json;charset=UTF-8")
    public Map<String, Object> updateInfo(HttpServletRequest request, @RequestBody @Valid UserUpdateInfoVO userUpdateInfoVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResultGenerator.genFailResult(bindingResult);

        User userSrc = getTokenUserInfo(request);
        log.info("update info: {{id={}, username={}}}", userSrc.getUserID(), userSrc.getUsername());

        User user = new User();
        BeanUtils.copyProperties(userUpdateInfoVO, user);
        return ResultGenerator.genSuccessResult(userService.updateInfo(userSrc, user));
    }

    @ApiOperation(value = "修改用户角色")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping(value = "/updaterole", produces = "application/json;charset=UTF-8")
    public Map<String, Object> updateRole(HttpServletRequest request, @RequestBody @Valid UserUpdateRoleVO userUpdateRoleVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResultGenerator.genFailResult(bindingResult);

        User user = new User();
        BeanUtils.copyProperties(userUpdateRoleVO, user);

        User userSrc = getTokenUserInfo(request);
        User userDst = userService.find(new User(user.getUsername()));
        if (userDst == null)
            return ResultGenerator.genFailResult(406, "目标用户不存在！");

        log.info("update role: source{{id={}, username={}}}, target{{id={}, username={}}}",
                userSrc.getUserID(), userSrc.getUsername(), userDst.getUserID(), userDst.getUsername());
        User record = userDst.clone();
        record.setRole(user.getRole());
        int state = userService.updateRole(userSrc, userDst, record);
        if (state < 0)
            return ResultGenerator.genFailResult(state);
        return ResultGenerator.genSuccessResult(record);
    }

    @ApiOperation(value = "重置用户密码")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping(value = "/resetPassword", produces = "application/json;charset=UTF-8")
    public Map<String, Object> resetPassword(HttpServletRequest request, @RequestBody @Valid UserLoginVO userLoginVO, BindingResult bindingResult) {
        // 如果有参数校验失败，会将错误信息封装成对象组装在BindingResult里
        if (bindingResult.hasErrors())
            return ResultGenerator.genFailResult(400, bindingResult);

        User user = new User();
        BeanUtils.copyProperties(userLoginVO, user);

        User userSrc = getTokenUserInfo(request);
        User userDst = userService.find(new User(user.getUsername()));
        if (userDst == null)
            return ResultGenerator.genFailResult(406, "目标用户不存在！");

        userDst.setPassword(user.getPassword());
        int state = userService.resetPassword(userSrc, userDst);
        if (state < 0)
            return ResultGenerator.genFailResult(state);

        log.info("reset password: {{id={}, username={}}}", userDst.getUserID(), userDst.getUsername());
        return ResultGenerator.genSuccessResult(userDst);
    }

    @ApiOperation("查询用户详情")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping(value = "/detail", produces = "application/json;charset=UTF-8")
    public Map<String, Object> detail(HttpServletRequest request, @RequestBody UserDetailVO userDetailVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResultGenerator.genFailResult(400, bindingResult);

        User user = new User();
        BeanUtils.copyProperties(userDetailVO, user);

        User _user = userService.find(user);
        if (_user == null)
            log.info("detail request null from user: {{id={}, username={}, role={}}}",
                    request.getAttribute("id"), request.getAttribute("username"), request.getAttribute("role"));
        else
            log.info("detail request {{id={}, username={}, role={}}} from user: {{id={}, username={}, role={}}}",
                    _user.getUserID(), _user.getUsername(), _user.getRole(),
                    request.getAttribute("id"), request.getAttribute("username"), request.getAttribute("role"));
        return ResultGenerator.genSuccessResult(_user);
    }

    @ApiOperation(value = "分页查询用户")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping("/list")
    public Map<String, Object> list(@RequestBody @Valid ListResquestBody listResquestBody, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResultGenerator.genFailResult(400, bindingResult);

        IPage<User> page = userService.selectPage(listResquestBody);

        return ResultGenerator.genSuccessResult(page.getRecords(), page.getCurrent(), page.getSize());
    }

}
