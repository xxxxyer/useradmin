package org.example.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "用户接口")
@Data
public class UserVO implements Serializable {

    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private long userID;

    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private String username;

    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private String nickname;

    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private String password;

    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private String role;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public UserVO(){
        this(0, null, null, null, null, null);
    }

    public UserVO(String username){
        this(0, username, null, null, null, null);
    }

    public UserVO(String username, String password){
        this(0, username, null, password, null, null);
    }

    public UserVO(long userID, String username, String nickname, String role) {
        this(userID, username, nickname, null, role, null);
    }

    public UserVO(long userID, String username, String nickname, String role, Date createTime) {
        this(userID, username, nickname, null, role, createTime);
    }

    public UserVO(long userID, String username, String nickname, String password, String role, Date createTime) {
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

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
