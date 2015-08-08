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
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup;
import th.co.geniustree.nhso.drugcatalog.model.ReimburseGroup_;

/**
 *
 * @author Thanthathon
 */
public class ReimburseGroupSpecs {
    
    
    public static Specification<ReimburseGroup> idLike(final List<String> keywords) {
        return new Specification<ReimburseGroup>() {

            @Override
            public Predicate toPredicate(Root<ReimburseGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.upper(root.get(ReimburseGroup_.id)), "%" + keyword.toUpperCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.upper(root.get(ReimburseGroup_.id)), "%" + keyword.toUpperCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
    
    public static Specification<ReimburseGroup> descriptionLike(final List<String> keywords) {
        return new Specification<ReimburseGroup>() {

            @Override
            public Predicate toPredicate(Root<ReimburseGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(ReimburseGroup_.description)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(ReimburseGroup_.description)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }
    
    public static Specification<ReimburseGroup> specialProjectEq(final boolean specialProject) {
        return new Specification<ReimburseGroup>() {

            @Override
            public Predicate toPredicate(Root<ReimburseGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(ReimburseGroup_.specialProject),specialProject);
            }

        };
    }
}
