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
            SimpleDateFormat dateFormat = new SimpleDateFormat(constraintAnnotation.pattern1(), Locale.US);
            dateFormat.setLenient(false);
            dateFormat.parse(value);
            return true;
        } catch (ParseException ex) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(constraintAnnotation.pattern2(), Locale.US);
            try {
                dateFormat.parse(value);
                dateFormat.setLenient(false);
                return true;
            } catch (ParseException parseException) {
                return false;
            }
        }
    }

}
