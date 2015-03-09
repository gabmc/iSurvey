select
	role_id as id,
	rolename,
	description
from
	${schema}s_role
where
	role_id = ${fld:role_id}