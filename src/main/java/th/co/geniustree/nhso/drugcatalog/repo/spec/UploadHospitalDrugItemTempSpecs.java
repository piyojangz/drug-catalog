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
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItemTemp;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItemTemp_;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug_;

/**
 *
 * @author moth
 */
public class UploadHospitalDrugItemTempSpecs {

    public static Specification<UploadHospitalDrugItemTemp> hcodeEq(final String hcode) {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItemTemp_.uploadDrug).get(UploadHospitalDrug_.hcode), hcode);
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> hospDrugCodeLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.hospDrugCode)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.hospDrugCode)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> tmtIdLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.tmtId)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.tmtId)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> tradeNameLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.tradeName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.tradeName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> genericNameLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.genericName)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.genericName)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> dosageFormLike(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate and = null;
                for (String keyword : keywords) {
                    if (and == null) {
                        and = cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.dosageForm)), "%" + keyword.toLowerCase() + "%");
                    } else {
                        cb.and(and, cb.like(cb.lower(root.get(UploadHospitalDrugItemTemp_.dosageForm)), "%" + keyword.toLowerCase() + "%"));
                    }
                }
                return and;
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> approved() {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItemTemp_.requestItem).get(RequestItem_.status), RequestItem.Status.ACCEPT);
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> notApproved() {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItemTemp_.requestItem).get(RequestItem_.status), RequestItem.Status.REJECT);
            }

        };
    }

    public static Specification<UploadHospitalDrugItemTemp> waitApprove() {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItemTemp_.requestItem).get(RequestItem_.status), RequestItem.Status.REQUEST);
            }
        };
    }

    public static Specification<UploadHospitalDrugItemTemp> notDelete() {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get(UploadHospitalDrugItemTemp_.requestItem).get(RequestItem_.deleted), Boolean.FALSE);
            }
        };
    }
    
    public static Specification<UploadHospitalDrugItemTemp> productCatIn(final List<String> keywords) {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get(UploadHospitalDrugItemTemp_.productCat).in(keywords);
            }

        };
    }
    
    public static Specification<UploadHospitalDrugItemTemp> tmtidIsNull() {
        return new Specification<UploadHospitalDrugItemTemp>() {

            @Override
            public Predicate toPredicate(Root<UploadHospitalDrugItemTemp> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return root.get(UploadHospitalDrugItemTemp_.tmtId).isNull();
            }

        };
    }

}
