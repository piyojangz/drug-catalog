/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.pl;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import oracle.sql.STRUCT;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredFunctionCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLrecord;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.dao.EclaimDAO;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType;

/**
 *
 * @author pramoth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-persistence.xml"})
@Transactional(propagation = Propagation.REQUIRED)
public class PLMappingIT {

    @PersistenceContext
    private EntityManager em;
    
    private static final Logger LOG = LoggerFactory.getLogger(PLMappingIT.class);
    
    private final String hospDrugCode;
    private final String hcode;
    private final String tmtid;
    private final Date dateEffective;

    @Autowired
    private EclaimDAO eclaimDAO;

    public PLMappingIT() {
        hospDrugCode = "1530046";
        hcode = "10835";
        tmtid = "556824";
        dateEffective = new Date(2014, 9, 9);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCallPl() {
        HospitalDrugType drug = eclaimDAO.loadDrugInfo(hospDrugCode, hcode, tmtid, dateEffective);
//        LOG.info("TMT id -> " + drug.getTmtid());
//        LOG.info("TMT type -> " + drug.getTmt_type());
//        LOG.info("FSN -> " + drug.getFsn());
//        LOG.info("MANUFACTURER -> " + drug.getManufacturer());
//        LOG.info("HOSP GENERIC NAME -> " + drug.getHosp_genericName());
//        LOG.info("HOSP TRADE NAME -> " + drug.getHosp_tradeName());
//        LOG.info("UNIT PRICE -> " + drug.getUnit_price());
//        LOG.info("SPECREP -> " + drug.getSPECPREP());
//        LOG.info("ED / NED -> " + drug.getIs_ed());
//        LOG.info("NDC24 -> " + drug.getNdc24());
//        LOG.info("DELETED -> " + drug.getDeleted());
//        LOG.info("APPROVED -> " + drug.getApproved());
//        LOG.info("PRODCTCAT -> " + drug.getProductcat());
//        LOG.info("TMT DOSAGEFORM -> " + drug.getTMT_DOSAGEFORM());
//        LOG.info("DOSAGEFORM GROUP-> " + drug.getDOSAGEFORM_GROUP());
//        LOG.info("REIMB UNIT PRICE -> " + drug.getREIMB_UNIT_PRICE());
        assertNotNull(drug);
    }

    @Test
    @Ignore
    public void testCallPl2() {

        PLSQLrecord record = new PLSQLrecord();

        record.setTypeName("HOSPITALDRUG");
        record.setCompatibleType("HOSPITALDRUG");
        record.setJavaType(HospitalDrugType.class);
        record.addField("tmtid", JDBCTypes.VARCHAR_TYPE);
        record.addField("tmt_type", JDBCTypes.VARCHAR_TYPE);
        record.addField("fsn", JDBCTypes.VARCHAR_TYPE);
        record.addField("manufacturer", JDBCTypes.VARCHAR_TYPE);
        record.addField("hosp_genericName", JDBCTypes.VARCHAR_TYPE);
        record.addField("hosp_tradeName", JDBCTypes.VARCHAR_TYPE);
        record.addField("unit_price", JDBCTypes.NUMERIC_TYPE);
        record.addField("unitprice", JDBCTypes.NUMERIC_TYPE);
        record.addField("SPECPREP", JDBCTypes.VARCHAR_TYPE);
        record.addField("is_ed", JDBCTypes.VARCHAR_TYPE);
        record.addField("ndc24", JDBCTypes.VARCHAR_TYPE);
        record.addField("deleted", JDBCTypes.VARCHAR_TYPE);
        record.addField("approved", JDBCTypes.VARCHAR_TYPE);
        record.addField("productcat", JDBCTypes.VARCHAR_TYPE);
        record.addField("TMT_DOSAGEFORM", JDBCTypes.VARCHAR_TYPE);
        record.addField("DOSAGEFORM_GROUP", JDBCTypes.VARCHAR_TYPE);
        record.addField("REIMB_UNIT_PRICE", JDBCTypes.NUMERIC_TYPE);
        record.addField("drggroup", JDBCTypes.ARRAY_TYPE);
        record.addField("content", JDBCTypes.VARCHAR_TYPE);
        record.addField("ISED_STATUS", JDBCTypes.VARCHAR_TYPE);

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall(record);
        call.addNamedArgument("p_hospdrugcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_hcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_tmtid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_date", JDBCTypes.DATE_TYPE);
        call.setProcedureName("HOSPITALDRUG_PACK.find_hospdrug_withtmt");
        DataReadQuery databaseQuery = new DataReadQuery(call);
        JpaEntityManager jem = (JpaEntityManager) em.getDelegate();
        DatabaseRecord result = (DatabaseRecord) jem.createQuery(databaseQuery)
                .setParameter("p_hospdrugcode", hospDrugCode)
                .setParameter("p_hcode", hcode)
                .setParameter("p_tmtid", tmtid)
                .setParameter("p_date", dateEffective).getSingleResult();
        System.out.println("--------->" + ToStringBuilder.reflectionToString(result));
        oracle.sql.STRUCT drug = (oracle.sql.STRUCT) result.get("RESULT");
        System.out.println("--------->" + drug.debugString());
//        System.out.println("rain : " + drug.get);
        assertNotNull(mappedToModel(drug));
        

    }

    private HospitalDrugType mappedToModel(STRUCT struct) {
        HospitalDrugType drug = new HospitalDrugType();
        try {
            Object[] objs = struct.getAttributes();
            
            drug.setTmtid((String) objs[0]);
            drug.setTmt_type((String) objs[1]);
            drug.setFsn((String) objs[2]);
            drug.setManufacturer((String) objs[3]);
            drug.setHosp_genericName((String) objs[4]);
            drug.setHosp_tradeName((String) objs[5]);
            drug.setUnit_price((BigDecimal) objs[6]);
            drug.setUnitprice((BigDecimal) objs[7]);
            drug.setSPECPREP((String) objs[8]);
            drug.setIs_ed((String) objs[9]);
            drug.setNdc24((String) objs[10]);
            drug.setDeleted((String) objs[11]);
            drug.setApproved((String) objs[12]);
            drug.setProductcat((String) objs[13]);
            drug.setTMT_DOSAGEFORM((String) objs[14]);
            drug.setDOSAGEFORM_GROUP((String) objs[15]);
            drug.setREIMB_UNIT_PRICE((BigDecimal) objs[16]);
            drug.setDrggroup((Array) objs[17]);
            drug.setContent((String) objs[18]);
            drug.setISED_STATUS((Array) objs[19]);
        } catch (SQLException sqlEx) {
            System.out.println(sqlEx.getMessage());
            return null;
        }
        return drug;
    }
}
