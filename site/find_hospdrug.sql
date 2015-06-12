--------------------------------------------------------
--  DDL for Type DRUGATTR
--------------------------------------------------------
drop type "DRUGCODEOWNER"."DRUGATTR" force;
drop type "DRUGCODEOWNER"."HOSPITALDRUG" force;
drop type "DRUGCODEOWNER"."DRUGGROUP" force;

/

  CREATE OR REPLACE TYPE "DRUGCODEOWNER"."DRUGATTR" as object 
(
    manufacturer nvarchar2(255),
    hosp_genericName NVARCHAR2(255),
    hosp_tradeName NVARCHAR2(255),
    SPECPREP varchar(2),
    productcat varchar(3),
    ised varchar(2)
);

/
--------------------------------------------------------
--  DDL for Type DRUGGROUP
--------------------------------------------------------

  CREATE OR REPLACE TYPE "DRUGCODEOWNER"."DRUGGROUP" 
as table of varchar2(20);

/
--------------------------------------------------------
--  DDL for Type HOSPITALDRUG
--------------------------------------------------------

  CREATE OR REPLACE TYPE "DRUGCODEOWNER"."HOSPITALDRUG" as object 
( 
    tmtid varchar2(6),
    tmt_type varchar2(6),
    fsn nvarchar2(1000),
    manufacturer nvarchar2(255),
    hosp_genericName NVARCHAR2(255),
    hosp_tradeName NVARCHAR2(255),
    unit_price number(10,2),
    SPECPREP varchar(2),
    is_ed varchar2(2),
    ndc24 varchar2(24),
    deleted varchar2(1),
    approved varchar2(1),
    productcat varchar(3),
    TMT_DOSAGEFORM varchar(255),
    DOSAGEFORM_GROUP varchar(255),
    REIMB_UNIT_PRICE number(10,2),
    drggroup druggroup
);

/
create or replace PACKAGE "HOSPITALDRUG_PACK"
AS
  FUNCTION find_unit_price(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE)
    RETURN NUMBER;
  FUNCTION find_status_ed(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE,
      v_current_tmt  VARCHAR2)
    RETURN VARCHAR2;
  FUNCTION find_status_ed2(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE)
    RETURN VARCHAR2;
  FUNCTION find_deleted(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE)
    RETURN VARCHAR2;
  FUNCTION find_tmtid_intx(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE)
    RETURN VARCHAR2;
  FUNCTION find_hospdrug(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE)
    RETURN hospitaldrug;
  FUNCTION is_drug_exist(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2)
    RETURN NUMBER;
  FUNCTION find_reimburse_price(
      p_tmtid VARCHAR2,
      p_date  DATE)
    RETURN NUMBER;
  FUNCTION find_drug_attr(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE)
    RETURN DRUGATTR;

      FUNCTION find_hospdrug_withtmt(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_tmtid        VARCHAR2,
      p_date         DATE)
    RETURN hospitaldrug;
    FUNCTION find_tmtid_intx_withtmt(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_tmtid        VARCHAR2,
    p_date         DATE)
  RETURN VARCHAR2;
END hospitaldrug_pack;
/
create or replace PACKAGE BODY hospitaldrug_pack
AS
  ------------------------------------------------------------------------------------------------
FUNCTION find_unit_price(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_date         DATE)
  RETURN NUMBER
AS
  v_unit_price NUMBER;
BEGIN
  SELECT price
  INTO v_unit_price
  FROM
    (SELECT *
    FROM tmt_hospprice hprice
    WHERE hprice.hcode                = p_hcode
    AND hprice.HOSPDRUGCODE           = p_hospdrugcode
    AND TRUNC(hprice.DATE_EFFECTIVE) <= p_date
    ORDER BY hprice.DATE_EFFECTIVE DESC,
      hprice.PRICE DESC
    )
  WHERE rownum = 1;
  RETURN v_unit_price;
EXCEPTION
WHEN OTHERS THEN
  RETURN NULL;
