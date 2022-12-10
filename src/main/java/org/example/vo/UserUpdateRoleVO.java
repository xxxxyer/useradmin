package org.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@ApiModel(description = "用户角色信息接口")
@Data
@EqualsAndHashCode(callSuper=true)
public class UserUpdateRoleVO extends UserVO {
    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "用户名不能为空")
    @Length(max = 45)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*$", message = "用户名不能包含特殊字符，只能是字母和数字，且只能字母开头")
    private String username;

    @ApiModelProperty(value = "角色名称", required = false)
    @Length(max = 45)
    private String role;

    public UserUpdateRoleVO() {

    }

    public UserUpdateRoleVO(String username, String role) {
        super(0, username, null, role, null);
        this.username = username;
        this.role = role;
    }
}
