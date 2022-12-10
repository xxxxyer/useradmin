package org.example.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@ApiModel(description = "用户信息接口")
@Data
@EqualsAndHashCode(callSuper=true)
public class UserUpdateInfoVO extends UserVO {

    @ApiModelProperty(name = "password", value = "用户密码", required = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 45, message = "密码长度必须是8-45个字符")
    @Pattern(regexp = "^(?=.*[0-9a-z])(?=.*[a-zA-Z])(?=.*[A-Z!@#$%^&*_])(?=.*[!@#$%^&*_0-9])[0-9a-zA-Z!@#$%^&*_]*$",
            message = "密码必须包含数字、字母大小写、特殊字符中至少三种")
    private String password;

    @ApiModelProperty(name = "nickname", value = "用户昵称", required = false)
    @Length(max = 45)
    private String nickname;

    public UserUpdateInfoVO() {}

    public UserUpdateInfoVO(String password, String nickname) {
        super(0, null, nickname, password, null, null);
        this.password = password;
        this.nickname = nickname;
    }
}
