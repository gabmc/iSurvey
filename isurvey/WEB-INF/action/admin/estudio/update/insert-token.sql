insert into ajvieira_isurvey_app.int_participante_instrumento
(
        id_participante,
        id_instrumento,
        token_participante,
        estatus
)
values
(
        {{id_participante}},
        {{id_instrumento}},
        '{{token}}',
        'Incompleta'
)
