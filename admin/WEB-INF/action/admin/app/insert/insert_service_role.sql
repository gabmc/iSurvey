insert into ${schema}s_service_role (service_id, role_id)
select service_id, ${fld:insert_role.sql.lastid} from ${schema}s_service where app_id = ${fld:insert.sql.lastid}
;