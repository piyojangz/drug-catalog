/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.hospital;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.DigestUtils;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.authen.WSUserDetails;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.controller.utils.UploadItemOrderHelper;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
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
    private WSUserDetails user;
    private String shaHex;
    @Autowired
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    private boolean duplicateFile;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation);
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        user = SecurityUtil.getUserDetails();
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
        List<UploadHospitalDrugItem> items = new ArrayList<>();
        UploadHospitalDrug uploadDrug = new UploadHospitalDrug();
        uploadDrug.setShaHex(shaHex);
        uploadDrug.setHcode(user.getOrgId());
        for (HospitalDrugExcelModel passModel : models) {
            UploadHospitalDrugItem item = new UploadHospitalDrugItem();
            BeanUtils.copyProperties(passModel, item);
            item.setUploadDrug(uploadDrug);
            items.add(item);
        }
        UploadItemOrderHelper.reorderByUpdateFlageAFirstDeleteLast(items);
        uploadDrug.setPassItems(items);
        uploadDrug.setOriginalFilename(originalFileName);
        uploadDrug.setSavedFileName(saveFileName);
        uploadDrug.setItemCount(models.size() + notPassModels.size());
        uploadDrug.setPassItemCount(models.size());
        uploadHospitalDrugService.saveUploadHospitalDrugAndRequest(uploadDrug);
        FacesMessageUtils.info("Save completed.");
        reset();
        return null;
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
        String nameWithoutExtension = Files.getNameWithoutExtension(file.getFileName());
        if (nameWithoutExtension.length() < 5) {
            FacesMessageUtils.info("File name must have HCODE 5 Digit.");
            return null;
        }
        String hcode = file.getFileName().substring(0, 5);
        if (!hcode.equalsIgnoreCase(user.getOrgId())) {
            FacesMessageUtils.error("Upload HCODE must match with login HCODE");
            return null;
        }

        try (InputStream inputFileStream = file.getInputstream()) {
            originalFileName = file.getFileName();
            saveFileName = UUID.randomUUID().toString() + "-" + file.getFileName();
            File targetFile = new File(uploadtempFileDir, saveFileName);
            LOG.debug("save target file to = {}" + targetFile.getAbsolutePath());
            Files.asByteSink(targetFile).writeFrom(inputFileStream);
            shaHex = DigestUtils.shaHex(targetFile);
            duplicateFile = uploadHospitalDrugRepo.countByShaHex(shaHex) > 0;
            ReaderUtils.read(targetFile, HospitalDrugExcelModel.class, new ReadCallback<HospitalDrugExcelModel>() {
                int rowNum = 0;

                @Override
                public void header(List<String> headers) {
                    LOG.debug("HEADERS = {}", headers);
                    rowNum++;
                }

                @Override
                public void ok(int rowNum, HospitalDrugExcelModel bean) {
                    bean.setRowNum(rowNum);
                    bean.setHcode(user.getOrgId());
                    Set<ConstraintViolation<HospitalDrugExcelModel>> violations = beanValidator.validate(bean);
                    if (violations.isEmpty()) {
                        checkDuplicateInCurrentFile(bean);
                        duplicateCheckFacade.checkDuplicateInDatabase(bean);
                        if (bean.getErrorMap().isEmpty()) {
                            models.add(bean);
                        } else {
                            notPassModels.add(bean);
                        }
                    } else {
                        for (ConstraintViolation<HospitalDrugExcelModel> violation : violations) {
                            bean.addError(violation.getPropertyPath().toString(), violation.getMessage());
                        }
                        notPassModels.add(bean);
                    }
                }

                @Override
                public void err(Exception e) {
                    LOG.error(null, e);
                }
            });
        } catch (ColumnNotFoundException columnNotFound) {
            reset();
            FacesMessageUtils.error(columnNotFound);
        } catch (Exception iOException) {
            reset();
            throw new RuntimeException(iOException);
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
                bean.addError("duplicated", "duplicated entry in current file.");
            }
        }
    }
}
