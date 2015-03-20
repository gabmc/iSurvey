select
	participante.*
from
	ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes,
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio,
	ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.int_estudio_usuario
where
	int_estudio_usuario.id_usuario in (select ajvieira_isurvey_security.s_user.user_id from ajvieira_isurvey_security.s_user where ajvieira_isurvey_security.s_user.userlogin = '${def:user}')
	and int_estudio_usuario.id_estudio = estudio.id_estudio
	and int_lista_participantes_estudio.id_estudio = estudio.id_estudio
	and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes
	and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes
	and int_participante_lista_participantes.id_participante = participante.id_participante
order by
	id_participante