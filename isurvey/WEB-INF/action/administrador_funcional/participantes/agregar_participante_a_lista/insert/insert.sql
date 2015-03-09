insert into int_participante_lista_participantes
(
	id_int_participante_lista_participantes,
        id_participante,
        id_lista_participantes
)
values
(
        NEXTVAL('seq_int_participante_lista_participantes'),
        {{id_participante}},
        {{id_lista_participantes}}
)
