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
import th.co.geniustree.nhso.drugcatalog.input.DrugAndGroup;
import th.co.geniustree.nhso.drugcatalog.service.DrugGroupService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugGroupItemService;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class UploadMappedDrugGroup implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadMappedDrugGroup.class);

    private UploadedFile file;
    private List<DrugAndGroup> notPassModels = new ArrayList<DrugAndGroup>();
    private List<DrugAndGroup> passModels = new ArrayList<DrugAndGroup>();
    private boolean duplicateFile = false;
    private String originalFileName;
    @Autowired
    @Qualifier("app")
    private Properties app;
    private File uploadtempFileDir;
    private String saveFileName;
    private File targetFile;
    @Autowired
    private Validator beanValidator;
    @Autowired
    private DrugGroupService drugGroupService;
    @Autowired
    private TMTDrugGroupItemService tmtDrugGroupItemService;
    @Autowired
    private TMTDrugService tmtDrugService;
    private StreamedContent templateFile;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "DRUGGROUP");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/files/druggroup_template.xlsx");
        templateFile = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "druggroup_template.xlsx");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<DrugAndGroup> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<DrugAndGroup> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public List<DrugAndGroup> getPassModels() {
        return passModels;
    }

    public void setPassModels(List<DrugAndGroup> passModels) {
        this.passModels = passModels;
    }

    public StreamedContent getTemplateFile() {
        return templateFile;
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
            ReaderUtils.read(targetFile, DrugAndGroup.class, new ReadCallback<DrugAndGroup>() {

                @Override
                public void header(List headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, DrugAndGroup bean) {
                    bean.setRowNum(rowNum);
                    bean.addErrors(beanValidator.validate(bean));
                    bean.postConstruct();
                    if (bean.getErrorMap().isEmpty()) {
                        if (drugGroupService.findOne(bean.getDrugGroup()) == null) {
                            bean.addError("drugGroup", "ไม่พบ Drug group");
                        } else if (tmtDrugService.findOneWithoutTx(bean.getTmtId()) == null) {
                            bean.addError("tmtId", "ไม่พบ  TMT DRUG");
                        }else{
                            tmtDrugGroupItemService.validate(bean);
                        }
                    }
                    if (bean.getErrorMap().isEmpty()) {
                        passModels.add(bean);
                    } else {
                        notPassModels.add(bean);
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
        tmtDrugGroupItemService.save(passModels);
        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        reset();
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
    }
}
