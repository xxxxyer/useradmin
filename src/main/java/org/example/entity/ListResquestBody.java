package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

@ApiModel(description = "分页查询用户接口")
@Data
@Accessors(chain = true)
public class ListResquestBody {

    @ApiModelProperty(name = "page", value = "当前页码", required = false, dataType="int")
    @Min(value = 1, message = "当前页码需为正值！")
    private int page;

    @ApiModelProperty(name = "size", value = "每页包含信息条数", required = false, dataType="int")
    @Min(value = 15, message = "每页只能包含15-100条")
    @Max(value = 100, message = "每页只能包含15-100条")
    private int size;

    @ApiModelProperty(name = "username", value = "用户名模糊查找", required = false)
    @Length(max = 45, message = "用户名不能超过45字符")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "用户名不能包含特殊字符，只能是字母和数字")
    private String username;

    @ApiModelProperty(name = "nickname", value = "用户昵称模糊查找", required = false)
    @Length(max = 45, message = "用户昵称不能超过45字符")
    private String nickname;

    @ApiModelProperty(name = "role", value = "用户角色查找", required = false)
    @Length(max = 45, message = "角色名不能超过45字符")
    private String role;

    private CreateTimeClass createTime;

    public ListResquestBody() {

    }

    public int getPage() {
        return page;
    }

    public ListResquestBody setPage(int page) {
        this.page = page;
        return this;
    }

    public int getSize() {
        return size;
    }

    public ListResquestBody setSize(int size) {
        this.size = size;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ListResquestBody setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public ListResquestBody setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getRole() {
        return role;
    }

    public ListResquestBody setRole(String role) {
        this.role = role;
        return this;
    }

    public CreateTimeClass getCreateTime() {
        return createTime;
    }

    public ListResquestBody setCreateTime(CreateTimeClass createTime) {
        this.createTime = createTime;
        return this;
    }

    @ApiModel(description = "用户创建时间")
    public static class CreateTimeClass {

        @ApiModelProperty(value = "最早用户创建时间", required = false)
        @Past
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date min;

        @ApiModelProperty(value = "最晚用户创建时间", required = false)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date max;

        public Date getMin() {
            return min;
        }

        public void setMin(Date min) {
            this.min = min;
        }

        public Date getMax() {
            return max;
        }

        public void setMax(Date max) {
            this.max = max;
        }
    }
}

