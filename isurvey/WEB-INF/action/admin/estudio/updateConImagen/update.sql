UPDATE ajvieira_isurvey_app.estudio SET
	nombre_estudio=${fld:nombre_estudio},
	id_empresa=${fld:id_empresa},
	banner=replace( ${fld:file}, '/images/banners_estudios/', ''),
	titulo_email=${fld:titulo_email},
	cuerpo_email=${fld:cuerpo_email2},
	titulo_mail_recordatorio=${fld:titulo_email_recordatorio},
	cuerpo_mail_recordatorio=${fld:cuerpo_email_recordatorio2}
	estatus=${fld:estatus}

WHERE
	id_estudio=${fld:id}

