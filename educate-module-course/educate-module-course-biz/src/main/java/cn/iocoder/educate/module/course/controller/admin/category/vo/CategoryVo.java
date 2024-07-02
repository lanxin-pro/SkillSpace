package cn.iocoder.educate.module.course.controller.admin.category.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author j-sentinel
 * @date 2024/4/13 18:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVo implements Serializable {

    private Long id;

    private Integer status;

    private Integer pageNo = 1;

    private Integer pageSize = 10;

    private String keyword;

    private String batchIds;

    private Integer isDelete = 0;

    private Long categoryId;

}
