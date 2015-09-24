UPDATE ajvieira_isurvey_app.participante SET
	nombre_participante=${fld:nombre_participante},
	apellido_participante=${fld:apellido_participante},
	email_participante=${fld:email_participante},
	cargo=${fld:cargo},
	empresa=${fld:empresa},
	supervisor=${fld:supervisor},
	fecha_nacimiento=${fld:fecha_nacimiento},
	fecha_ingreso=${fld:fecha_ingreso},
	sexo=${fld:sexo},
	tipo_nomina=${fld:tipo_nomina},
	funcion=${fld:funcion},
	id_participante=${fld:id_participante},
	area=${fld:area},
	telefono=${fld:telefono}

WHERE
	id_participante=${fld:id}

