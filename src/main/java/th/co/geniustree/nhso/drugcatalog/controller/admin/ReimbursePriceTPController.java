/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.ReimbursePriceService;

/**
 *
 * @author thanthathon.b
 */
@Component
@Scope("view")
public class ReimbursePriceTPController implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ReimbursePriceTPController.class);

    @Autowired
    private ReimbursePriceService reimbursePriceService;

    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;

    private SpringDataLazyDataModelSupport<HospitalDrug> hospitalDrugs;
    private HospitalDrug selectHospitalDrug;
    private SpringDataLazyDataModelSupport<ReimbursePriceTP> data;
    private Date dateEffective;
    private BigDecimal price;

    private String keyword;

    @PostConstruct
    public void postConstruct() {
        loadData();
    }

    public void loadHospitalDrug() {
        hospitalDrugs = new SpringDataLazyDataModelSupport<HospitalDrug>() {

            @Override
            public Page<HospitalDrug> load(Pageable pageAble) {
                return hospitalDrugRepo.findByApprovedAndNotDeleted(pageAble);
            }

        };
    }
    
    public void reset(){
        selectHospitalDrug = null;
        dateEffective = null;
        price = null;
    }

    public void select(HospitalDrug hospitalDrug) {
        selectHospitalDrug = hospitalDrug;
    }

    public void loadData() {
        data = new SpringDataLazyDataModelSupport<ReimbursePriceTP>() {

            @Override
            public Page<ReimbursePriceTP> load(Pageable pageAble) {
                return reimbursePriceService.findAllTP(pageAble);
            }

        };
    }

    public void search() {
        data = new SpringDataLazyDataModelSupport<ReimbursePriceTP>() {

            @Override
            public Page<ReimbursePriceTP> load(Pageable pageAble) {
                return reimbursePriceService.searchTP(keyword, pageAble);
            }

        };
    }

    public void addNewReimbursePriceTP() {
        try {
            reimbursePriceService.save(selectHospitalDrug, price, dateEffective);
            FacesMessageUtils.info("กำหนดราคายา TP เรียบร้อย");
        } catch (Exception e) {
            FacesMessageUtils.error("เพิ่มรายการยาไม่สำเร็จ");
            LOG.debug("Can't add new ReimbursePriceTP", e);
        }

    }

    public SpringDataLazyDataModelSupport<ReimbursePriceTP> getData() {
        return data;
    }

    public void setData(SpringDataLazyDataModelSupport<ReimbursePriceTP> data) {
        this.data = data;
    }

    public SpringDataLazyDataModelSupport<HospitalDrug> getHospitalDrugs() {
        return hospitalDrugs;
    }

    public void setHospitalDrugs(SpringDataLazyDataModelSupport<HospitalDrug> hospitalDrugs) {
        this.hospitalDrugs = hospitalDrugs;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public HospitalDrug getSelectHospitalDrug() {
        return selectHospitalDrug;
    }

    public void setSelectHospitalDrug(HospitalDrug selectHospitalDrug) {
        this.selectHospitalDrug = selectHospitalDrug;
    }

    public Date getDateEffective() {
        return dateEffective;
    }

    public void setDateEffective(Date dateEffective) {
        this.dateEffective = dateEffective;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
