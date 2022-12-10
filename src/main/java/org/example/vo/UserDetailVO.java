package org.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "用户名接口")
@Data
@EqualsAndHashCode(callSuper=true)
public class UserDetailVO extends UserVO {
    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "用户名不能为空")
    @Length(max = 45)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*$", message = "用户名不能包含特殊字符，只能是字母和数字，且只能字母开头")
    private String username;

    public UserDetailVO() {}

    public UserDetailVO(String username) {
        super(username, null);
        this.username = username;
    }
}
