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
import th.co.geniustree.nhso.basicmodel.readonly.ICD10;
import th.co.geniustree.nhso.basicmodel.readonly.ICD10_;

/**
 *
 * @author Thanthathon
 */
public class ICD10Specs {
    
    public static Specification<ICD10> codeLike(final List<String> code) {
        return new Specification<ICD10>() {
            
            @Override
            public Predicate toPredicate(Root<ICD10> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : code) {
                    if (and == null) {
                        and = cb.like(cb.upper(root.get(ICD10_.code)), "%" + keyword.toUpperCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.upper(root.get(ICD10_.code)), "%" + keyword.toUpperCase() + "%"));
                    }
                }
                return and;
            }
        };
    }
    
    public static Specification<ICD10> nameLike(final List<String> name) {
        return new Specification<ICD10>() {
            
            @Override
            public Predicate toPredicate(Root<ICD10> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : name) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(ICD10_.name)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(ICD10_.name)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }
        };
    }
}
