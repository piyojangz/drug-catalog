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
 * @author pramoth
 */
public class ValueSetValidator implements ConstraintValidator<ValueSet, String> {

    private ValueSet valueSet;

    @Override
    public void initialize(ValueSet constraintAnnotation) {
        this.valueSet = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String[] constrainValues = valueSet.values();
        for (String constrainValue : constrainValues) {
            if (constrainValue.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