END find_unit_price;
------------------------------------------------------------------------------------------------
FUNCTION find_status_ed(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_date         DATE,
    v_current_tmt  VARCHAR2)
  RETURN VARCHAR2
AS
  v_status_ed VARCHAR2(2);
BEGIN
  SELECT status_ed
  INTO v_status_ed
  FROM
    (SELECT *
    FROM tmt_tmtedned ed
    WHERE ed.tmtid        = v_current_tmt
    AND TRUNC(ed.datein) <= p_date
    ORDER BY ed.DATEIN DESC
    )
  WHERE rownum = 1;
  RETURN v_status_ed;
EXCEPTION
WHEN OTHERS THEN
  RETURN NULL;
END find_status_ed;
------------------------------------------------------------------------------------------------
FUNCTION find_status_ed2(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_date         DATE)
  RETURN VARCHAR2
AS
  v_status_ed VARCHAR2(2);
BEGIN
  SELECT status_ed
  INTO v_status_ed
  FROM
    (SELECT *
    FROM tmt_edned ed
    WHERE ed.hospdrugcode = p_hospdrugcode
    AND ed.HCODE          = p_hcode
    AND TRUNC(ed.datein) <= p_date
    ORDER BY ed.DATEIN DESC
    )
  WHERE rownum = 1;
  RETURN v_status_ed;
EXCEPTION
WHEN OTHERS THEN
  RETURN NULL;
END find_status_ed2;
------------------------------------------------------------------------------------------------
FUNCTION find_deleted(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_date         DATE)
  RETURN VARCHAR2
AS
  v_deleted VARCHAR2(1);
BEGIN
  SELECT
    CASE
      WHEN updateflag = 'D'
      THEN 'Y'
      ELSE 'N'
    END deleted
  INTO v_deleted
  FROM
    (SELECT item.*,
      parent.hcode
    FROM TMT_UPLOADHOSPDRUG_ITEM item
    INNER JOIN TMT_UPLOADHOSPDRUG parent
    ON (item.UPLOADHOSPDRUG_ID                                                       = parent.id)
    inner join TMT_REQUEST_ITEM req
    on item.id=req.id
    WHERE parent.hcode                                                               = p_hcode
    and req.STATUS='ACCEPT'
    AND item.hospdrugcode                                                            = p_hospdrugcode
    AND TRUNC(to_date(TO_CHAR(item.DATEEFFECTIVEDATE, 'DD-MM-YYYY'), 'DD-MM-YYYY')) <= p_date
    ORDER BY item.DATEEFFECTIVEDATE DESC,item.UPDATEFLAG DESC
    )
  WHERE rownum = 1;
  RETURN v_deleted;
EXCEPTION
WHEN OTHERS THEN
  RETURN NULL;
END;
------------------------------------------------------------------------------------------------
FUNCTION find_tmtid_intx(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_date         DATE)
  RETURN VARCHAR2
AS
  v_current_tmt VARCHAR2(6);
BEGIN
  SELECT TMTID
  INTO v_current_tmt
  FROM
    (SELECT *
    FROM TMT_DRUG_TX
    WHERE HCODE                = p_hcode
    AND HOSPDRUGCODE           = p_hospdrugcode
    AND TRUNC(DATE_EFFECTIVE) <= p_date
    ORDER BY DATE_EFFECTIVE DESC
    )
  WHERE rownum = 1;
  RETURN v_current_tmt;
EXCEPTION
WHEN OTHERS THEN
  RETURN NULL;
END find_tmtid_intx;
------------------------------------------------------------------------------------------------
FUNCTION find_hospdrug(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_date         DATE)
  RETURN hospitaldrug
AS
  v_hdrug hospitaldrug          := hospitaldrug(NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,NULL,NULL, NULL,NULL, NULL,NULL, NULL, NULL, druggroup());
  v_unit_price     NUMBER       :=NULL;
  v_status_ed      VARCHAR2(2)  :=NULL;
  v_tmtid          VARCHAR2(6)  :=NULL;
  v_tmt_type       VARCHAR2(6)  :=NULL;
  v_tmt_dosageform VARCHAR2(255):=NULL;
  v_fsn NVARCHAR2(1000)         :=NULL;
  v_deleted         VARCHAR2(1)         :=NULL;
  v_current_tmt     VARCHAR2(6)         :=NULL;
  v_reimburse_price NUMBER;
  v_drugattr DRUGATTR;
