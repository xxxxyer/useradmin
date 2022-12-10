package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Role;
import org.example.service.ResultGenerator;
import org.example.service.RoleService;
import org.springframework.http.HttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static org.example.jwt.JwtFilter.getTokenUserInfo;

@Api(tags = "角色模块")
@Slf4j
@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation(value = "创建角色")
    @ApiImplicitParam(name = "Authorization", value = "用户token", paramType = "header", required = true)
    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public Map<String, Object> create(HttpServletRequest request, @RequestBody @Valid Role role, BindingResult bindingResult) {
        int state = roleService.create(role, getTokenUserInfo(request));
        log.info("create role state: {}", state);
        return ResultGenerator.genSuccessResult("创建成功！");
    }
}
