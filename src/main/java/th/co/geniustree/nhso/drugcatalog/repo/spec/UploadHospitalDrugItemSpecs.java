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
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem_;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItemTemp;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItemTemp_;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem_;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug_;

/**
 *
 * @author moth
 */
public class UploadHospitalDrugItemSpecs {

    public static Specification<UploadHospitalDrugItem> hcodeEq(final String hcode) {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItem_.uploadDrug).get(UploadHospitalDrug_.hcode), hcode);
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> hospDrugCodeLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItem_.hospDrugCode)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItem_.hospDrugCode)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> tmtIdLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItem_.tmtId)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItem_.tmtId)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> tradeNameLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItem_.tradeName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItem_.tradeName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> genericNameLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItem_.genericName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItem_.genericName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> dosageFormLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItem_.dosageForm)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItem_.dosageForm)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> approved() {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItem_.requestItem).get(RequestItem_.status), RequestItem.Status.ACCEPT);
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> notApproved() {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItem_.requestItem).get(RequestItem_.status), RequestItem.Status.REJECT);
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> waitApprove() {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItem_.requestItem).get(RequestItem_.status), RequestItem.Status.REQUEST);
            }
        };
    }

    public static Specification<UploadHospitalDrugItem> notDelete() {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItem_.requestItem).get(RequestItem_.deleted), Boolean.FALSE);
            }
        };
    }

    public static Specification<UploadHospitalDrugItem> productCatIn(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get(UploadHospitalDrugItem_.productCat).in(keywords);
            }

        };
    }

    public static Specification<UploadHospitalDrugItem> tmtidIsNull() {
        return new Specification<UploadHospitalDrugItem>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get(UploadHospitalDrugItem_.tmtId).isNull();
            }

        };
    }
}
