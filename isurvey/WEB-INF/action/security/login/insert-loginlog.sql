insert into ${schema}s_loginlog  
(
	login_date,
	login_time,
	remote_addr,
	context,
	user_id
)
values 
(
	{d '${def:date}'},
	'${def:time}',
	'${def:remoteaddr}',
	'${def:alias}',
	${fld:user_id}
)
