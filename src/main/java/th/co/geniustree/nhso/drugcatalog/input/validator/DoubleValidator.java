/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input.validator;

import java.text.DecimalFormat;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author moth
 */
public class DoubleValidator implements ConstraintValidator<DoubleValue, String> {
    private final DecimalFormat formatter = new DecimalFormat("########.##");

    @Override
    public void initialize(DoubleValue constraintAnnotation) {
        
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        try {
            formatter.parse(value.trim());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
