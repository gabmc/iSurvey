INSERT INTO ajvieira_isurvey_app.opinion
(
	id_instrumento,
	id_participante,
	tipo,
	texto
)
VALUES
(
	{{id_instrumento}},
	{{id_participante}},
	'{{tipo}}',
	'{{texto}}'
)
