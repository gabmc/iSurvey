update ${schema}s_user set 

	lname = ${fld:lname},
	fname = ${fld:fname},
	email = ${fld:email},
	locale = ${fld:locale}

where
	user_id = ${fld:user_id}
