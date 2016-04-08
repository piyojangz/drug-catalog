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
import th.co.geniustree.nhso.drugcatalog.model.MatchTMTNDC24;
import th.co.geniustree.nhso.drugcatalog.model.MatchTMTNDC24_;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug_;

/**
 *
 * @author thanthathon.b
 */
public class MatchTMTNDC24Specs {

    public static Specification<MatchTMTNDC24> tmtLike(final List<String> keywords) {
        return new Specification<MatchTMTNDC24>() {
            @Override
            public Predicate toPredicate(Root<MatchTMTNDC24> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : keywords) {
                    if (or == null) {
                        or = cb.like(root.get(MatchTMTNDC24_.tmtid), "%" + keyword + "%");
                    } else {
                        cb.or(or, cb.like(root.get(MatchTMTNDC24_.tmtid), "%" + keyword + "%"));
                    }
                }
                return or;
            }
        };
    }
    
    public static Specification<MatchTMTNDC24> fsnLike(final List<String> keywords) {
        return new Specification<MatchTMTNDC24>() {
            @Override
            public Predicate toPredicate(Root<MatchTMTNDC24> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : keywords) {
                    if (or == null) {
                        or = cb.like(cb.lower(root.get(MatchTMTNDC24_.tmtDrug).get(TMTDrug_.fsn)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.or(or, cb.like(cb.lower(root.get(MatchTMTNDC24_.tmtDrug).get(TMTDrug_.fsn)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return or;
            }
        };
    }
    
    public static Specification<MatchTMTNDC24> ndcLike(final List<String> keywords) {
        return new Specification<MatchTMTNDC24>() {
            @Override
            public Predicate toPredicate(Root<MatchTMTNDC24> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate or = null;
                for (String keyword : keywords) {
                    if (or == null) {
                        or = cb.like((root.get(MatchTMTNDC24_.ndc24)), "%" + keyword + "%");
                    } else {
                        cb.or(or, cb.like((root.get(MatchTMTNDC24_.ndc24)), "%" + keyword + "%"));
                    }
                }
                return or;
            }
        };
    }
}
