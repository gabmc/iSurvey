select
	ajvieira_isurvey_app.participante.*
from
	ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes
where
	ajvieira_isurvey_app.int_participante_lista_participantes.id_participante = ajvieira_isurvey_app.participante.id_participante
	and ajvieira_isurvey_app.int_participante_lista_participantes.id_lista_participantes = '${fld:id_lista_participantes}'
order by
	id_participante