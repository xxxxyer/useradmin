package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.DigestUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Data
@ApiModel
@Accessors(chain = true)
public class User implements Serializable, Cloneable {

    private long userID;

    @ApiModelProperty(value = "用户名",required = true)
    @NotNull(message = "用户名不能为空")
    @Length(max = 45)
    @Pattern(regexp = "^[a-zA-Z]+[a-zA-Z0-9]*$", message = "用户名不能包含特殊字符，只能是字母和数字，且只能字母开头")
    private String username;
    @Length(max = 45)
    private String nickname;

    @ApiModelProperty(name = "password", value = "用户密码", required = true)
    @NotNull(message = "用户密码不能为空")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 45, message = "密码长度必须是8-45个字符")
    @Pattern(regexp = "^(?=.*[0-9a-z])(?=.*[a-zA-Z])(?=.*[A-Z!@#$%^&*_])(?=.*[!@#$%^&*_0-9])[0-9a-zA-Z!@#$%^&*_]*$",
            message = "密码必须包含数字、字母大小写、特殊字符中至少三种")
    private String password;

    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public User(){
        this(0, null, null, null, null, null);
    }

    public User(String username){
        this(0, username, null, null, null, null);
    }

    public User(String username, String password){
        this(0, username, null, password, null, null);
    }

    public User(long userID, String username, String nickname, String role) {
        this(userID, username, nickname, null, role, null);
    }

    public User(long userID, String username, String nickname, String role, Date createTime) {
        this(userID, username, nickname, null, role, createTime);
    }


    public User(long userID, String username, String nickname, String password, String role, Date createTime) {
        this.userID = userID;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.createTime = createTime;
    }



    public long getUserID() {
        return userID;
    }

    public User setUserID(long userID) {
        this.userID = userID;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getRole() {
        return role;
    }

    public User setRole(String role) {
        this.role = role;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public User setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public User clone() {
        try {
            User clone = (User) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
