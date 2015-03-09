insert into ${schema}s_login_failed  
(
	userlogin,
	login_date,
	login_time,
	remote_addr,
	context
)
values 
(
	${fld:userlogin},
	{d '${def:date}'},
	'${def:time}',
	'${def:remoteaddr}',
	'${def:alias}'
)
