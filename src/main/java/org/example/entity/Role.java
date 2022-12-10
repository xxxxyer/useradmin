package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Accessors(chain = true)
public class Role {

    @ApiModelProperty(value = "用角色名称", required = true)
    @NotNull(message = "角色名称不能为空")
    @Length(max = 45)
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$", message = "角色名称只能是汉字、字母、数字和下划线")
    private String rolename;

    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private String superior;

    @ApiModelProperty(hidden=true)
    @JsonIgnore
    private List<String> superiors;

    public Role() {
        this.rolename = null;
        this.superior = null;
        this.superiors = null;
    }

    public Role(String rolename) {
        this.rolename = rolename;
        this.superior = null;
        this.superiors = null;
    }

    public Role(String rolename, String superior) {
        this.rolename = rolename;
        this.superior = superior;
        this.superiors = null;
    }

    public Role(String rolename, List<String> superiors) {
        this.rolename = rolename;
        this.superior = null;
        this.superiors = superiors;
    }

    public Boolean containsSuperior (String superior) {
        return superiors.contains(superior);
    }


    public String getRolename() {
        return rolename;
    }

    public Role setRolename(String rolename) {
        this.rolename = rolename;
        return this;
    }

    public String getSuperior() {
        return superior;
    }

    public Role setSuperior(String superior) {
        this.superior = superior;
        return this;
    }
    public List<String> getSuperiors() {
        return superiors;
    }

    public Role setSuperiors(List<String> superiors) {
        this.superiors = superiors;
        return this;
    }
}
