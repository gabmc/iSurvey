select
	ajvieira_isurvey_app.lista_participantes.*
from 
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio, 
	ajvieira_isurvey_app.estudio
where
	ajvieira_isurvey_app.estudio.id_estudio = ajvieira_isurvey_app.int_lista_participantes_estudio.id_estudio
	and ajvieira_isurvey_app.int_lista_participantes_estudio.id_lista_participantes = ajvieira_isurvey_app.lista_participantes.id_lista_participantes
	and ajvieira_isurvey_app.estudio.id_empresa in (select ajvieira_isurvey_security.s_user.id_empresa from ajvieira_isurvey_security.s_user where ajvieira_isurvey_security.s_user.userlogin = '${def:user}')
order by 
	id_lista_participantes
