INSERT INTO ajvieira_isurvey_app.lista_participantes
(
	id_lista_participantes,
	nombre_lista_participantes,
	tipo
)
VALUES
(
	${seq:nextval@ajvieira_isurvey_app.seq_lista_participantes},
	${fld:nombre_lista_participantes},
	${fld:tipo}
)
