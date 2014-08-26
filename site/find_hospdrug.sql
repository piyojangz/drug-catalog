create or replace type druggroup
as table of varchar2(20);
-----------------------------------
create or replace TYPE                 "HOSPITALDRUG" as object 
( 
    tmtid varchar2(6),
    tmt_type varchar2(6),
    fsn nvarchar2(1000),
    manufacturer nvarchar2(255),
    unit_price number(10,2),
    SPECPREP varchar(3),
    is_ed varchar2(2),
    ndc24 varchar2(24),
    drggroup druggroup
);
-----------------------------------
create or replace package hospitaldrug_pack as

function find_hospdrug(p_hospdrugcode varchar2, p_hcode varchar2,p_date date) return hospitaldrug;

end hospitaldrug_pack;
-----------------------------------
create or replace package body hospitaldrug_pack as

function find_hospdrug(p_hospdrugcode varchar2, p_hcode varchar2,p_date date) return hospitaldrug as

    v_hdrug hospitaldrug := hospitaldrug(null, null, null, null, null, null, null,null, druggroup());
    v_unit_price number;
    v_status_ed varchar2(2);
    
    begin
        for rcd in (select drg.tmtid,
                           drg.type tmt_type,
                           drg.fsn,
                           hdrg.manufacturer,
                           hdrg.specprep,
                           hdrg.ised is_ed,
                           hdrg.ndc24 ndc24
                           
                    from tmt_hospdrug hdrg 
                    inner join tmt_drug drg
                    on (hdrg.tmtid = drg.tmtid)
      
                    where hdrg.HCODE = p_hcode and hdrg.HOSPDRUGCODE = p_hospdrugcode
        ) loop
        
            -- find unit price
            begin
                  select price
                  into v_unit_price
                  from(
                      select *
                      from tmt_hospprice hprice
                      where hprice.hcode = p_hcode 
                            and hprice.HOSPDRUGCODE = p_hospdrugcode 
                            and trunc(hprice.DATE_EFFECTIVE) <= p_date
                      order by hprice.DATE_EFFECTIVE desc, hprice.PRICE desc
                  )
                  where rownum = 1;
              exception
                  when others then
                      v_unit_price := null;
            end;
  
  
            -- find 'status_ed' from tmt_tmtedned
            begin
                  select status_ed
                  into v_status_ed
                  from(
                    select *
                    from tmt_tmtedned ed
                    where ed.tmtid = rcd.tmtid and trunc(ed.datein) <= p_date
                    order by ed.DATEIN desc
                  )
                  where rownum = 1;
              exception
                  when others then
                      v_status_ed := null;
            end;  
    
            -- find 'status_ed' from tmt_edned
            if v_status_ed is null then
                begin
                        select status_ed
                        into v_status_ed
                        from(
                          select *
                          from tmt_edned ed
                          where ed.hospdrugcode = p_hospdrugcode and trunc(ed.datein) <= p_date
                          order by ed.DATEIN desc
                        )
                        where rownum = 1;
                    exception
                      when others then
                          v_status_ed := null;
                end;  
            end if;
    
            v_hdrug.tmtid := rcd.tmtid;
            v_hdrug.tmt_type := rcd.tmt_type;
            v_hdrug.fsn := rcd.fsn;
            v_hdrug.manufacturer := rcd.manufacturer;
            v_hdrug.unit_price := v_unit_price;
            v_hdrug.specprep := rcd.specprep;
            v_hdrug.is_ed := v_status_ed;
            v_hdrug.ndc24 := rcd.ndc24;
        
            -- find group
            for rs in(select druggroup_name
                      from TMT_DRUGGROUPITEM item
                      where tmtid = rcd.tmtid 
                      and p_date between trunc(item.datein) and trunc(nvl(item.dateout, sysdate))
            ) loop
                v_hdrug.drggroup.extend();
                v_hdrug.drggroup(v_hdrug.drggroup.last) := rs.druggroup_name;
            end loop;
        
        end loop;
        
        return v_hdrug;
        
    end find_hospdrug;

end hospitaldrug_pack;