/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;import java.util.Locale;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;

/**
 *
 * @author moth
 */
public class DateAndOptionalTimeValidator implements ConstraintValidator<DateAndOptionalTime, String> {

    private DateAndOptionalTime constraintAnnotation;

    @Override
    public void initialize(DateAndOptionalTime constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            DateUtils.parseDateWithOptionalTimeAndNoneLeneint(value);
            return true;
        } catch (Exception ex) {
                return false;
        }
    }

}
