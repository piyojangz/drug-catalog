/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug_;

/**
 *
 * @author moth
 */
public class HospitalDrugSpecs {

    public static Specification<HospitalDrug> hcodeEq(final String hcode) {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(HospitalDrug_.hcode), hcode);
            }

        };
    }
}
