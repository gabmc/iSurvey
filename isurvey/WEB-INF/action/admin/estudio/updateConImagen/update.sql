UPDATE ajvieira_isurvey_app.estudio SET
	nombre_estudio=${fld:nombre_estudio},
	tipo=${fld:tipo},
	id_empresa=${fld:id_empresa},
	banner=replace( ${fld:file}, '/images/banners_estudios/', '')
	titulo_email=${fld:titulo_email},
	cuerpo_email=${fld:cuerpo_email2},
	titulo_mail_recordatorio=${fld:titulo_mail_recordatorio},
	cuerpo_mail_recordatorio=${fld:cuerpo_mail_recordatorio2}

WHERE
	id_estudio=${fld:id}

