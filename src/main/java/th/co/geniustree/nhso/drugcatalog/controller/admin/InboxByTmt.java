/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Strings;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class InboxByTmt implements Serializable {

    private SpringDataLazyDataModelSupport<TMTDrug> tmtDrug;
    @Autowired
    private RequestItemRepo requestItemRepo;
    private String keyword;

    @PostConstruct
    public void postConstruct() {
        tmtDrug = new SpringDataLazyDataModelSupport<TMTDrug>() {

            @Override
            public Page<TMTDrug> load(Pageable pageAble) {
                if (Strings.isNullOrEmpty(keyword)) {
                    return requestItemRepo.findTMTDrugByStatus(RequestItem.Status.REQUEST, pageAble);
                } else {
                    return requestItemRepo.findTMTDrugByStatusAndTmtIdLike(RequestItem.Status.REQUEST,keyword, pageAble);
                }
            }
        };
    }

    public SpringDataLazyDataModelSupport<TMTDrug> getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(SpringDataLazyDataModelSupport<TMTDrug> tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void search() {

    }

}
