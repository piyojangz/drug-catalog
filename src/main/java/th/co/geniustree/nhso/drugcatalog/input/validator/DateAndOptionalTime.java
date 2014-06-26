/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author pramoth
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DateAndOptionalTimeValidator.class)
@Documented
public @interface DateAndOptionalTime {

    String message() default "{th.co.geniustree.nhso.drugcatalog.input.validator.DateAndOptionalTime}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String pattern1() default "dd/MM/yyyy HH:mm";

    String pattern2() default "dd/MM/yyyy";

    boolean noFuture() default false;
}
