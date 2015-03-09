INSERT INTO ${schema}s_session
(
	jsessionid,
	login_date,
	login_time,
	remote_addr,
	context_alias,
	userlogin
)
VALUES
(
	'${def:session}',
	{d '${def:date}'},
	'${def:time}',
	'${def:remoteaddr}',
	'${def:alias}',
	${fld:userlogin}
)