BEGIN
  FOR rcd IN
  (SELECT hdrg.tmtid,
    hdrg.ised is_ed,
    CASE
      WHEN hdrg.APPROVED = 0
      THEN 'N'
      ELSE 'Y'
    END APPROVED,
    hdrg.ndc24 ndc24
  FROM tmt_hospdrug hdrg
  WHERE hdrg.HCODE      = p_hcode
  AND hdrg.HOSPDRUGCODE = p_hospdrugcode
  )
  LOOP
    v_deleted     := find_deleted(p_hospdrugcode, p_hcode, p_date);
    v_current_tmt := find_tmtid_intx(p_hospdrugcode, p_hcode, p_date);
    -- find drug
    BEGIN
      SELECT drg.tmtid,
        drg.type tmt_type,
        drg.fsn,
        drg.dosageform
      INTO v_tmtid,
        v_tmt_type,
        v_fsn,
        v_tmt_dosageform
      FROM tmt_drug drg
      WHERE drg.tmtid = v_current_tmt;
    EXCEPTION
    WHEN OTHERS THEN
      v_tmtid          := NULL;
      v_tmt_type       := NULL;
      v_fsn            := NULL;
      v_tmt_dosageform :=NULL;
    END;
    v_unit_price      := find_unit_price(p_hospdrugcode, p_hcode, p_date);
    v_reimburse_price := find_reimburse_price(v_tmtid,p_date);
    v_drugattr := find_drug_attr(p_hospdrugcode, p_hcode, p_date);
    v_status_ed       := find_status_ed(p_hospdrugcode, p_hcode, p_date, v_current_tmt);
    -- find 'status_ed' from tmt_edned
    IF v_status_ed IS NULL THEN
      v_status_ed  := find_status_ed2(p_hospdrugcode, p_hcode, p_date);
    END IF;
    v_hdrug.tmtid            := v_current_tmt;
    v_hdrug.tmt_type         := v_tmt_type;
    v_hdrug.fsn              := v_fsn;
    v_hdrug.manufacturer     := v_drugattr.manufacturer;
    v_hdrug.hosp_genericName := v_drugattr.hosp_genericName;
    v_hdrug.hosp_tradeName   := v_drugattr.hosp_tradeName;
    v_hdrug.unit_price       := v_unit_price;
    v_hdrug.specprep         := v_drugattr.specprep;
    v_hdrug.is_ed            := v_status_ed;
    v_hdrug.ndc24            := rcd.ndc24;
    v_hdrug.deleted          := v_deleted;
    v_hdrug.APPROVED         := rcd.APPROVED;
    v_hdrug.productcat       := v_drugattr.productcat;
    v_hdrug.tmt_dosageform   := v_tmt_dosageform;
    -- find group
    FOR rs IN
    (SELECT druggroup_name
    FROM TMT_DRUGGROUPITEM item
    WHERE tmtid = v_current_tmt
    AND p_date BETWEEN TRUNC(item.datein) AND TRUNC(NVL(item.dateout, sysdate))
    )
    LOOP
      v_hdrug.drggroup.extend();
      v_hdrug.drggroup(v_hdrug.drggroup.last) := rs.druggroup_name;
    END LOOP;
  END LOOP;
  RETURN v_hdrug;
END find_hospdrug;
FUNCTION is_drug_exist(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2)
  RETURN NUMBER
AS
  l_exist NUMBER := 0;
BEGIN
  SELECT
    CASE
      WHEN COUNT(*) > 0
      THEN 1
      ELSE 0
    END exist
  INTO l_exist
  FROM TMT_HOSPDRUG
  WHERE HCODE     =p_hcode
  AND HOSPDRUGCODE=p_hospdrugcode;
  RETURN l_exist;
