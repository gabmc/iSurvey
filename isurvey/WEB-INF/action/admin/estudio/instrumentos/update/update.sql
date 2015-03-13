UPDATE ajvieira_isurvey_app.instrumento SET
	ajvieira_isurvey_app.instrumento.id_estudio=${fld:id_estudio},
	ajvieira_isurvey_app.instrumento.nombre=${fld:nombre},
	ajvieira_isurvey_app.instrumento.id_instrumento=${fld:id}

WHERE
	ajvieira_isurvey_app.instrumento.id_instrumento=${fld:id}

