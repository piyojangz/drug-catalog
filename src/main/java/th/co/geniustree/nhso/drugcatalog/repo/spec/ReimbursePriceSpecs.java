/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.repo.spec;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePrice_;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug_;

/**
 *
 * @author Thanthathon
 */
public class ReimbursePriceSpecs {

    public static Specification<ReimbursePrice> tmtLike(final List<String> tmtid) {
        return new Specification<ReimbursePrice>() {
            @Override
            public Predicate toPredicate(Root<ReimbursePrice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : tmtid) {
                    if (or == null) {
                        or = cb.like(root.get(ReimbursePrice_.tmtDrug).get(TMTDrug_.tmtId), "%" + keyword + "%");
                    } else {
                        cb.or(or, cb.like(root.get(ReimbursePrice_.tmtDrug).get(TMTDrug_.tmtId), "%" + keyword + "%"));
                    }
                }
                return or;
            }
        };
    }
    
    public static Specification<ReimbursePrice> fsnLike(final List<String> fsn) {
        return new Specification<ReimbursePrice>() {
            @Override
            public Predicate toPredicate(Root<ReimbursePrice> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : fsn) {
                    if (or == null) {
                        or = cb.like(root.get(ReimbursePrice_.tmtDrug).get(TMTDrug_.fsn), "%" + keyword + "%");
                    } else {
                        cb.or(or, cb.like(root.get(ReimbursePrice_.tmtDrug).get(TMTDrug_.fsn), "%" + keyword + "%"));
                    }
                }
                return or;
            }
        };
    }
}
