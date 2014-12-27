/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.input.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author moth
 */
public class DoubleValidator implements ConstraintValidator<DoubleValue, String> {

    private DoubleValue constraintAnnotation;

    @Override
    public void initialize(DoubleValue constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            if (constraintAnnotation.removeSeperator()) {
                Double.parseDouble(value.replaceAll(",", ""));
            } else {
                Double.parseDouble(value);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
