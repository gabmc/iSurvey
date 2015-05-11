select
	lista_participantes.*, .estudio.nombre_estudio as estudio, estudio.id_estudio as id_estudio
from 
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio, 
	ajvieira_isurvey_app.estudio
where
	estudio.id_estudio = int_lista_participantes_estudio.id_estudio
	and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes
	and estudio.id_empresa = (select s_user.id_empresa from ajvieira_isurvey_security.s_user where s_user.userlogin = '${def:user}')
	and lista_participantes.oculta = 'No'
order by 
	id_lista_participantes
