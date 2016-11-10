/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.validation.Validator;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.Constants;
import th.co.geniustree.nhso.drugcatalog.controller.utils.DateUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.ReimbursePriceTPExcelModel;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTP;
import th.co.geniustree.nhso.drugcatalog.model.ReimbursePriceTPID;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.HospitalDrugService;
import th.co.geniustree.nhso.drugcatalog.service.ReimbursePriceService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class UploadReimbursePriceTP implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(UploadReimbursePriceTP.class);

    @Autowired
    @Qualifier("app")
    private Properties app;

    @Autowired
    private Validator beanValidator;

    @Autowired
    private ReimbursePriceService reimbursePriceService;

    @Autowired
    private TMTDrugService tmtDrugService;

    @Autowired
    private HospitalDrugService hospitalDrugService;

    private UploadedFile file;
    private List<ReimbursePriceTPExcelModel> notPassModels = new ArrayList<>();
    private List<ReimbursePriceTPExcelModel> passModels = new ArrayList<>();
    private List<ReimbursePriceTP> reimbursePrices = new ArrayList<>();

    private boolean duplicateFile = false;
    private String originalFileName;

    private File uploadtempFileDir;
    private String saveFileName;
    private File targetFile;

    private StreamedContent templateFile;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "REIMBURSEPRICETP");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/files/reimburseprice_tp_template.xlsx");
        templateFile = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "reimburseprice_tp_template.xlsx");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<ReimbursePriceTPExcelModel> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<ReimbursePriceTPExcelModel> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public List<ReimbursePriceTPExcelModel> getPassModels() {
        return passModels;
    }

    public void setPassModels(List<ReimbursePriceTPExcelModel> passModels) {
        this.passModels = passModels;
    }

    public StreamedContent getTemplateFile() {
        return templateFile;
    }

    public List<ReimbursePriceTP> getReimbursePriceTPs() {
        return reimbursePrices;
    }

    public void setReimbursePriceTPs(List<ReimbursePriceTP> reimbursePrices) {
        this.reimbursePrices = reimbursePrices;
    }

    public void handleFileUpload(FileUploadEvent event) {
        reset();
        file = event.getFile();
        try (InputStream inputFileStream = file.getInputstream()) {
            originalFileName = file.getFileName();
            saveFileName = UUID.randomUUID().toString() + "-" + file.getFileName();
            targetFile = new File(uploadtempFileDir, saveFileName);
            LOG.debug("save target file to = {}", targetFile.getAbsolutePath());
            Files.asByteSink(targetFile).writeFrom(inputFileStream);
            ReaderUtils.read(targetFile, ReimbursePriceTPExcelModel.class, new ReadCallback<ReimbursePriceTPExcelModel>() {

                @Override
                public void header(List headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, ReimbursePriceTPExcelModel bean) {
                    bean.setRowNum(rowNum);
                    processValidate(bean);
                    if (hasError(bean)) {
                        notPassModels.add(bean);
                    } else {
                        passModels.add(bean);
                    }
                }

                @Override
                public void err(Exception e) {
                    if (e instanceof ColumnNotFoundException) {
                        FacesMessageUtils.error("ไม่พบ column => " + Joiner.on(",").join(((ColumnNotFoundException) e).getColumnNames()));
                    }
                    LOG.error(null, e);
                }
            });
        } catch (ColumnNotFoundException columnNotFound) {
            reset();
            FacesMessageUtils.error("ไม่พบ column => " + Joiner.on(",").join(columnNotFound.getColumnNames()));
        } catch (Exception iOException) {
            reset();
            FacesMessageUtils.error(iOException);
        }
        LOG.debug("File : {}", file);
    }

    private void processValidate(ReimbursePriceTPExcelModel bean) {

        TMTDrug tmtDrug = tmtDrugService.findOneWithoutTx(bean.getTmtid());
        Date dateEffective = null;
        try {
            dateEffective = DateUtils.parseUSDate(Constants.TMT_DATE_FORMAT, bean.getEffectiveDate());
        } catch (RuntimeException re) {
            return;
        }
        if (isHospitalDrugNotFound(bean.getHcode(), bean.getHospDrugCode())) {
            bean.addError("rowNum", "ไม่พบข้อมูลรายการยาของหน่วยบริการ");
        }
        if (tmtDrug == null) {
            bean.addError("tmtid", "ไม่พบ TMTID นี้ในระบบ");
        } else if (!tmtDrug.getType().equals(TMTDrug.Type.TP)) {
            bean.addError("tmtid", "TMT นี้ไม่ใช่ระดับ TP");
        } else if (dateEffective != null && isDuplicateDateEffective(tmtDrug.getTmtId(), bean.getHospDrugCode(), bean.getHcode(), dateEffective)) {
            bean.addError("rowNum", "มีข้อมูลนี้อยู่แล้วในฐานข้อมูล");
        }
        for(ReimbursePriceTPExcelModel pass : passModels){
            if(pass.equals(bean)){
                bean.addError("rowNum", "พบข้อมูลซ้ำในรายการที่ผ่านการตรวจสอบ");
                break;
            }
        }
    }

    private boolean isHospitalDrugNotFound(String hcode, String hospDrugCode) {
        return hospitalDrugService.findById(hcode, hospDrugCode) == null;
    }

    public Date convertStringToDate(String date) {
        try {
            return DateUtils.parseUSDate(Constants.TMT_DATE_FORMAT, date);
        } catch (Exception e){
            LOG.debug("Can't parse to Date", e);
            return null;
        }
    }

    private List<ReimbursePriceTP> convertBeanToReimbursePriceTPList(List<ReimbursePriceTPExcelModel> list) {
        List<ReimbursePriceTP> prices = new LinkedList<>();
        for (ReimbursePriceTPExcelModel item : list) {
            Date dateEffective;
            try {
                dateEffective = DateUtils.parseUSDate(Constants.TMT_DATE_FORMAT, item.getEffectiveDate());
            } catch (RuntimeException re) {
                continue;
            }
            TMTDrug tmtDrug = tmtDrugService.findOneWithoutTx(item.getTmtid());
            ReimbursePriceTP reimbursePrice = new ReimbursePriceTP(new ReimbursePriceTPID(tmtDrug.getTmtId(), item.getHospDrugCode(), item.getHcode(), dateEffective));
            reimbursePrice.setTmtDrug(tmtDrug);
            reimbursePrice.setPrice(new BigDecimal(item.getPrice()));
            prices.add(reimbursePrice);
        }
        return prices;
    }

    private boolean hasError(ReimbursePriceTPExcelModel bean) {
        return !bean.getErrorMap().isEmpty();
    }

    private boolean isDuplicateDateEffective(String tmtId, String hospDrugCode, String hcode, Date dateEffective) {
        return reimbursePriceService.isExists(hcode, hospDrugCode, tmtId, dateEffective);
    }

    public boolean isDuplicateFile() {
        return duplicateFile;
    }

    public void setDuplicateFile(boolean duplicateFile) {
        this.duplicateFile = duplicateFile;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void save() {
        List<ReimbursePriceTP> list = convertBeanToReimbursePriceTPList(passModels);
        try {
            LOG.debug("total prices list : {}", list.size());
            reimbursePriceService.saveTP(list);
            FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        } catch (Exception e) {
            LOG.error("Can't save", e);
            FacesMessageUtils.error("ไม่สามารถบันทึกข้อมูล");
        } finally {
            reset();
        }
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
        reimbursePrices.clear();
    }

}