END is_drug_exist;
------------------------------------------------------------------------------------------------
FUNCTION find_reimburse_price(
    p_tmtid VARCHAR2,
    p_date  DATE)
  RETURN NUMBER
AS
  v_reimburse_price NUMBER;
BEGIN
  SELECT price
  INTO v_reimburse_price
  FROM
    (SELECT *
    FROM tmt_reimburse_price hprice
    WHERE hprice.tmtid                = p_tmtid
    AND TRUNC(hprice.EFFECTIVE_DATE) <= p_date
    ORDER BY hprice.EFFECTIVE_DATE DESC,
      hprice.PRICE DESC
    )
  WHERE rownum = 1;
  RETURN v_reimburse_price;
EXCEPTION
WHEN OTHERS THEN
  RETURN NULL;
END find_reimburse_price;
------------------------------------------------------------------------------------------------
FUNCTION find_drug_attr(
      p_hospdrugcode VARCHAR2,
      p_hcode        VARCHAR2,
      p_date         DATE)
    RETURN DRUGATTR
AS
   v_drug DRUGATTR := DRUGATTR(NULL,NULL,NULL,NULL,NULL,NULL);
BEGIN
  SELECT 
  manufacturer,
  genericName,
  tradeName,
  SPECPREP,
  productcat,
  ised
 INTO 
  v_drug.manufacturer,
    v_drug.hosp_genericName,
    v_drug.hosp_tradeName,
    v_drug.SPECPREP,
    v_drug.productcat,
    v_drug.ised
  FROM
    (SELECT *
    FROM TMT_UPLOADHOSPDRUG_ITEM u join TMT_REQUEST_ITEM r on u.id=r.id
    WHERE r.HCODE                = p_hcode
    AND r.HOSPDRUGCODE           = p_hospdrugcode
    AND u.UPDATEFLAG in ('A','E')
    AND r.STATUS='ACCEPT'
    AND TRUNC(u.DATEEFFECTIVEDATE) <= p_date
    ORDER BY u.DATEEFFECTIVEDATE DESC ,u.UPDATEFLAG DESC
    )
  WHERE rownum = 1;
  RETURN v_drug;
EXCEPTION
WHEN OTHERS THEN
    v_drug.manufacturer := null;
    v_drug.hosp_genericName := null;
    v_drug.hosp_tradeName := null;
    v_drug.SPECPREP := null;
    v_drug.productcat := null;
    v_drug.ised := null;
  RETURN v_drug;
END find_drug_attr;
------------------------------------------------------------------------------------------------
FUNCTION find_hospdrug_withtmt(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_tmtid        VARCHAR2,
    p_date         DATE)
  RETURN hospitaldrug
AS
  v_hdrug hospitaldrug          := hospitaldrug(NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,NULL,NULL, NULL,NULL, NULL,NULL, NULL, NULL, druggroup());
  v_unit_price     NUMBER       :=NULL;
  v_status_ed      VARCHAR2(2)  :=NULL;
  v_tmtid          VARCHAR2(6)  :=NULL;
  v_tmt_type       VARCHAR2(6)  :=NULL;
  v_tmt_dosageform VARCHAR2(255):=NULL;
  v_fsn NVARCHAR2(1000)         :=NULL;
  v_deleted         VARCHAR2(1)         :=NULL;
  v_current_tmt     VARCHAR2(6)         :=NULL;
  v_reimburse_price NUMBER;
  v_drugattr DRUGATTR;
