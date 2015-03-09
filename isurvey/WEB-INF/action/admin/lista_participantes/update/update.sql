UPDATE ajvieira_isurvey_app.lista_participantes SET
	nombre_lista_participantes=${fld:nombre_lista_participantes},
	tipo=${fld:tipo}

WHERE
	id_lista_participantes=${fld:id}

