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
import org.eclipse.persistence.internal.helper.DatabaseType;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredFunctionCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredProcedureCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLrecord;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    public PLMappingIT() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    @Ignore
    public void testCallPl() {

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
        record.addField("approve", JDBCTypes.VARCHAR_TYPE);
        record.addField("product_cat", JDBCTypes.VARCHAR_TYPE);
        record.addField("dosage_form", JDBCTypes.VARCHAR_TYPE);
        record.addField("dosage_form_group", JDBCTypes.VARCHAR_TYPE);
        record.addField("reimburse_unit_price", JDBCTypes.NUMERIC_TYPE);
        record.addField("druggroup", JDBCTypes.ARRAY_TYPE);
        //TODO Mapping ให้ครบ

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall(record);
        call.addNamedArgument("p_hospdrugcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_hcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_tmtid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_date", JDBCTypes.DATE_TYPE);
        call.setProcedureName("HOSPITALDRUG_PACK.find_hospdrug_withtmt");
        DataReadQuery databaseQuery = new DataReadQuery(call);
        JpaEntityManager jem = (JpaEntityManager) em.getDelegate();
        jem.createQuery(databaseQuery).setParameter("p_hospdrugcode", "1EXEL3").setParameter("p_hcode", "13756").setParameter("p_tmtid", "").setParameter("p_date", new Date()).getResultList();
    }
}
