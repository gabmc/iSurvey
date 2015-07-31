UPDATE ajvieira_isurvey_app.estudio SET
	nombre_estudio=${fld:nombre_estudio},
	tipo=${fld:tipo},
	id_empresa=${fld:id_empresa},
	banner=replace( ${fld:file}, '/images/banners_estudios/', '')
	titulo_email=${fld:titulo_email},
	cuerpo_email=${fld:cuerpo_email}

WHERE
	id_estudio=${fld:id}

