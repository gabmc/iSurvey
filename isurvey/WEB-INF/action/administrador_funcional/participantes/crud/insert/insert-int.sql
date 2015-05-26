INSERT INTO ajvieira_isurvey_app.int_participante_lista_participantes
(id_participante, id_empresa_participante, id_lista_participantes)
VALUES
({{id_participante}}, (select id_empresa from ajvieira_isurvey_security.s_user where userlogin='{{userlogin}}'), {{id_lista_participantes}})
