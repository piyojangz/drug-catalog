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
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.TMTParentChild;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrug;
import th.co.geniustree.nhso.drugcatalog.model.TMTRelation;
import th.co.geniustree.nhso.drugcatalog.service.TMTDrugService;
import th.co.geniustree.nhso.drugcatalog.service.TMTRelationService;
import th.co.geniustree.xls.beans.ColumnNotFoundException;
import th.co.geniustree.xls.beans.ReadCallback;
import th.co.geniustree.xls.beans.ReaderUtils;

/**
 *
 * @author Thanthathon
 */
@Component
@Scope("view")
public class UploadTMTRelation {

    private static final Logger LOG = LoggerFactory.getLogger(UploadTMTRelation.class);

    private UploadedFile file;
    private List<TMTParentChild> notPassModels = new ArrayList<TMTParentChild>();
    private List<TMTParentChild> passModels = new ArrayList<TMTParentChild>();
    private String originalFileName;
    private List<TMTRelation> passRelations;

    @Autowired
    @Qualifier("app")
    private Properties app;
    @Autowired
    private Validator beanValidator;
    @Autowired
    private TMTRelationService tmtRelationService;
    @Autowired
    private TMTDrugService tmtDrugService;

    private File uploadtempFileDir;
    private String saveFileName;
    private File targetFile;

    private StreamedContent templateFile;

    @PostConstruct
    public void postConstruct() {
        passRelations = new ArrayList<>();
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "TMTRELATION");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/files/tmtrelation_template.xlsx");
        templateFile = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "tmtrelation_template.xlsx");
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<TMTParentChild> getNotPassModels() {
        return notPassModels;
    }

    public void setNotPassModels(List<TMTParentChild> notPassModels) {
        this.notPassModels = notPassModels;
    }

    public List<TMTParentChild> getPassModels() {
        return passModels;
    }

    public void setPassModels(List<TMTParentChild> passModels) {
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
            ReaderUtils.read(targetFile, TMTParentChild.class, new ReadCallback<TMTParentChild>() {

                @Override
                public void header(List headers) {
                    LOG.debug("HEADERS = {}", headers);
                }

                @Override
                public void ok(int rowNum, TMTParentChild bean) {
                    bean.setRowNum(rowNum);
                    bean.addErrors(beanValidator.validate(bean));
                    if (bean.getErrorMap().isEmpty()) {
                        if (bean.getParentTmtId().matches("\\d{6}") && bean.getChildTmtId().matches("\\d{6}")) {
                            TMTDrug parentTMT = tmtDrugService.findOneWithoutTx(bean.getParentTmtId());
                            TMTDrug childTMT = tmtDrugService.findOneWithoutTx(bean.getChildTmtId());
                            if (parentTMT == null) {
                                bean.addError("TMTID_PARENT", "ไม่พบ TMTID นี้ในระบบ");
                            } else {
                                if (childTMT == null) {
                                    bean.addError("TMTID_CHILD", "ไม่พบ TMTID นี้ในระบบ");
                                } else {
                                    boolean exist = tmtRelationService.isRelationExist(parentTMT.getTmtId(), childTMT.getTmtId());
                                    if (exist) {
                                        bean.addError("TMTID", "TMT ที่ระบุมีการเชื่อมโยงอยู่แล้ว");
                                    } else {
                                        TMTRelation r = new TMTRelation(parentTMT, childTMT);
                                        passRelations.add(r);
                                    }
                                }
                            }
                        } else {
                            bean.addError("TMTID", "TMTID ที่ใส่มาไม่ถูกต้อง");
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
            }
            );
        } catch (ColumnNotFoundException columnNotFound) {
            reset();
            FacesMessageUtils.error("ไม่พบ column => " + Joiner.on(",").join(columnNotFound.getColumnNames()));
        } catch (Exception iOException) {
            reset();
            FacesMessageUtils.error(iOException);
        }
        LOG.debug("File : {}", file);
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void save() {
        tmtRelationService.saveAll(passRelations);
        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        reset();
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
    }
}
