package th.co.geniustree.nhso.drugcatalog.service.impl;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.input.DrugAndDosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.spec.TMTDrugSpecs;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;

/**
 *
 * @author moth
 */
@Service
public class TMTDrugServiceImpl implements TMTDrugService {

    @Autowired
    private TMTDrugRepo tMTDrugRepo;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    @Override
    public TMTDrug findOneWithoutTx(String tmtid) {
        return tMTDrugRepo.findOne(tmtid);
    }

    @Override
    public Page<TMTDrug> findAllAndEagerGroup(Specification<TMTDrug> spec, Pageable pgbl) {
        Page<TMTDrug> findAll = tMTDrugRepo.findAll(spec, pgbl);
//        for (TMTDrug tmtDrug : findAll) {
//            tmtDrug.getDrugGroupItems().size();
//        }
        return findAll;
    }

    @Override
    public List<TMTDrug> findTMTDrugWithFsn(String fsn) {
        return tMTDrugRepo.findByFsnIgnoreCaseContaining(fsn);
    }

    @Override
    public List<TMTDrug> findTMTDrugWithFsn(String fsn, Specifications specs) {
        return tMTDrugRepo.findByFsn(fsn, specs);
    }


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public Page<TMTDrug> search(String keyword, Pageable pageable) {
        List<String> keyList = Arrays.asList(keyword.split("\\s+"));
        Specification<TMTDrug> spec = Specifications.where(TMTDrugSpecs.tmtIdContains(keyList)).or(TMTDrugSpecs.fsnContains(keyList));
        return tMTDrugRepo.findAll(spec,pageable);
    }

    @Override
    public List<TMTDrug> findBySpec(Specification<TMTDrug> s) {
        return tMTDrugRepo.findAll(s);
    }

    @Override
    public List<TMTDrug> searchByFSN(String keyword) {
        List<String> keyList = Arrays.asList(keyword.split("\\s+"));
        return tMTDrugRepo.findAll(Specifications.where(TMTDrugSpecs.fsnContains(keyList)));
    }

    @Override
    public Page<TMTDrug> findAll(Pageable pageable) {
        return tMTDrugRepo.findAll(pageable);
    }

    @Override
    public void save(TMTDrug tmtDrug) {
        tMTDrugRepo.save(tmtDrug);
    }

    @Override
    public void uploadEditDosageFormGroup(List<DrugAndDosageFormGroup> drugAndDosageFormGroups) {
        for(DrugAndDosageFormGroup group : drugAndDosageFormGroups){
            TMTDrug tmtDrug = tMTDrugRepo.findOne(group.getTmtid());
            tmtDrug.setDosageformGroup(group.getDosageFormGroup());
            tMTDrugRepo.save(tmtDrug);
        }
        
    }

    

}
