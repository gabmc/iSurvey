insert into ${schema}s_user_role
(
	user_id,
	role_id
)
values 
(
	${fld:insert.sql.lastid},
	${fld:role_id}
)
