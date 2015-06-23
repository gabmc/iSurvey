INSERT INTO ajvieira_isurvey_app.lista_participantes
(nombre_lista_participantes, id_empresa)
VALUES
(${fld:nombre_lista_participantes}, (select id_empresa from ajvieira_isurvey_security.s_user where userlogin='${def:user}'))
