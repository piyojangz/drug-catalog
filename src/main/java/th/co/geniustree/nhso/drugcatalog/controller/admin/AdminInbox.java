/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.basicmodel.readonly.Hospital;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.repo.RequestItemRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;

/**
 *
 * @author moth
 */
@Scope("view")
@Component
public class AdminInbox implements Serializable {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminInbox.class);
    private List<String> selectColumns = Arrays.asList(new String[]{"HOSPDRUGCODE", "TMTID", "GENERICNAME", "TRADENAME", "DOSAGEFORM"});
    private String keyword;
    @Autowired
    private RequestItemRepo requestItemRepo;
    private SpringDataLazyDataModelSupport<RequestItem> requestItems;
    private List<List<RequestItem>> requestItemHolders = new ArrayList<>();
    private Hospital selectedHospital;
    private String hcode;
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    @Autowired
    private ApproveService approveService;
    private List<RequestItem> approveRequests = new ArrayList<>();
    private List<RequestItem> notApproveRequests = new ArrayList<>();
    private long totalElements;
    private long displayElement;

    @PostConstruct
    public void postConstruct() {

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

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public List<RequestItem> getApproveRequests() {
        return approveRequests;
    }

    public void setApproveRequests(List<RequestItem> approveRequests) {
        this.approveRequests = approveRequests;
    }

    public List<RequestItem> getNotApproveRequests() {
        return notApproveRequests;
    }

    public void setNotApproveRequests(List<RequestItem> notApproveRequests) {
        this.notApproveRequests = notApproveRequests;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getDisplayElement() {
        return displayElement;
    }

    public void setDisplayElement(long displayElement) {
        this.displayElement = displayElement;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }
    

    public void showSearchHospitalDialog() {
        requestItemHolders.clear();
        notApproveRequests.clear();
        approveRequests.clear();
        if (checkHospitalReturnOneElement()) {
            hcode = selectedHospital.getFullName();
            search();
            return;
        }
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("draggable", true);
        options.put("resizable", true);
        options.put("contentHeight", 500);
        options.put("contentWidth", 800);
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        List<String> keywords = new ArrayList<>();
        keywords.add(hcode);
        params.put("keyword", keywords);
        RequestContext.getCurrentInstance().openDialog("/private/common/searchHospitalDialog", options, params);
    }

    public void searchHospitalDialogReturn(SelectEvent event) {
        selectedHospital = (Hospital) event.getObject();
        if (selectedHospital != null) {
            hcode = selectedHospital.getFullName();
            search();
        }
        log.info("selected hospital from search dialog is => {}", selectedHospital);
    }

    public void search() {
        requestItemHolders.clear();
        notApproveRequests.clear();
        approveRequests.clear();
        if (selectedHospital != null) {
            Page<RequestItem> pageResult = requestItemRepo.findByStatusAndHcodeAndNotDel(RequestItem.Status.REQUEST,
                    selectedHospital.getHcode(), new PageRequest(0, 10, Sort.Direction.ASC, "requestDate"));
            totalElements = pageResult.getTotalElements();
            displayElement = pageResult.getSize();
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

    public void error(RequestItem requestItem, String columnName) {
        requestItem.getErrorColumns().add(columnName);
        requestItem.setStatus(RequestItem.Status.REJECT);
        if (!notApproveRequests.contains(requestItem)) {
            notApproveRequests.add(requestItem);
        }
        log.info("add error to RequestId = {}", requestItem.getId());
    }

    public void clearErrorColumns(RequestItem requestItem) {
        requestItem.getErrorColumns().clear();
        requestItem.setStatus(RequestItem.Status.REQUEST);
        notApproveRequests.remove(requestItem);
    }

    public void approve(ValueChangeEvent event) {
        UIComponent component = event.getComponent();
        RequestItem item = (RequestItem) component.getAttributes().get("selectedItem");
        log.debug("selectItem = {}", item.getId());
        if (event.getNewValue() != null) {
            item.setStatus(RequestItem.Status.valueOf(event.getNewValue().toString()));
            if (item.getStatus() == RequestItem.Status.ACCEPT) {
                item.getErrorColumns().clear();
                approveRequests.add(item);
                notApproveRequests.remove(item);
            } else {
                notApproveRequests.add(item);
                approveRequests.remove(item);
            }
        }
    }

    public String save() {
        if (notApproveHaveErrorColumn()) {
            List<RequestItem> merge = new ArrayList<>(approveRequests);
            merge.addAll(notApproveRequests);
            approveService.approveOrReject(merge);
            requestItemHolders.clear();
            notApproveRequests.clear();
            approveRequests.clear();
            //TODO send mail to each HCODE
            search();
            FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        }
        return null;
    }

    public String clear() {
        requestItemHolders.clear();
        notApproveRequests.clear();
        approveRequests.clear();
        hcode = "";
        return "/private/admin/drug/inbox.xhtml?faces-redirect=true";
    }

    private boolean checkHospitalReturnOneElement() {
        String _keyword = "%" + Strings.nullToEmpty(this.hcode).trim() + "%";
        PageRequest pageRequest = new PageRequest(0, 3);
        Page<Hospital> findHospitalInTmt = uploadHospitalDrugRepo.findHospitalInTmt(_keyword, _keyword, pageRequest);
        if (findHospitalInTmt.getTotalElements() == 1) {
            selectedHospital = findHospitalInTmt.getContent().get(0);
            log.info("selected hospital from search dialog is => {}", selectedHospital);
            return true;
        } else {
            return false;
        }
    }

    private boolean notApproveHaveErrorColumn() {
        for (RequestItem notApprove : notApproveRequests) {
            if (notApprove.getErrorColumns().isEmpty()) {
                FacesMessageUtils.error("จะต้องระบุ column ที่ไม่ให้ผ่านด้วย");
                return false;
            }
        }
        return true;
    }

}
