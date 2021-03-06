/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import th.co.geniustree.nhso.drugcatalog.authen.Role;
import th.co.geniustree.nhso.drugcatalog.authen.SecurityUtil;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.ExcelTMTEdNed;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;
import th.co.geniustree.nhso.drugcatalog.service.TMTEdNedService;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class UploadTMTEdNed implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadTMTEdNed.class);

    private UploadedFile file;
    private List<ExcelTMTEdNed> notPassModels = new ArrayList<ExcelTMTEdNed>();
    private List<ExcelTMTEdNed> passModels = new ArrayList<ExcelTMTEdNed>();
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
    private TMTDrugService tmtDrugService;
    @Autowired
    private TMTEdNedService tmtEdNedService;
    private StreamedContent templateFile;

    @PostConstruct
    public void postConstruct() {
        if(SecurityUtil.getUserDetails().getAuthorities().contains(Role.HOSPITAL)){
            try {
                LOG.warn("No permission to use this function");
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException ex) {
                
            }
        }
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "TMTEDNED");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/files/edned_template.xlsx");
        templateFile = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "edned_template.xlsx");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<ExcelTMTEdNed> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<ExcelTMTEdNed> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public List<ExcelTMTEdNed> getPassModels() {
        return passModels;
    }

    public void setPassModels(List<ExcelTMTEdNed> passModels) {
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
            ReaderUtils.read(targetFile, ExcelTMTEdNed.class, new ReadCallback<ExcelTMTEdNed>() {

                @Override
                public void header(List headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, ExcelTMTEdNed bean) {
                    bean.setRowNum(rowNum);
                    bean.addErrors(beanValidator.validate(bean));
                    bean.postConstruct();
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

    private void processValidate(ExcelTMTEdNed bean) {
        if (isDuplicateInFile(bean)) {
            bean.addError("rowNum", "พบข้อมูลซ้ำกันในไฟล์");
        }
        TMTDrug findOneWithoutTx = tmtDrugService.findOneWithoutTx(bean.getTmtId().trim());
        if (findOneWithoutTx == null) {
            bean.addError("tmtId", "ไม่พบ TMTID นี้ในระบบ");
        }
        if (isExistED(bean.getTmtId(), bean.getDateIn())) {
            bean.addError("dateinString", "ED/NED ในวันที่เดียวกันนี้เคยระบุไปแล้ว");
        }
    }

    private boolean isDuplicateInFile(ExcelTMTEdNed bean) {
        return passModels.contains(bean);
    }

    private boolean hasError(ExcelTMTEdNed bean) {
        return !bean.getErrorMap().isEmpty();
    }

    private boolean isExistED(String tmtId, Date dateIn) {
        return tmtEdNedService.exist(tmtId, dateIn);
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
        try {
            tmtEdNedService.save(passModels);
            FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        } catch (Exception e) {
            FacesMessageUtils.error("ไม่สามารถบันทึกได้ อาจเป็นเพราะมีข้อมูลซ้ำกัน");
            LOG.error(null, e);
        } finally {
            reset();
        }
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
    }
}
