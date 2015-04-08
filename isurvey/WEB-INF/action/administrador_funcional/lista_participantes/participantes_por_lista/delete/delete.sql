delete from ajvieira_isurvey_app.int_participante_lista_participantes
where id_participante = ${fld:id_participante}
and id_lista_participantes = ${fld:id_lista}
and id_empresa_participante in (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')