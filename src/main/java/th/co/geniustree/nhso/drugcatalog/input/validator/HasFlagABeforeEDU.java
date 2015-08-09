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
@Target({ElementType.TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = HasFlagABeforeEDUValidator.class)
@Documented
public @interface HasFlagABeforeEDU {

    String message() default "{th.co.geniustree.nhso.drugcatalog.input.validator.HasFlagABeforeEDU}";

    Class<?>[] groups() default {};
    boolean removeSeperator() default false;

    Class<? extends Payload>[] payload() default {};
}
