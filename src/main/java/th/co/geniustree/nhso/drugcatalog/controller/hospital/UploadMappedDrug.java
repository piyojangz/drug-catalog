/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.DigestUtils;
import th.co.geniustree.nhso.drugcatalog.authen.Role;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.UploadItemOrderHelper;
import th.co.geniustree.nhso.drugcatalog.input.AGroup;
import th.co.geniustree.nhso.drugcatalog.input.EDGroup;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.input.Lastgroup;
import th.co.geniustree.nhso.drugcatalog.input.UGroup;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugErrorItem;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugRepo;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
import th.co.geniustree.nhso.drugcatalog.service.DuplicateCheckFacade;
import th.co.geniustree.nhso.drugcatalog.service.UploadHospitalDrugService;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class UploadMappedDrug implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadMappedDrug.class);
    private UploadedFile file;
    private List<HospitalDrugExcelModel> models = new ArrayList<>();
    private List<HospitalDrugExcelModel> notPassModels = new ArrayList<>();
    @Autowired
    @Qualifier("app")
    private Properties app;
    private File uploadtempFileDir;
    @Autowired
    private Validator beanValidator;
    @Autowired
    private UploadHospitalDrugService uploadHospitalDrugService;
    @Autowired
    private DuplicateCheckFacade duplicateCheckFacade;
    private String saveFileName;
    private String originalFileName;
    private String shaHex;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    private boolean duplicateFile;
    @Autowired
    private TMTDrugRepo tmtDrugRepo;
    private File uploadDir;
    private File targetFile;
    private String hcodeFromFile;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation);
        uploadDir = new File(uploadtempFileDir, "UPLOAD");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        uploadDir.mkdir();
    }

    public String getHcodeFromFile() {
        return hcodeFromFile;
    }

    public void setHcodeFromFile(String hcodeFromFile) {
        this.hcodeFromFile = hcodeFromFile;
    }

    public List<HospitalDrugExcelModel> getModels() {
        return models;
    }

    public void setModels(List<HospitalDrugExcelModel> models) {
        this.models = models;
    }

    public List<HospitalDrugExcelModel> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<HospitalDrugExcelModel> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public boolean isDuplicateFile() {
        return duplicateFile;
    }

    public String save() {
        if (duplicateFile) {
            FacesMessageUtils.info("ไฟล์นี้เคยนำเข้าแล้ว.");
            return null;
        }
        UploadHospitalDrug uploadDrug = new UploadHospitalDrug();
        uploadDrug.setShaHex(shaHex);
        uploadDrug.setHcode(hcodeFromFile);
        List<UploadHospitalDrugItem> items = createPassItem(uploadDrug);
        List<UploadHospitalDrugErrorItem> errorItems = createErrorItem(uploadDrug);
        UploadItemOrderHelper.reorderByUpdateFlageAFirstDeleteLast(items);
        uploadDrug.setPassItems(items);
        uploadDrug.setErrorItems(errorItems);
        uploadDrug.setOriginalFilename(originalFileName);
        uploadDrug.setSavedFileName(saveFileName);
        uploadDrug.setItemCount(models.size() + notPassModels.size());
        uploadDrug.setPassItemCount(models.size());
        uploadHospitalDrugService.saveUploadHospitalDrugAndRequest(uploadDrug);
        try {
            Files.copy(targetFile, new File(uploadDir, targetFile.getName()));
            boolean delete = targetFile.delete();
            LOG.info("delete temp file {} result {}", targetFile.getAbsolutePath(), delete);
        } catch (IOException ex) {
            LOG.warn(null, ex);
        }
        FacesMessageUtils.info("บันทึกเสร็จสิ้น.");
        reset();
        return null;
    }

    private List<UploadHospitalDrugItem> createPassItem(UploadHospitalDrug uploadDrug) throws BeansException {
        List<UploadHospitalDrugItem> items = new ArrayList<>();
        for (HospitalDrugExcelModel passModel : models) {
            UploadHospitalDrugItem item = new UploadHospitalDrugItem();
            BeanUtils.copyProperties(passModel, item);
            item.setUploadDrug(uploadDrug);
            items.add(item);
        }
        return items;
    }

    private List<UploadHospitalDrugErrorItem> createErrorItem(UploadHospitalDrug uploadDrug) {
        List<UploadHospitalDrugErrorItem> errorItems = new ArrayList<>();
        for (HospitalDrugExcelModel notPassModel : notPassModels) {
            UploadHospitalDrugErrorItem errorItem = new UploadHospitalDrugErrorItem();
            BeanUtils.copyProperties(notPassModel, errorItem);
            errorItem.addAllError(notPassModel.getErrorMap());
            errorItem.setUploadDrug(uploadDrug);
            errorItems.add(errorItem);
        }
        return errorItems;
    }

    public String reset() {
        saveFileName = null;
        originalFileName = null;
        models.clear();
        notPassModels.clear();
        duplicateFile = false;
        file = null;
        shaHex = null;
        return null;
    }

    public String upload() {
        models.clear();
        notPassModels.clear();
        //TODO file == null NOT work in prime fileupload(simple) and require NOT work too.
        if (file.getFileName() == null || file.getFileName().isEmpty()) {
            FacesMessageUtils.info("Please select file first.");
            return null;
        }
        if (!SecurityUtil.getUserDetails().getAuthorities().contains(Role.ADMIN) && !SecurityUtil.getUserDetails().getAuthorities().contains(Role.EMCO)) {
            FacesMessageUtils.error("ไม่มีสิทธิ์ในการนำเข้าข้อมูล");
            return null;
        }
        String nameWithoutExtension = Files.getNameWithoutExtension(file.getFileName());
        if (nameWithoutExtension.length() < 5) {
            FacesMessageUtils.info("ชื่อไฟล์จะต้องขึ้นต้นด้วย HCODE 5 ตัวอักษร");
            return null;
        }
        hcodeFromFile = file.getFileName().substring(0, 5);
        if (SecurityUtil.getUserDetails().getAuthorities().contains(Role.EMCO) && !hcodeFromFile.equalsIgnoreCase(SecurityUtil.getUserDetails().getOrgId())) {
            FacesMessageUtils.error("ไม่ใช่ไฟล์ Drug Catalogue ของโรงพยาบาลท่าน");
            return null;
        }
        try (InputStream inputFileStream = file.getInputstream()) {
            final List<HospitalDrugExcelModel> allRowModels = new ArrayList<>();
            originalFileName = file.getFileName();
            saveFileName = UUID.randomUUID().toString() + "-" + file.getFileName();
            targetFile = new File(uploadtempFileDir, saveFileName);
            LOG.debug("save target file to = {}", targetFile.getAbsolutePath());
            Files.asByteSink(targetFile).writeFrom(inputFileStream);
            shaHex = DigestUtils.shaHex(targetFile);
            duplicateFile = uploadHospitalDrugRepo.countByShaHexAndHcode(shaHex, hcodeFromFile) > 0;
            ReaderUtils.read(targetFile, HospitalDrugExcelModel.class, new ReadCallback<HospitalDrugExcelModel>() {
                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, HospitalDrugExcelModel bean) {
                    bean.setRowNum(rowNum + 1);
                    bean.setHcode(hcodeFromFile);
                    bean.postProcessing();
                    allRowModels.add(bean);
                }

                @Override
                public void err(Exception e) {
                    if (e instanceof ColumnNotFoundException) {
                        FacesMessageUtils.error("ไม่พบ column => " + Joiner.on(",").join(((ColumnNotFoundException) e).getColumnNames()));
                    }
                    LOG.error(null, e);
                }

            });
            processValidate(allRowModels, models, notPassModels);
        } catch (ColumnNotFoundException columnNotFound) {
            reset();
            FacesMessageUtils.error("ไม่พบ column => " + Joiner.on(",").join(columnNotFound.getColumnNames()));
        } catch (Exception iOException) {
            reset();
            FacesMessageUtils.error(iOException);
        }
        LOG.debug("File : {}", file);
        return null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = event.getFile();
        upload();
    }

    private void checkDuplicateInCurrentFile(HospitalDrugExcelModel bean) {
        for (HospitalDrugExcelModel model : models) {
            if (bean.isEqual(model)) {
                bean.addError("duplicated", "มีรายการยาของโรงพยาบาลซ้ำอยู่ในแฟ้มข้อมูลเดียวกัน");
            }
        }
    }

    private void checkTmt(HospitalDrugExcelModel bean) {
        if (Strings.isNullOrEmpty(bean.getTmtId()) || bean.getTmtId().length() < 6) {
            return;
        }
        long count = tmtDrugRepo.countByTmtId(bean.getTmtId());
        if (count == 0) {
            bean.addError("tmtId", "ไม่พบ TMTID ตามรหัสยามาตรฐาน TMT");
        }
    }

    private void processValidate(List<HospitalDrugExcelModel> allRowModels, List<HospitalDrugExcelModel> models, List<HospitalDrugExcelModel> notPassModels) {
        for (HospitalDrugExcelModel bean : allRowModels) {
            if (bean.isEmptyRow()) {
                continue;
            }
            Set<ConstraintViolation<HospitalDrugExcelModel>> violations = beanValidator.validate(bean);
            if ("U".equalsIgnoreCase(bean.getUpdateFlag())) {
                violations.addAll(beanValidator.validate(bean, UGroup.class));
            } else if ("E".equalsIgnoreCase(bean.getUpdateFlag()) || "D".equalsIgnoreCase(bean.getUpdateFlag())) {
                violations.addAll(beanValidator.validate(bean, EDGroup.class));
            } else if ("A".equalsIgnoreCase(bean.getUpdateFlag())) {
                violations.addAll(beanValidator.validate(bean, AGroup.class));
            }
            violations.addAll(beanValidator.validate(bean, Lastgroup.class));

            if (violations.isEmpty()) {
                checkDuplicateInCurrentFile(bean);
                checkTmt(bean);
                duplicateCheckFacade.checkDuplicateInDatabase(bean);
                if (bean.getErrorMap().isEmpty()) {
                    models.add(bean);
                } else {
                    notPassModels.add(bean);
                }
            } else {
                bean.addErrors(violations);
                notPassModels.add(bean);
            }
        }
    }

}
