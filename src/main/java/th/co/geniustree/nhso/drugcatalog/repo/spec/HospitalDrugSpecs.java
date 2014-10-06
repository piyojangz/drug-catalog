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

    public static Specification<HospitalDrug> hospDrugCodeLike(final List<String> keywords) {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(HospitalDrug_.hospDrugCode)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(HospitalDrug_.hospDrugCode)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<HospitalDrug> tmtIdLike(final List<String> keywords) {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(HospitalDrug_.tmtId)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(HospitalDrug_.tmtId)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<HospitalDrug> tradeNameLike(final List<String> keywords) {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(HospitalDrug_.tradeName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(HospitalDrug_.tradeName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<HospitalDrug> genericNameLike(final List<String> keywords) {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(HospitalDrug_.genericName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(HospitalDrug_.genericName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<HospitalDrug> dosageFormLike(final List<String> keywords) {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(HospitalDrug_.dosageForm)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(HospitalDrug_.dosageForm)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<HospitalDrug> noTmt() {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get(HospitalDrug_.tmtId).isNull();
            }

        };
    }

    public static Specification<HospitalDrug> approved() {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(HospitalDrug_.approved), true);
            }

        };
    }
    public static Specification<HospitalDrug> notApproved() {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                throw new UnsupportedOperationException("Not yet impl");//return cb.equal(root.get(HospitalDrug_.requestItem).get(RequestItem_.status),RequestItem.Status.REJECT);
            }

        };
    }

    public static Specification<HospitalDrug> waitApprove() {
        return new Specification<HospitalDrug>() {

            @Override
            public Predicate toPredicate(Root<HospitalDrug> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                throw new UnsupportedOperationException("Not yet impl");//return cb.equal(root.get(HospitalDrug_.requestItem).get(RequestItem_.status),RequestItem.Status.REQUEST);
            }
        };
    }
}
