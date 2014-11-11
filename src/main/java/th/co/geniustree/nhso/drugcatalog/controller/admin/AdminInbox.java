/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class AdminInbox implements Serializable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminInbox.class);
    @Autowired
    private RequestItemRepo requestItemRepo;
    private SpringDataLazyDataModelSupport<RequestItem> requestItems;
    private List<List<RequestItem>> requestItemHolders;
    private Hospital selectedHospital;
    private String keyword;
    @Autowired
    private TMTDrugRepo tmtDrugRepo;

    @PostConstruct
    public void postConstruct() {
        requestItemHolders = new ArrayList<>();
    }

    public SpringDataLazyDataModelSupport<RequestItem> getRequestItems() {
        return requestItems;
    }

    public List<List<RequestItem>> getRequestItemHolders() {
        return requestItemHolders;
    }

    public void setRequestItemHolders(List<List<RequestItem>> requestItemHolders) {
        this.requestItemHolders = requestItemHolders;
    }

    public Hospital getSelectedHospital() {
        return selectedHospital;
    }

    public void setSelectedHospital(Hospital selectedHospital) {
        this.selectedHospital = selectedHospital;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void showSearchHospitalDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        List<String> keywords = new ArrayList<>();
        keywords.add(keyword);
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/common/searchHospitalDialog", options, params);
    }

    public void searchHospitalDialogReturn(SelectEvent event) {
        selectedHospital = (Hospital) event.getObject();
        if (selectedHospital != null) {
            keyword = selectedHospital.getFullName();
            search();
        }
        log.info("selected hospital from search dialog is => {}", selectedHospital);
    }

    public void search() {
        if (selectedHospital != null) {
            Page<RequestItem> pageResult = requestItemRepo.findByStatusAndHcode(RequestItem.Status.REQUEST,
                    selectedHospital.getHcode(), new PageRequest(0, 3000, Sort.Direction.ASC, "requestDate"));
            for (RequestItem item : pageResult.getContent()) {
                List<RequestItem> requestItems = new ArrayList<>();
                requestItems.add(createRequestFormTmt(item));
                requestItems.add(item);
                requestItemHolders.add(requestItems);
            }
        }
    }

    private RequestItem createRequestFormTmt(RequestItem item) {
        TMTDrug tmtDrug = tmtDrugRepo.findOne(item.getUploadDrugItem().getTmtId());
        if (tmtDrug != null) {
            return new RequestItem(tmtDrug);
        }
        return new RequestItem();
    }

}