BEGIN
  FOR rcd IN
  (SELECT hdrg.tmtid,
    hdrg.ised is_ed,
    CASE
      WHEN hdrg.APPROVED = 0
      THEN 'N'
      ELSE 'Y'
    END APPROVED,
    hdrg.ndc24 ndc24
  FROM tmt_hospdrug hdrg
  WHERE hdrg.HCODE      = p_hcode
  AND hdrg.HOSPDRUGCODE = p_hospdrugcode
  )
  LOOP
      v_current_tmt := find_tmtid_intx_withtmt(p_hospdrugcode, p_hcode,p_tmtid, p_date);
    -- find drug
    BEGIN
      SELECT drg.tmtid,
        drg.type tmt_type,
        drg.fsn,
        drg.dosageform
      INTO v_tmtid,
        v_tmt_type,
        v_fsn,
        v_tmt_dosageform
      FROM tmt_drug drg
      WHERE drg.tmtid = v_current_tmt;
    EXCEPTION
    WHEN OTHERS THEN
      v_tmtid          := NULL;
      v_tmt_type       := NULL;
      v_fsn            := NULL;
      v_tmt_dosageform :=NULL;
    END;
    v_deleted     := find_deleted(p_hospdrugcode, p_hcode, p_date);
    v_unit_price      := find_unit_price(p_hospdrugcode, p_hcode, p_date);
    v_reimburse_price := find_reimburse_price(v_tmtid,p_date);
    v_drugattr := find_drug_attr(p_hospdrugcode, p_hcode, p_date);
    /**
    temporary comment out will use this later.
    currently we use ised from hospital input.
    v_status_ed       := find_status_ed(p_hospdrugcode, p_hcode, p_date, v_current_tmt);
    -- find 'status_ed' from tmt_edned
    IF v_status_ed IS NULL THEN
      v_status_ed  := find_status_ed2(p_hospdrugcode, p_hcode, p_date);
    END IF;
    **/
    v_hdrug.tmtid            := v_current_tmt;
    v_hdrug.tmt_type         := v_tmt_type;
    v_hdrug.fsn              := v_fsn;
    v_hdrug.manufacturer     := v_drugattr.manufacturer;
    v_hdrug.hosp_genericName := v_drugattr.hosp_genericName;
    v_hdrug.hosp_tradeName   := v_drugattr.hosp_tradeName;
    v_hdrug.unit_price       := v_unit_price;
    v_hdrug.specprep         := v_drugattr.specprep;
    --v_hdrug.is_ed            := v_status_ed;
    v_hdrug.is_ed            := v_drugattr.ised;
    v_hdrug.ndc24            := rcd.ndc24;
    v_hdrug.deleted          := v_deleted;
    v_hdrug.APPROVED         := rcd.APPROVED;
    v_hdrug.productcat       := v_drugattr.productcat;
    v_hdrug.tmt_dosageform   := v_tmt_dosageform;
    -- find group
    FOR rs IN
    (SELECT druggroup_name
    FROM TMT_DRUGGROUPITEM item
    WHERE tmtid = v_current_tmt
    AND p_date BETWEEN TRUNC(item.datein) AND TRUNC(NVL(item.dateout, sysdate))
    )
    LOOP
      v_hdrug.drggroup.extend();
      v_hdrug.drggroup(v_hdrug.drggroup.last) := rs.druggroup_name;
    END LOOP;
  END LOOP;
  RETURN v_hdrug;
END find_hospdrug_withtmt;
------------------------------------------------------------------------------------------------
FUNCTION find_tmtid_intx_withtmt(
    p_hospdrugcode VARCHAR2,
    p_hcode        VARCHAR2,
    p_tmtid        VARCHAR2,
    p_date         DATE)
  RETURN VARCHAR2
AS
  v_current_tmt VARCHAR2(6);
BEGIN
  SELECT TMTID
  INTO v_current_tmt
  FROM
    (SELECT *
    FROM TMT_DRUG_TX
    WHERE HCODE                = p_hcode
    AND HOSPDRUGCODE           = p_hospdrugcode
    AND TMTID                  = p_tmtid
    AND TRUNC(DATE_EFFECTIVE) <= p_date
    ORDER BY DATE_EFFECTIVE DESC
    )
  WHERE rownum = 1;
  RETURN v_current_tmt;
EXCEPTION
WHEN OTHERS THEN
  RETURN NULL;
END find_tmtid_intx_withtmt;
------------------------------------------------------------------------------------------------
END hospitaldrug_pack;
/