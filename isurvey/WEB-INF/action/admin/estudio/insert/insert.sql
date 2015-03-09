INSERT INTO ajvieira_isurvey_app.estudio
(
	id_estudio,
	nombre_estudio,
	id_empresa
)
VALUES
(
	${seq:nextval@ajvieira_isurvey_app.seq_estudio},
	${fld:nombre_estudio},
	${fld:id_empresa}
)
