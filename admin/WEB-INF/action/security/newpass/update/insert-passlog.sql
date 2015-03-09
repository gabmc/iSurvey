insert into ${schema}s_passlog  
(
	last_change,
	hash,
	user_id
)
values 
(
	{d '${def:date}'},
	${fld:passwd},
	${fld:userid}
)
