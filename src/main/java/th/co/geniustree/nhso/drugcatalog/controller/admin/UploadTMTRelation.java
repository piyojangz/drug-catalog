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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
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
import th.co.geniustree.nhso.drugcatalog.controller.utils.FSNSplitter;
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

    private boolean isPassModelContain(TMTParentChild bean) {
        return passModels.contains(bean);
    }

    private boolean isParentAndChildMatched(TMTParentChild bean) {
        return tmtRelationService.isRelationExist(bean.getParent().getTmtId(), bean.getChild().getTmtId());
    }

    private void processValidate(TMTParentChild bean) {
        if (isNotSixNumberTMTID(bean)) {
            bean.addError("TMTID", "TMTID ที่ใส่มาไม่ถูกต้อง");
            return;
        }

        bean.setParent(tmtDrugService.findOneWithoutTx(bean.getParentTmtId()));
        bean.setChild(tmtDrugService.findOneWithoutTx(bean.getChildTmtId()));

        if (isNullTMT(bean.getParent())) {
            bean.addError("TMTID_PARENT", "ไม่พบ TMTID นี้ในระบบ");
            return;
        } else if (isNullTMT(bean.getChild())) {
            bean.addError("TMTID_CHILD", "ไม่พบ TMTID นี้ในระบบ");
            return;
        }

        if (isTypeRelated(bean)) {
            bean.addError("rowNum", "TMT ไม่สัมพันธ์กัน");
        }
        if (isPassModelContain(bean)) {
            bean.addError("rowNum", "ข้อมูลซ้ำกันในไฟล์");
        }
        if (isParentAndChildMatched(bean)) {
            bean.addError("rowNum", "TMT ที่ระบุมีการเชื่อมโยงอยู่แล้ว");
        }
    }

    private boolean hasError(TMTParentChild bean) {
        return !bean.getErrorMap().isEmpty();
    }

    private boolean isNotSixNumberTMTID(TMTParentChild bean) {
        return !(bean.getParentTmtId().matches("\\d{6}") && bean.getChildTmtId().matches("\\d{6}"));
    }

    private boolean isNullTMT(TMTDrug tmt) {
        return tmt == null;
    }

    public boolean isAccordActiveIngredient(TMTParentChild bean) {
        FSNSplitter splitter = new FSNSplitter();
        splitter.getActiveIngredientAndStrengthFromFSN(bean.getParent());
        Set<String> activeIngredients = splitter.getOnlyActiveIngredients();
        boolean accord = isFSNContain(bean.getChild().getFsn(), activeIngredients);
        if (accord) {
            return true;
        } else {
            splitter.getActiveIngredientAndStrengthFromFSN(bean.getChild());
            activeIngredients = splitter.getOnlyActiveIngredients();
            return isFSNContain(bean.getParent().getFsn(), activeIngredients);
        }
    }

    private boolean isFSNContain(String fsn, Set<String> activeIngredients) {
        LOG.debug("FSN : {}", fsn);
        LOG.debug("ActiveIngredient : {}", activeIngredients);
        for (String activeIngredient : activeIngredients) {
            boolean accord = fsn.trim().toLowerCase().contains(activeIngredient.trim().toLowerCase());
            if (accord) {
                return true;
            }
        }
        return false;
    }

    private boolean isTypeRelated(TMTParentChild bean) {
        if (bean.getParent().getType().equals(TMTDrug.Type.SUB)) {
            return bean.getChild().getType().equals(TMTDrug.Type.VTM);
        } else if (bean.getParent().getType().equals(TMTDrug.Type.VTM)) {
            return bean.getChild().getType().equals(TMTDrug.Type.GP);
        } else if (bean.getParent().getType().equals(TMTDrug.Type.GP)) {
            return bean.getChild().getType().equals(TMTDrug.Type.TP) || bean.getChild().getType().equals(TMTDrug.Type.GPU);
        } else { //if(bean.getChild().getType().equals(TMTDrug.Type.TP) || bean.getChild().getType().equals(TMTDrug.Type.GPU)){
            return bean.getChild().getType().equals(TMTDrug.Type.TPU);
        }
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void save() {
        List<TMTRelation> passRelations = convertUploadModelToTMTRelation(passModels);
        tmtRelationService.saveAll(passRelations);
        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        reset();
    }

    private List<TMTRelation> convertUploadModelToTMTRelation(List<TMTParentChild> beans) {
        List<TMTRelation> relationList = new LinkedList<TMTRelation>();
        for (TMTParentChild bean : beans) {
            TMTRelation relation = new TMTRelation(bean.getParent(), bean.getChild());
            relationList.add(relation);
        }
        return relationList;
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
    }
}
