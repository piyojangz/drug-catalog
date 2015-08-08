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
import java.util.ArrayList;
import java.util.Date;
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
import th.co.geniustree.nhso.drugcatalog.model.DosageFormGroup;
import th.co.geniustree.nhso.drugcatalog.service.DosageFormGroupService;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class UploadDosageFormGroup {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadDosageFormGroup.class);

    @Autowired
    @Qualifier("app")
    private Properties app;
    
    @Autowired
    private Validator beanValidator;
    
    @Autowired
    private DosageFormGroupService dosageFormGroupService;
    
    private UploadedFile file;
    private List<DosageFormGroup> notPassModels = new ArrayList<>();
    private List<DosageFormGroup> passModels = new ArrayList<>();
    private List<DosageFormGroup> dosageFormGroups = new ArrayList<>();
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

    public List<DosageFormGroup> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<DosageFormGroup> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public List<DosageFormGroup> getPassModels() {
        return passModels;
    }

    public void setPassModels(List<DosageFormGroup> passModels) {
        this.passModels = passModels;
    }

    public StreamedContent getTemplateFile() {
        return templateFile;
    }

    public List<DosageFormGroup> getDosageFormGroups() {
        return dosageFormGroups;
    }

    public void setDosageFormGroups(List<DosageFormGroup> dosageFormGroups) {
        this.dosageFormGroups = dosageFormGroups;
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
            ReaderUtils.read(targetFile, DosageFormGroup.class, new ReadCallback<DosageFormGroup>() {

                @Override
                public void header(List headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, DosageFormGroup bean) {
                    bean.setCreateDate(new Date());
                    dosageFormGroups.add(bean);
                    LOG.debug("id : {} \t\t\t desc : {}",bean.getIdGroup(),bean.getDescription());
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
        save();
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
        dosageFormGroupService.saveAll(dosageFormGroups);
//        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
//        reset();
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
        dosageFormGroups.clear();
    }
}
