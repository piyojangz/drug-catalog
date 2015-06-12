/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.pl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.persistence.internal.helper.DatabaseType;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredFunctionCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredProcedureCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLrecord;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
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

    @Autowired
    private EclaimDAO eclaimDAO;
    public PLMappingIT() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test 
    public void testCallPl() {
        th.co.geniustree.nhso.drugcatalog.model.HospitalDrugType drug = eclaimDAO.loadDrugInfo("1480055", "10919", "", new Date());
        assertNotNull(drug);
    }

    @Test
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
        DatabaseRecord result = (DatabaseRecord)jem.createQuery(databaseQuery)
                .setParameter("p_hospdrugcode", "1480055")
                .setParameter("p_hcode", "10919")
                .setParameter("p_tmtid", "")
                .setParameter("p_date", new Date()).getSingleResult();
        System.out.println("--------->"+ToStringBuilder.reflectionToString(result));
        oracle.sql.STRUCT drug = (oracle.sql.STRUCT) result.get("RESULT");
        System.out.println("--------->"+drug.debugString());
        assertNotNull(drug);
        
    }
}
