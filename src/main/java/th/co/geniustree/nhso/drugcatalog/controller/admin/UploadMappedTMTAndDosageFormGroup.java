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
import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.DrugAndDosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.service.DosageFormGroupService;
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
public class UploadMappedTMTAndDosageFormGroup implements Serializable{

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadMappedTMTAndDosageFormGroup.class);

    @Autowired
    @Qualifier("app")
    private Properties app;

    @Autowired
    private Validator beanValidator;

    @Autowired
    private DosageFormGroupService dosageFormGroupService;

    @Autowired
    private TMTDrugService tmtDrugService;

    private UploadedFile file;
    private List<DrugAndDosageFormGroup> notPassModels = new ArrayList<>();
    private List<DrugAndDosageFormGroup> passModels = new ArrayList<>();
    private List<DrugAndDosageFormGroup> tmtDosageformGroups = new ArrayList<>();
    private boolean duplicateFile = false;
    private String originalFileName;

    private File uploadtempFileDir;
    private String saveFileName;
    private File targetFile;

    private StreamedContent templateFile;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "DOSAGEFORMGROUP");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/files/dosageformgroup_template.xlsx");
        templateFile = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "dosageformgroup_template.xlsx");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<DrugAndDosageFormGroup> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<DrugAndDosageFormGroup> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public List<DrugAndDosageFormGroup> getPassModels() {
        return passModels;
    }

    public void setPassModels(List<DrugAndDosageFormGroup> passModels) {
        this.passModels = passModels;
    }

    public StreamedContent getTemplateFile() {
        return templateFile;
    }

    public List<DrugAndDosageFormGroup> getTmtDosageformGroups() {
        return tmtDosageformGroups;
    }

    public void setTmtDosageformGroups(List<DrugAndDosageFormGroup> tmtDosageformGroups) {
        this.tmtDosageformGroups = tmtDosageformGroups;
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
            ReaderUtils.read(targetFile, DrugAndDosageFormGroup.class, new ReadCallback<DrugAndDosageFormGroup>() {

                @Override
                public void header(List headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, DrugAndDosageFormGroup bean) {
                    bean.setRowNum(rowNum);
                    bean.addErrors(beanValidator.validate(bean));
                    if (bean.getTmtid().matches("\\d{6}")) {
                        if (bean.getDosageFormGroup() != null && !bean.getDosageFormGroup().isEmpty()) {
                            if (bean.getDosageFormGroup().matches("[a-z][a-z0-9]*")) {
                                DosageFormGroup group = dosageFormGroupService.findById(bean.getDosageFormGroup());
                                if (group != null) {
                                    passModels.add(bean);
                                } else {
                                    bean.addError("dosageFormGroup", "ไม่พบ Dosage Form Group");
                                    notPassModels.add(bean);
                                }
                            } else {
                                bean.addError("dosageFormGroup", "รูปแบบของ DOSAGE_FORM_GROUP ไม่ถูกต้อง");
                                notPassModels.add(bean);
                            }
                        } else {
                            bean.addError("dosageFormGroup", "กรุณากรอก DOSAGE_FORM_GROUP");
                            notPassModels.add(bean);
                        }
                    } else {
                        bean.addError("tmtid", "รูปแบบของ TMTID ไม่ถูกต้อง");
                        notPassModels.add(bean);
                    }

                    tmtDosageformGroups.add(bean);
                    LOG.debug("tmtid : {}\t\t dosageFormGroup : {}", bean.getTmtid(), bean.getDosageFormGroup());
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
        tmtDrugService.uploadEditDosageFormGroup(passModels);
        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        reset();
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
        tmtDosageformGroups.clear();
    }
}
