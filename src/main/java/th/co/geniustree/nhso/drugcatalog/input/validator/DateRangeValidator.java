/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;

/**
 *
 * @author moth
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, String> {

    private DateRange constraintAnnotation;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        try {
            Date parseDateWithOptionalTimeAndNoneLeneint = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(value);
            Calendar inputCalendar = Calendar.getInstance(Locale.US);
            int currentyear = inputCalendar.get(Calendar.YEAR);
            inputCalendar.setTime(parseDateWithOptionalTimeAndNoneLeneint);
            if (inputCalendar.get(Calendar.YEAR) < constraintAnnotation.min() || inputCalendar.get(Calendar.YEAR) > currentyear + constraintAnnotation.futureOffset()) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
