select	
	instrumento.*, participante.id_participante
from 
	ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes, 
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio,
	ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento
where 
	participante.id_participante = ${fld:id_participante}
and estudio.id_estudio = ${fld:id_estudio}
and int_participante_lista_participantes.id_participante = participante.id_participante
and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes
and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes
and int_lista_participantes_estudio.id_estudio = estudio.id_estudio
and estudio.id_empresa = participante.id_empresa