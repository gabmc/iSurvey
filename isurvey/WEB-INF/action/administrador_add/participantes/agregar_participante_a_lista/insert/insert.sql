insert into ajvieira_isurvey_app.int_participante_lista_participantes
(
        id_participante,
        id_lista_participantes,
        id_empresa_participante
)
values
(
        {{id_participante}},
        {{id_lista_participantes}},
        (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '{{userlogin}}')
)
