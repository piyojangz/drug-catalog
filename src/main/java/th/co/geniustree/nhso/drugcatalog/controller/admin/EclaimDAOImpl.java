/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredProcedureCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLrecord;
import org.eclipse.persistence.queries.DataReadQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Thanthathon
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class EclaimDAOImpl implements EclaimDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public HospitalDrugWithTMT loadDrugInfo(String hospDrugCode, String hcode, String tmtid, Date dateEffective) {

        DataReadQuery databaseQuery = new DataReadQuery();
        PLSQLrecord record = new PLSQLrecord();

        record.setTypeName("HOSPITALDRUG_PACK.hospitaldrug");
        record.setCompatibleType("DRUGCODEOWNER.HOSPITALDRUG");
        record.setJavaType(HospitalDrugWithTMT.class);
        record.addField("p_hospdrugcode", JDBCTypes.VARCHAR_TYPE);
        record.addField("p_hcode", JDBCTypes.VARCHAR_TYPE);
        record.addField("p_tmtid", JDBCTypes.NUMERIC_TYPE);
        record.addField("p_date", JDBCTypes.DATE_TYPE);
        
        PLSQLStoredProcedureCall call = new PLSQLStoredProcedureCall();
        call.setProcedureName("HOSPITALDRUG_PACK.find_hospdrug_withtmt");
//        call.addNamedArgument("p_hospdrugcode", "p_hospdrugcode", String.class);
//        call.addNamedArgument("p_hcode", "p_hcode", String.class);
//        call.addNamedArgument("p_tmtid", "p_tmtid", String.class);
//        call.addNamedArgument("p_date", "p_date", Date.class);
        call.addNamedArgumentValue("p_hospdrugcode", hospDrugCode);
        call.addNamedArgumentValue("p_hcode", hcode);
        call.addNamedArgumentValue("p_tmtid", tmtid);
        call.addNamedArgumentValue("p_date", dateEffective);
        
        databaseQuery.addArgument("p_hospdrugcode", String.class);
        databaseQuery.addArgument("p_hcode", String.class);
        databaseQuery.addArgument("p_tmtid", String.class);
        databaseQuery.addArgument("p_date", Date.class);
//        List list = new ArrayList();
//        list.add(hospDrugCode);
//        list.add(hcode);
//        list.add(tmtid);
//        list.add(dateEffective);
//        databaseQuery.addArgumentValue(list);
//        databaseQuery.addArgumentValue(hospDrugCode);
//        databaseQuery.addArgumentValue(hcode);
//        databaseQuery.addArgumentValue(tmtid);
//        databaseQuery.addArgumentValue(dateEffective);
        databaseQuery.setCall(call);

        JpaEntityManager jem = (JpaEntityManager) em.getDelegate();
//        Query query = (Query) jem.getActiveSession().executeQuery(databaseQuery, list);
        jem.getActiveSession().executeNonSelectingCall(call);
//        return (HospitalDrugWithTMT) query.getSingleResult();
        return null;
    }
}
