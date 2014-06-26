/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.hospital.controller;

import com.google.common.io.Files;
import java.io.File;
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
import org.primefaces.model.UploadedFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.HospitalDrugExcelModel;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrug;
import th.co.geniustree.nhso.drugcatalog.model.UploadHospitalDrugItem;
import th.co.geniustree.nhso.drugcatalog.repo.UploadHospitalDrugRepo;
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
    private UploadHospitalDrugRepo uploadHospitalDrugRepo;
    private String saveFileName;
    private String originalFileName;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation);
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
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

    public String save() {
        List<UploadHospitalDrugItem> items = new ArrayList<>();
        UploadHospitalDrug uploadDrug = new UploadHospitalDrug();
        for (HospitalDrugExcelModel passModel : models) {
            UploadHospitalDrugItem item = new UploadHospitalDrugItem();
            BeanUtils.copyProperties(passModel, item);
            item.setUploadDrug(uploadDrug);
            items.add(item);
        }
        uploadDrug.setPassItems(items);
        uploadDrug.setOriginalFilename(originalFileName);
        uploadDrug.setSavedFileName(saveFileName);
        uploadDrug.setItemCount(models.size() + notPassModels.size());
        uploadDrug.setPassItemCount(models.size());
        uploadHospitalDrugRepo.save(uploadDrug);
        FacesMessageUtils.info("Save completed.");
        reset();
        return null;
    }

    public String reset() {
        saveFileName = null;
        originalFileName = null;
        models.clear();
        notPassModels.clear();
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
        try (InputStream inputFileStream = file.getInputstream()) {
            originalFileName = file.getFileName();
            saveFileName = UUID.randomUUID().toString() + "-" + file.getFileName();
            File targetFile = new File(uploadtempFileDir, saveFileName);
            LOG.debug("save target file to = {}" + targetFile.getAbsolutePath());
            Files.asByteSink(targetFile).writeFrom(inputFileStream);

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
                    Set<ConstraintViolation<HospitalDrugExcelModel>> violations = beanValidator.validate(bean);
                    if (violations.isEmpty()) {
                        models.add(bean);
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
            FacesMessageUtils.error(columnNotFound);
        } catch (Exception iOException) {
            throw new RuntimeException(iOException);
        }
        LOG.debug(
                "File : {}", file);

        return null;
    }
}
