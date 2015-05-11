insert into ajvieira_isurvey_app.int_lista_participantes_estudio
	(id_lista_participantes, id_estudio)
	values(
		{{id_lista}}, (select max(id_estudio) from ajvieira_isurvey_app.estudio))