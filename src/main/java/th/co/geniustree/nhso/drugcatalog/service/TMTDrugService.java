
package th.co.geniustree.nhso.drugcatalog.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.input.DrugAndDosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;

/**
 *
 * @author moth
 */
public interface TMTDrugService {

    public TMTDrug findOneWithoutTx(String tmtid);

    public Page<TMTDrug> findAllAndEagerGroup(Specification<TMTDrug> s, Pageable pgbl);

    public Page<TMTDrug> search(String keyword,Pageable pageable);
    
    public List<TMTDrug> findBySpec(Specification<TMTDrug> s);
    
    public Page<TMTDrug> findAll(Pageable pageable);
    
    public void save(TMTDrug tmtDrug);
    
    public void uploadEditDosageFormGroup(List<DrugAndDosageFormGroup> drugAndDosageFormGroups);
    
}
