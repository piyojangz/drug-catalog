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
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        dateEffective = new GregorianCalendar(2014, 9, 9).getTime();
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
        getDrugInfo(drug);
        assertNotNull(drug);
    }

    private void getDrugInfo(HospitalDrugType drug) {
        LOG.info("\n\n********************************DRUG info****************************************");
        LOG.info("TMT id -> " + drug.getTmtid());
        LOG.info("TMT type -> " + drug.getTmt_type());
        LOG.info("FSN -> " + drug.getFsn());
        LOG.info("MANUFACTURER -> " + drug.getManufacturer());
        LOG.info("HOSP GENERIC NAME -> " + drug.getHosp_genericName());
        LOG.info("HOSP TRADE NAME -> " + drug.getHosp_tradeName());
        LOG.info("UNIT PRICE -> " + drug.getUnit_price());
        LOG.info("Unitprice -> " + drug.getUnitprice());
        LOG.info("SPECREP -> " + drug.getSPECPREP());
        LOG.info("ED / NED -> " + drug.getIs_ed());
        LOG.info("NDC24 -> " + drug.getNdc24());
        LOG.info("DELETED -> " + drug.getDeleted());
        LOG.info("APPROVED -> " + drug.getApproved());
        LOG.info("PRODCTCAT -> " + drug.getProductcat());
        LOG.info("TMT DOSAGEFORM -> " + drug.getTMT_DOSAGEFORM());
        LOG.info("DOSAGEFORM GROUP-> " + drug.getDOSAGEFORM_GROUP());
        LOG.info("REIMB UNIT PRICE -> " + drug.getREIMB_UNIT_PRICE());
        LOG.info("content -> " + drug.getContent());
        LOG.info("ISED_STATUS -> " + drug.getISED_STATUS());
        LOG.info("\n*******************************************************************************\n");
    }
}
