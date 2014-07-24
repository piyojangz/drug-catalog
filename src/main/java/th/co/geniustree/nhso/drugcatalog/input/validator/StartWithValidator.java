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
public class StartWithValidator implements ConstraintValidator<StartWith, String> {

    private StartWith valueSet;

    @Override
    public void initialize(StartWith constraintAnnotation) {
        this.valueSet = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        String[] constrainValues = valueSet.values();
        for (String constrainValue : constrainValues) {
            if (value.trim().startsWith(constrainValue.trim())) {
                return true;
            }
        }
        return false;
    }
}
