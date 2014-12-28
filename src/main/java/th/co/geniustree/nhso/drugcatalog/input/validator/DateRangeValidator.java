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
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DateRangeValidator.class);
    private DateRange constraintAnnotation;

    @Override
    public void initialize(DateRange constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Date parseDateWithOptionalTimeAndNoneLeneint = null;
        if (value == null) {
            return true;
        }
        try {
            if (value instanceof String) {
                if (value.toString().isEmpty()) {
                    return true;
                }
                parseDateWithOptionalTimeAndNoneLeneint = DateUtils.parseDateWithOptionalTimeAndNoneLeneint(value.toString());
            }
            Calendar inputCalendar = Calendar.getInstance(Locale.US);
            int currentyear = inputCalendar.get(Calendar.YEAR);
            inputCalendar.setTime(parseDateWithOptionalTimeAndNoneLeneint);
            if (inputCalendar.get(Calendar.YEAR) < constraintAnnotation.min() || inputCalendar.get(Calendar.YEAR) > currentyear + constraintAnnotation.futureOffset()) {
                return false;
            }
        } catch (Exception e) {
            log.warn(null, e);
            return false;
        }
        return true;
    }

}
