INSERT INTO ajvieira_isurvey_app.empresa
(
	id_empresa,
	id_sector_empresarial,
	nombre_empresa,
	telefono,
	tipo,
	rif
)
VALUES
(
	${seq:nextval@ajvieira_isurvey_app.seq_empresa},
	${fld:id_sector_empresarial},
	${fld:nombre_empresa},
	${fld:telefono},
	${fld:tipo},
	${fld:rif}
)
