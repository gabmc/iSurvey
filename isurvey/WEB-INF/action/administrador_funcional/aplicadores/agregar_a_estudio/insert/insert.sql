INSERT INTO ajvieira_isurvey_app.int_estudio_usuario
(
	id_int_estudio_usuario,
	id_usuario,
	id_estudio
)
VALUES
(
	${seq:nextval@ajvieira_isurvey_app.seq_int_estudio_usuario},
	${fld:id_usuario},
	${fld:id_estudio}
)
