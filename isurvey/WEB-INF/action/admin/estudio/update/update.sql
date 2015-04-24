UPDATE ajvieira_isurvey_app.estudio SET
	nombre_estudio=${fld:nombre_estudio},
	tipo=${fld:tipo},
	id_empresa=${fld:id_empresa}

WHERE
	id_estudio=${fld:id}

