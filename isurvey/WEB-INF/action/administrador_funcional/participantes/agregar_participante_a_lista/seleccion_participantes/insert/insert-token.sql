insert into ajvieira_isurvey_app.int_participante_instrumento
(
        id_participante,
        id_instrumento,
        token_participante,
        estatus,
        porcentaje
)
values
(
        {{id_participante}},
        {{id_instrumento}},
        '{{token}}',
        'Sin Iniciar',
        0
)
