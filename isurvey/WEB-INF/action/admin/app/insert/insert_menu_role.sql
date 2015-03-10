insert into ${schema}s_menu_role
(menu_id, role_id)
values
(${fld:insert_menu.sql.lastid}, ${fld:insert_role.sql.lastid});
