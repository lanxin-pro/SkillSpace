package cn.iocoder.educate.framework.common.validation;

import cn.iocoder.educate.framework.common.core.IntArrayValuable;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author: 董伟豪
 * @Date: 2023/5/8 18:22
 */
@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = InEnumValidator.class
)
public @interface InEnum {

    /**
     * @return 实现 EnumValuable 接口的
     */
    Class<? extends IntArrayValuable> value();

    String message() default "必须在指定范围 {value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
