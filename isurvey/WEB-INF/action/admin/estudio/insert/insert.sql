INSERT INTO ajvieira_isurvey_app.estudio
(
	nombre_estudio,
	id_empresa,
	tipo,
	banner
)
VALUES
(
	${fld:nombre_estudio},
	${fld:id_empresa},
	${fld:tipo},
	replace( ${fld:file}, '/images/banners_estudios/', '')
)
