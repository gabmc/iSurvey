INSERT INTO ajvieira_isurvey_app.int_participante_lista_participantes
(
	id_participante,
	id_empresa_participante,
	id_lista_participantes
)
values
(
	${fld:id_participante},
	${fld:id_empresa},
	(select id_lista_participantes from ajvieira_isurvey_app.int_lista_participantes_estudio
where id_estudio = (select id_estudio from ajvieira_isurvey_app.instrumento where id_instrumento = ${fld:id_instrumento}))	
)