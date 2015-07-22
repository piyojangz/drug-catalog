/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.Fund;
import th.co.geniustree.nhso.drugcatalog.repo.spec.FundSpecs;
import th.co.geniustree.nhso.drugcatalog.service.FundService;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class FundController {

    private static final Logger log = LoggerFactory.getLogger(FundController.class);

    @Autowired
    private FundService fundService;

    private SpringDataLazyDataModelSupport<Fund> funds;

    private Fund fund;
    
    private String keyword;

    @PostConstruct
    public void postConstruct() {
        fund = new Fund();
        funds = new SpringDataLazyDataModelSupport<Fund>() {

            @Override
            public Page<Fund> load(Pageable pageAble) {
                return fundService.findAllPaging(pageAble);
            }
        };
    }

    public void onSave() {
        if (!fund.getFundCode().isEmpty() && !fund.getName().isEmpty()) {
            try {
                fundService.save(fund);
                FacesMessageUtils.info("บันทึกข้อมูล สำเร็จ");
            } catch (Exception e) {
                FacesMessageUtils.error("บันทึกข้อมูล ไม่สำเร็จ");
            }
        }
        fund = new Fund();
    }
    
    public void search(){
        funds = new SpringDataLazyDataModelSupport<Fund>() {

            @Override
            public Page<Fund> load(Pageable pageAble) {
                List<String> keyList = Arrays.asList(keyword.split(" "));
                Specification<Fund> spec = Specifications.where(FundSpecs.fundCodeLike(keyList)).or(FundSpecs.fundNameLike(keyList));
                
                Page<Fund> page = fundService.findAllBySpecs(spec, pageAble);
                log.info("search word : {}" , keyword);
                return page;
            }
        };
    }

    //****************** getter and setter method ******************
    public SpringDataLazyDataModelSupport<Fund> getFunds() {
        return funds;
    }

    public void setFunds(SpringDataLazyDataModelSupport<Fund> funds) {
        this.funds = funds;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }
    
    

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
