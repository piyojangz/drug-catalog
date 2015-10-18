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
    private UploadHospitalDrugItemService uploadHospitalDrugItemService;

    @Override
    public void initialize(HasFlagABeforeEDU constraintAnnotation) {

    }

    @Override
    public boolean isValid(HospitalDrugExcelModel value, ConstraintValidatorContext context) {
        if (!"A".equals(value.getUpdateFlag())) {
            long countA = uploadHospitalDrugItemService.countTotalHospitalDrugWithFlag(value.getHcode(), value.getHospDrugCode(),value.getTmtId(), "A");
            return countA > 0;
        }
        return true;
    }

}
