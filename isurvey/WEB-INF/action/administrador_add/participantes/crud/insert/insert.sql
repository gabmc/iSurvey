INSERT INTO ajvieira_isurvey_app.participante
(
	id_participante,
	nombre_participante,
	apellido_participante,
	email_participante,
	telefono,
	empresa,
	area,
	cargo,
	supervisor,
	fecha_nacimiento,
	fecha_ingreso,
	sexo,
	tipo_nomina,
	funcion,
	id_empresa,
	fecha_registro,
	verificado,
	fecha_verificacion
)
VALUES
(
	${fld:id_participante},
	${fld:nombre_participante},
	${fld:apellido_participante},
	${fld:email_participante},
	${fld:telefono},
	${fld:empresa},
	${fld:area},
	${fld:cargo},
	${fld:supervisor},
	${fld:fecha_nacimiento},
	${fld:fecha_ingreso},
	${fld:sexo},
	${fld:tipo_nomina},
	${fld:funcion},
	${fld:id_empresa},
	now(),
	'Si',
	now()
)
