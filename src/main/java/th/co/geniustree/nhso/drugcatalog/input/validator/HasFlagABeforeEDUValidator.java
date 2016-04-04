package th.co.geniustree.nhso.drugcatalog.input.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugItemService;

/**
 *
 * @author pramoth
 */
public class HasFlagABeforeEDUValidator implements ConstraintValidator<HasFlagABeforeEDU, HospitalDrugExcelModel> {

    @Autowired
    private UploadHospitalDrugItemService service;

    @Override
    public void initialize(HasFlagABeforeEDU constraintAnnotation) {

    }

    @Override
    public boolean isValid(HospitalDrugExcelModel value, ConstraintValidatorContext context) {
        boolean valid = false;
        if (value.getUpdateFlag().equals("E") || 
                value.getUpdateFlag().equals("U") || 
                value.getUpdateFlag().equals("D")) {
            valid = service.hasHospitalDrugFlagABefore(value.getHcode(), value.getHospDrugCode());
        }
        return valid;
    }

}
