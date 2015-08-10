package th.co.geniustree.nhso.drugcatalog.input.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugItemRepo;

/**
 *
 * @author pramoth
 */
public class HasFlagABeforeEDUValidator implements ConstraintValidator<HasFlagABeforeEDU, HospitalDrugExcelModel> {

    @Autowired
    private UploadHospitalDrugItemRepo uploadHospitalDrugItemRepo;

    @Override
    public void initialize(HasFlagABeforeEDU constraintAnnotation) {

    }

    @Override
    public boolean isValid(HospitalDrugExcelModel value, ConstraintValidatorContext context) {
        if (!"A".equals(value.getUpdateFlag())) {
            long countA = uploadHospitalDrugItemRepo.countByHospDrugCodeAndUploadDrugHcodeAndRequestAndAcceptAndUpdateFlag(value.getHospDrugCode(), value.getHcode(), "A");
            return countA > 0;
        }
        return true;
    }

}
