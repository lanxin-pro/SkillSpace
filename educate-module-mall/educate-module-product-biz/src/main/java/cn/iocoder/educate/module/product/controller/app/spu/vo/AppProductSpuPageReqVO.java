package cn.iocoder.educate.module.product.controller.app.spu.vo;

import cn.iocoder.educate.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author: j-sentinel
 * @Date: 2023/12/13 22:23
 */
@Schema(description = "用户 App - 商品 SPU 分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppProductSpuPageReqVO extends PageParam {

    /**
     * 排序字段常量
     */
    public static final String SORT_FIELD_PRICE = "price";
    public static final String SORT_FIELD_SALES_COUNT = "salesCount";

    /**
     * 推荐字段常量
     */
    public static final String RECOMMEND_TYPE_HOT = "hot";
    public static final String RECOMMEND_TYPE_BENEFIT = "benefit";
    public static final String RECOMMEND_TYPE_BEST = "best";
    public static final String RECOMMEND_TYPE_NEW = "new";
    public static final String RECOMMEND_TYPE_GOOD = "good";

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "关键字", example = "好看")
    private String keyword;

    /**
     * 参见 @link AppProductSpuPageReqVO#SORT_FIELD_XXX 常量
     */
    @Schema(description = "排序字段", example = "price")
    private String sortField;

    @Schema(description = "排序方式", example = "true")
    private Boolean sortAsc;

    /**
     * 参见 @link AppProductSpuPageReqVO#RECOMMEND_TYPE_XXX 常量
     */
    @Schema(description = "推荐类型", example = "hot")
    private String recommendType;

}
