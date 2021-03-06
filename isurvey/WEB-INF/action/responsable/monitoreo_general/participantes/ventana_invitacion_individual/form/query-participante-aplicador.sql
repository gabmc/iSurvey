select
	distinct participante.id_participante, participante.nombre_participante, participante.apellido_participante,
	int_participante_instrumento.token_participante, estudio.id_estudio, estudio.titulo_email, estudio.cuerpo_email, 
	case estudio.tipo
		when 'Cerrado' then concat('http://localhost/isurvey/action/estudio/cerrado2/form?id=',estudio.id_estudio,'&token=', int_participante_instrumento.token_participante)
		when 'Abierto-Anonimo' then concat('http://localhost/isurvey/action/estudio/abierto/anonimo/form?id=', instrumento.id_instrumento)
        when 'Abierto-Identificado' then concat('http://localhost/isurvey/action/estudio/abierto/identificado/form?id=', instrumento.id_instrumento)
        end as link
from
	ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes,
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio,
	ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.int_participante_instrumento,
	ajvieira_isurvey_app.instrumento
where
	int_lista_participantes_estudio.id_estudio = estudio.id_estudio
	and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes
	and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes
	and int_participante_lista_participantes.id_participante = participante.id_participante
	and participante.id_empresa = ${fld:id_empresa}
	and estudio.id_estudio = ${fld:id_estudio}
	and int_participante_instrumento.id_participante = participante.id_participante
	and instrumento.id_instrumento = int_participante_instrumento.id_instrumento
	and instrumento.id_estudio = estudio.id_estudio
	and participante.id_participante = ${fld:id_participante}
order by
	id_participante