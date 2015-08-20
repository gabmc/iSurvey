INSERT INTO ajvieira_isurvey_app.int_participante_instrumento
(
	token_participante,
	id_participante,
	id_instrumento,
	estatus,
	porcentaje_completado
)
values
(
	'{{token_participante}}',
	{{id_participante}},
	{{id_instrumento}},
	'Sin Iniciar',
	0
)