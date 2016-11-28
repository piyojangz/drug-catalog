create or replace PROCEDURE              "INITIAL_HOSPITAL_DRUG" is

  -- Created on 26/12/2014 by CHIROD.N 

  cursor initial_cur is
    select u.hcode, t.*, r.approvedate, r.status, r.rowid
      from TMT_UPLOADHOSPDRUG u
      join TMT_UPLOADHOSPDRUG_ITEM t
        on (u.id = t.uploadhospdrug_id)
      join TMT_REQUEST_ITEM r
        on (r.id = t.id)
     where r.status = 'ACCEPT'
          --and r.deleted = 0
       and not exists (select s.uploadhospdrug_item_id
              from TMT_HOSPDRUG_TRANS s
             where s.uploadhospdrug_item_id = t.id)
     order by u.hcode, u.createdate, t.hospdrugcode, t.dateeffectivedate;

  initial_rec initial_cur%rowtype := null;

  v_ised_approved  TMT_HOSPDRUG_TRANS.ised_approved%type := null;
  v_ised_status    TMT_HOSPDRUG_TRANS.ised_status%type := null;
  v_ndc24_approved TMT_HOSPDRUG_TRANS.ndc24_approved%type := null;
  d_date_ed_ned    TMT_HOSPDRUG_TRANS.date_approved%type := null;

  cnt_hospdrug_trans number(3) := 0;

begin

  --- เริ่มประมวลผลข้อมูล
  open initial_cur;

  loop
  
    initial_rec := null;
  
    fetch initial_cur
      into initial_rec;
    exit when initial_cur%notfound;
    
  cnt_hospdrug_trans := 0;
    begin
      select count(*)
        into cnt_hospdrug_trans
        from TMT_HOSPDRUG_TRANS s
       where s.uploadhospdrug_item_id = initial_rec.id;
    exception
      when others then
        null;
    end;
    
    if cnt_hospdrug_trans = 0 then 
      --- สำหรับ เติม 25 fields
      --- หา รหัสยามาตรฐาน 24 หลัก
      v_ndc24_approved := null;
      begin
        select b.ndc24
          into v_ndc24_approved
          from (select m.ndc24
                  from TMT_TMTDRUG_NDC24 m
                 where m.tmtid = initial_rec.tmtid
                 order by m.ndc24) b
         where rownum = 1;
      exception
        when others then
          null;
      end;
    
      --- หา ED / NED
      v_ised_approved := null;
      v_ised_status   := null;
      d_date_ed_ned   := trunc(sysdate); --- วันที่ตรวจสอบ ED / NED ใช้วันที่ Download 
      
/*        begin
          select a.status_ed
            into v_ised_approved
            from (select e.status_ed
                    from TMT_TMTEDNED e
                   where e.tmtid = initial_rec.tmtid
                     and e.datein <= initial_rec.approvedate
                   order by e.datein desc) a
           where rownum = 1;
        exception
          when others then
            null;
        end;
    
      if v_ised_approved is not null then
        v_ised_status := '01';
      else
        v_ised_approved := initial_rec.ised;
        v_ised_status   := '99';
      end if;*/
      --- สำหรับ เติม 25 fields
    
      begin
        insert into TMT_HOSPDRUG_TRANS
          (hospdrugcode,
           hcode,
           approved,
           content,
           datechange,
           dateeffective,
           dateupdate,
           deleted,
           dfscode,
           distributor,
           dosageform,
           genericname,
           ised,
           manufacturer,
           ndc24,
           packprice,
           packsize,
           productcat,
           specprep,
           strength,
           tmtid,
           tradename,
           unitprice,
           updateflag,
           version,
           uploadhospdrug_id,
           uploadhospdrug_item_id,
           process_date,
           ised_approved,
           ndc24_approved,
           date_approved,
           ised_status)
        values
          (initial_rec.hospdrugcode,
           initial_rec.hcode,
           1,
           initial_rec.content,
           initial_rec.datechangedate,
           initial_rec.dateeffectivedate,
           initial_rec.dateupdatedate,
           0,
           initial_rec.dfscode,
           initial_rec.distributor,
           initial_rec.dosageform,
           initial_rec.genericname,
           initial_rec.ised,
           initial_rec.manufacturer,
           initial_rec.ndc24,
           initial_rec.packprice,
           initial_rec.packsize,
           initial_rec.productcat,
           initial_rec.specprep,
           initial_rec.strength,
           initial_rec.tmtid,
           initial_rec.tradename,
           to_number(initial_rec.unitprice),
           initial_rec.updateflag,
           0,
           initial_rec.uploadhospdrug_id,
           initial_rec.id,
           sysdate,
           null,
           v_ndc24_approved,
           sysdate,
           null);
           
      exception
        when others then
        rollback;
          null;
      end;
   end if;
  end loop;

  close initial_cur;

end;