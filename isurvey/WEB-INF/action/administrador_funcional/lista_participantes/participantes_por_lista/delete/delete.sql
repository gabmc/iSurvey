delete from ajvieira_isurvey_app.int_participante_lista_participantes
where id_participante = {{id_participante}}
and id_lista_participantes = {{id_lista}}
and id_empresa_participante in (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '{{userlogin}}')