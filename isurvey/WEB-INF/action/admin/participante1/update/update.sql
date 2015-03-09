UPDATE ajvieira_isurvey_app.participante SET
	id_lista_participantes=${fld:id_lista_participantes},
	nombre_participante=${fld:nombre_participante},
	apellido_participante=${fld:apellido_participante},
	cedula_participante=${fld:cedula_participante},
	email_participante=${fld:email_participante},
	cargo=${fld:cargo},
	supervisor=${fld:supervisor},
	fecha_nacimiento=${fld:fecha_nacimiento},
	fecha_ingreso=${fld:fecha_ingreso},
	sexo=${fld:sexo},
	tipo_nomina=${fld:tipo_nomina},
	funcion=${fld:funcion}

WHERE
	id_participante=${fld:id}

