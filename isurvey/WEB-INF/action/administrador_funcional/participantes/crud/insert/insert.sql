INSERT INTO ajvieira_isurvey_app.participante
(
	id_participante,
	nombre_participante,
	apellido_participante,
	email_participante,
	telefono,
	empresa,
	sector_empresa,
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
SELECT
	${fld:id_participante},
	${fld:nombre_participante},
	${fld:apellido_participante},
	${fld:email_participante},
	${fld:telefono},
	(select nombre_empresa from ajvieira_isurvey_app.empresa where id_empresa = 
		(select id_empresa from ajvieira_isurvey_security.s_user where userlogin='${def:user}')),
	(select nombre_sector from ajvieira_isurvey_app.sector_empresarial where id_sector_empresarial = 
		(select id_sector_empresarial from ajvieira_isurvey_app.empresa where id_empresa = 
			(select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}'))),
	${fld:area},
	${fld:cargo},
	${fld:supervisor},
	${fld:fecha_nacimiento},
	${fld:fecha_ingreso},
	${fld:sexo},
	${fld:tipo_nomina},
	${fld:funcion},
	id_empresa,
	now(),
	'Si',
	now()
FROM
	ajvieira_isurvey_security.s_user where userlogin = '${def:user}'
