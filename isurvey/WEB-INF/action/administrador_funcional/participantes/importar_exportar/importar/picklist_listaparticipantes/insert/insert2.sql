INSERT INTO ajvieira_isurvey_app.int_lista_participantes_estudio
(id_lista_participantes, id_estudio)
VALUES
(LAST_INSERT_ID(), ${fld:id_estudio})