INSERT INTO ${schema}s_user 
	(userlogin, 
	passwd, 
	lname, 
	fname, 
	email, 
	enabled, 
	pwd_policy, 
	force_newpass, 
	locale,
	id_empresa) 
SELECT 
${fld:userlogin}, 
${fld:passwd}, 
${fld:lname}, 
${fld:fname}, 
${fld:email}, 
1,
${fld:pwd_policy},
${fld:force_newpass}, 
${fld:locale}, 
id_empresa 
from ${schema}s_user where userlogin = '${def:user}'
