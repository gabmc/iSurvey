select	
	*
from ${schema}s_user
where user_id = ${fld:id} and
	  is_ldap = 1