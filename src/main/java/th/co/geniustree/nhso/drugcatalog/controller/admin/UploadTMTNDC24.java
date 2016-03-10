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
import th.co.geniustree.nhso.drugcatalog.model.TmtNDC24;
import th.co.geniustree.nhso.drugcatalog.repo.TmtNDC24Repo;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author moth
 */
@Component
@Scope("view")
public class UploadTMTNDC24 implements Serializable {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(UploadTMTNDC24.class);

    private UploadedFile file;
    private List<TmtNDC24> notPassModels = new ArrayList<TmtNDC24>();
    private List<TmtNDC24> passModels = new ArrayList<TmtNDC24>();
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
    private TmtNDC24Repo tmtNDC24Repo;
    private StreamedContent templateFile;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "TMTNDC24");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/files/tmtndc24_template.xlsx");
        templateFile = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "tmtndc24_template.xlsx");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<TmtNDC24> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<TmtNDC24> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public List<TmtNDC24> getPassModels() {
        return passModels;
    }

    public void setPassModels(List<TmtNDC24> passModels) {
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
            ReaderUtils.read(targetFile, TmtNDC24.class, new ReadCallback<TmtNDC24>() {

                @Override
                public void header(List headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, TmtNDC24 bean) {
                    bean.setRowNum(rowNum);
                    bean.addErrors(beanValidator.validate(bean));

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

    private void processValidate(TmtNDC24 bean) {
        TmtNDC24 tmtNdc24 = tmtNDC24Repo.findOne(bean.getTmtId());
        if (tmtNdc24 == null) {
            bean.addError("tmtId", "ไม่พบ TMTID นี้ในระบบ");
            return;
        } else if (tmtNdc24.getNdc24().equals(bean.getNdc24())) {
            bean.addError("ndc24", "มีการระบุ NDC24 แล้ว");
        }

        if (isDuplicateDataInFile(bean)) {
            bean.addError("rowNum", "ซ้ำกับรายการอื่นในไฟล์");
        }
    }

    private boolean isDuplicateDataInFile(TmtNDC24 bean) {
        return passModels.contains(bean);
    }

    private boolean hasError(TmtNDC24 bean) {
        return !bean.getErrorMap().isEmpty();
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
        tmtNDC24Repo.save(passModels);
        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        reset();
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
    }
}
