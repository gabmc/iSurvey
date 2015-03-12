INSERT INTO ajvieira_isurvey_app.instrumento
(
	id_instrumento,
	id_estudio,
	nombre
)
VALUES
(
	${fld:id_survey},
	${fld:id_estudio},
	${fld:nombre}
)
