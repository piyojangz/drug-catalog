/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.SpringDataLazyDataModelSupport;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.model.RequestItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.HospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.ApproveService;
import th.co.geniustree.nhso.drugcatalog.service.RequestItemService;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class ApproveByTmt implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ApproveByTmt.class);
    private String selectTmtId;
    private TMTDrug tmtDrug;
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    @Autowired
    private ApproveService approveService;
    private List<RequestItem> request;
    private List<RequestItem> approveRequests = new ArrayList<>();
    private List<RequestItem> notApproveRequests = new ArrayList<>();
    private String notApproveMessage;
    @Autowired
    private HospitalDrugRepo hospitalDrugRepo;
    private BigDecimal avg;
    private BigDecimal stdev;
    @Autowired
    private RequestItemService requestItemService;

    private UploadHospitalDrugItem uploadDrugItem;
    
    @PostConstruct
    public void postConstruct() {
    }

    public UploadHospitalDrugItem getUploadDrugItem() {
        return uploadDrugItem;
    }

    public void setUploadDrugItem(UploadHospitalDrugItem uploadDrugItem) {
        this.uploadDrugItem = uploadDrugItem;
    }

    public String getSelectTmtId() {
        return selectTmtId;
    }

    public void setSelectTmtId(String selectTmtId) {
        this.selectTmtId = selectTmtId;
    }

    public TMTDrug getTmtDrug() {
        return tmtDrug;
    }

    public void setTmtDrug(TMTDrug tmtDrug) {
        this.tmtDrug = tmtDrug;
    }

    public List<RequestItem> getRequest() {
        return request;
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

    public String getNotApproveMessage() {
        return notApproveMessage;
    }

    public void setNotApproveMessage(String notApproveMessage) {
        this.notApproveMessage = notApproveMessage;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getStdev() {
        return stdev;
    }

    public void setStdev(BigDecimal stdev) {
        this.stdev = stdev;
    }

    public void error(RequestItem requestItem, String columnName) {
        requestItem.getErrorColumns().add(columnName);
        requestItem.setStatus(RequestItem.Status.REJECT);
        if (!notApproveRequests.contains(requestItem)) {
            notApproveRequests.add(requestItem);
        }
        LOG.info("add error to RequestId = {}", requestItem.getId());
    }

    public void clearErrorColumns(RequestItem requestItem) {
        requestItem.getErrorColumns().clear();
        requestItem.setStatus(RequestItem.Status.REQUEST);
        notApproveRequests.remove(requestItem);
    }

    public void approve(ValueChangeEvent event) {
        UIComponent component = event.getComponent();
        RequestItem item = (RequestItem) component.getAttributes().get("selectedItem");
        LOG.debug("selectItem = {}", item.getId());
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

    public void remove(RequestItem item) {
        notApproveRequests.remove(item);
        approveRequests.remove(item);
        item.setStatus(RequestItem.Status.REQUEST);
    }

    public String save() {
        if (notApproveHaveErrorColumn()) {
            List<RequestItem> merge = new ArrayList<>(approveRequests);
            merge.addAll(notApproveRequests);
            approveService.approveOrReject(merge);
            notApproveRequests.clear();
            approveRequests.clear();
            //TODO send mail to each HCODE
            load();
            FacesMessageUtils.info("บันทึกเสร็จสิ้น");
            return "inbox-groupby-tmt";
        } else {
            return null;
        }

    }

    public void load() {
        avg = hospitalDrugRepo.avg(selectTmtId);
        stdev = hospitalDrugRepo.stddev(selectTmtId);
        tmtDrug = tmtDrugRepo.findOne(selectTmtId);
        request = requestItemService.findAllByStatusAndTmtId(RequestItem.Status.REQUEST, selectTmtId);
        request.add(0, new RequestItem(tmtDrug));
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

    public static class SelectableRequestItemModel extends SpringDataLazyDataModelSupport<RequestItem> {

        @Override
        public Object getRowKey(Object object) {
            RequestItem requestItem = (RequestItem) object;
            return requestItem.getId().toString();
        }

        @Override
        public RequestItem getRowData(String rowKey) {
            List<RequestItem> wrappedData = (List<RequestItem>) getWrappedData();
            for (RequestItem item : wrappedData) {
                if (item.getId().toString().equals(rowKey)) {
                    return item;
                }
            }
            return null;
        }
    }
    
    public void searchTMTDrug(UploadHospitalDrugItem uploadHospitalDrugItem) {
        this.uploadDrugItem = uploadHospitalDrugItem;
        TMTDrug drug = tmtDrugRepo.findOne(uploadDrugItem.getTmtId());
        uploadDrugItem.setTmtDrug(drug);
    }
}
