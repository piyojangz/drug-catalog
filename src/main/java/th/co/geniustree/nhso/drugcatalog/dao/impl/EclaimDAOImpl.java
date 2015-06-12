/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.co.geniustree.nhso.drugcatalog.dao.impl;

import th.co.geniustree.nhso.drugcatalog.dao.EclaimDAO;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.eclipse.persistence.jpa.JpaEntityManager;
import org.eclipse.persistence.platform.database.jdbc.JDBCTypes;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLStoredFunctionCall;
import org.eclipse.persistence.platform.database.oracle.plsql.PLSQLrecord;
import org.eclipse.persistence.queries.DataReadQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import th.co.geniustree.nhso.drugcatalog.model.HospitalDrugWithTMT;

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
    public List<HospitalDrugWithTMT> loadDrugInfo(String hospDrugCode, String hcode, String tmtid, Date dateEffective) {

        DataReadQuery databaseQuery = new DataReadQuery();
        PLSQLrecord record = new PLSQLrecord();

        record.setTypeName("HOSPITALDRUG");
        record.setCompatibleType("HOSPITALDRUG");
        record.setJavaType(HospitalDrugWithTMT.class);
        record.addField("tmtid", JDBCTypes.VARCHAR_TYPE);
        record.addField("tmt_type", JDBCTypes.VARCHAR_TYPE);
        record.addField("fsn", JDBCTypes.VARCHAR_TYPE);
        record.addField("manufacturer", JDBCTypes.VARCHAR_TYPE);
        record.addField("hosp_genericName", JDBCTypes.VARCHAR_TYPE);
        record.addField("hosp_tradeName", JDBCTypes.VARCHAR_TYPE);
        record.addField("unit_price", JDBCTypes.NUMERIC_TYPE);
        record.addField("SPECPREP", JDBCTypes.VARCHAR_TYPE);
        record.addField("is_ed", JDBCTypes.VARCHAR_TYPE);
        record.addField("ndc24", JDBCTypes.VARCHAR_TYPE);
        record.addField("deleted", JDBCTypes.VARCHAR_TYPE);
        record.addField("approved", JDBCTypes.VARCHAR_TYPE);
        record.addField("productcat", JDBCTypes.VARCHAR_TYPE);
        record.addField("TMT_DOSAGEFORM", JDBCTypes.VARCHAR_TYPE);
        record.addField("DOSAGEFORM_GROUP", JDBCTypes.VARCHAR_TYPE);
        record.addField("REIMB_UNIT_PRICE", JDBCTypes.NUMERIC_TYPE);
        record.addField("druggroup", JDBCTypes.JAVA_OBJECT_TYPE);
        //TODO Mapping ให้ครบ

        PLSQLStoredFunctionCall call = new PLSQLStoredFunctionCall(record);
        call.addNamedArgument("p_hospdrugcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_hcode", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_tmtid", JDBCTypes.VARCHAR_TYPE);
        call.addNamedArgument("p_date", JDBCTypes.DATE_TYPE);
        call.setProcedureName("HOSPITALDRUG_PACK.find_hospdrug_withtmt");
        JpaEntityManager jem = (JpaEntityManager) em.getDelegate();
        return jem.createQuery(databaseQuery).setParameter("p_hospdrugcode", hospDrugCode).setParameter("p_hcode", hcode).setParameter("p_tmtid", tmtid).setParameter("p_date", new Date()).getResultList();

    }

}
