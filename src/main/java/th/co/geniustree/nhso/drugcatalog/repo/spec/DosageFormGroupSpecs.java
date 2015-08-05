/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo.spec;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup_;

/**
 *
 * @author Thanthathon
 */
public class DosageFormGroupSpecs {

    public static Specification<DosageFormGroup> idGroupLike(final List<String> keywords) {
        return new Specification<DosageFormGroup>() {

            @Override
            public Predicate toPredicate(Root<DosageFormGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(DosageFormGroup_.idGroup)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(DosageFormGroup_.idGroup)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
    
    public static Specification<DosageFormGroup> descriptionLike(final List<String> keywords) {
        return new Specification<DosageFormGroup>() {

            @Override
            public Predicate toPredicate(Root<DosageFormGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(DosageFormGroup_.description)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(DosageFormGroup_.description)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
}
