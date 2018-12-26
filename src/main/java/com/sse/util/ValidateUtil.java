package com.sse.util;

import com.sse.exception.ParamRTException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author pczhao
 * @email
 * @date 2018-12-06 15:13
 */

public class ValidateUtil {

    /**
     * 使用hibernate注解校验参数
     */
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

    /**
     * 注解校验参数
     *
     * @param obj
     */
    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (constraintViolations.size() > 0) {
            throw new ParamRTException(constraintViolations.iterator().next().getMessage());
        }
    }
}
