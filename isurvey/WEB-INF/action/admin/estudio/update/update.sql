UPDATE ajvieira_isurvey_app.estudio SET
	nombre_estudio='{{nombre_estudio}}',
	tipo='{{tipo}}',
	id_empresa={{id_empresa}},
	titulo_email='{{titulo_email}}',
	cuerpo_email='{{cuerpo_email}}'

WHERE
	id_estudio={{id_estudio}}

