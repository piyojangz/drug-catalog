/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import th.co.geniustree.nhso.drugcatalog.controller.utils.FacesMessageUtils;
import th.co.geniustree.nhso.drugcatalog.input.DrugAndGroup;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItem;
import th.co.geniustree.nhso.drugcatalog.model.TMTDrugGroupItemPK;
import th.co.geniustree.nhso.drugcatalog.repo.TMTDrugGroupItemRepo;
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
    @Autowired
    private TMTDrugGroupItemRepo tMTDrugGroupItemRepo;

    private StreamedContent templateFile;

    @PostConstruct
    public void postConstruct() {
        String uploadtempLocation = app.getProperty("uploadtempLocation");
        uploadtempFileDir = new File(uploadtempLocation, "DRUGGROUP");
        if (!uploadtempFileDir.exists()) {
            uploadtempFileDir.mkdirs();
        }
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/files/druggroup_template.xlsx");
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
                        processValidate(bean);
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

    private List<DrugAndGroup> splitDrugGroup(DrugAndGroup bean) {
        List<String> drugGroups;
        if (bean.getDrugGroup().split(",") == null) {
            drugGroups = Arrays.asList(bean.getDrugGroup());
        } else {
            drugGroups = Arrays.asList(bean.getDrugGroup().split(","));
        }
        List<DrugAndGroup> groups = new LinkedList<>();
        for (String drugGroupStr : drugGroups) {
            DrugAndGroup _group = new DrugAndGroup();
            BeanUtils.copyProperties(bean, _group);
            _group.setDrugGroup(drugGroupStr);
            if (_group.getDateout() != null && _group.getDateout().equals("")) {
                _group.setDateout(null);
            }
            groups.add(_group);
        }
        return groups;
    }

    private void processValidate(DrugAndGroup bean) {
        if (passModels.contains(bean)) {
            bean.addError("rowNum", "ซ้ำกับรายการยาที่ผ่านการตรวจสอบ");
            return;
        }
        List<DrugAndGroup> groups = splitDrugGroup(bean);
        Map<String, String> errors = new HashMap<>();
        for (DrugAndGroup group : groups) {
            if (drugGroupService.findOne(group.getDrugGroup().trim()) == null) {
                if (errors.get("drugGroup") == null) {
                    errors.put("drugGroup", "ไม่พบ Drug group : " + group.getDrugGroup().trim());
                } else {
                    errors.replace("drugGroup", errors.get("drugGroup") + ", " + group.getDrugGroup().trim());
                }
            }
            if (tmtDrugService.findOneWithoutTx(group.getTmtId()) == null) {
                if (errors.get("tmtId") == null) {
                    errors.put("tmtId", "ไม่พบรายการยามาตรฐาน TMT นี้");
                }
            }
            TMTDrugGroupItem findOne = tMTDrugGroupItemRepo.findOne(new TMTDrugGroupItemPK(group.getTmtId(), group.getDrugGroup(), group.getDateInDate()));
            if (findOne != null && group.getDateOutDate() == null) {
                if (errors.get("rowNum") == null) {
                    errors.put("rowNum", "มีการเพิ่ม Drug group นี้แล้ว : " + group.getDrugGroup().trim());
                } else {
                    errors.replace("rowNum", errors.get("drugGroup") + ", " + group.getDrugGroup().trim());
                }
            }
            for (String key : errors.keySet()) {
                bean.addError(key, errors.get(key));
            }
        }
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
        List<DrugAndGroup> passList = new LinkedList<>();
        for (DrugAndGroup group : passModels) {
            passList.addAll(splitDrugGroup(group));
        }
        tmtDrugGroupItemService.save(passList);
        FacesMessageUtils.info("บันทึกเสร็จสิ้น");
        reset();
    }

    public void reset() {
        notPassModels.clear();
        passModels.clear();
        originalFileName = null;
    }
}
