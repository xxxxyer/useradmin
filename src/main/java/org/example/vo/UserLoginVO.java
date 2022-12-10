package org.example.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "用户信息接口")
@Data
@EqualsAndHashCode(callSuper=true)
public class UserLoginVO extends UserVO {

    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "用户名不能为空")
    @Length(max = 45)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*$", message = "用户名不能包含特殊字符，只能是字母和数字，且只能字母开头")
    private String username;

    @ApiModelProperty(name = "password", value = "用户密码", required = true)
    @NotNull(message = "用户密码不能为空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 45, message = "密码长度必须是8-45个字符")
    @Pattern(regexp = "^(?=.*[0-9a-z])(?=.*[a-zA-Z])(?=.*[A-Z!@#$%^&*_])(?=.*[!@#$%^&*_0-9])[0-9a-zA-Z!@#$%^&*_]*$",
            message = "密码必须包含数字、字母大小写、特殊字符中至少三种")
    private String password;

    public UserLoginVO(){
        this(null, null);
    }

    public UserLoginVO(String username, String password){
        super(username, password);
        this.username = username;
        this.password = password;
    }

}
