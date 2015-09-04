UPDATE ajvieira_isurvey_app.estudio SET
	nombre_estudio='{{nombre_estudio}}',
	id_empresa={{id_empresa}},
	titulo_email='{{titulo_email}}',
	cuerpo_email='{{cuerpo_email}}',
	titulo_mail_recordatorio='{{titulo_mail_recordatorio}}',
	cuerpo_mail_recordatorio='{{cuerpo_mail_recordatorio}}',
	estatus='{{estatus}}'

WHERE
	id_estudio={{id_estudio}}

