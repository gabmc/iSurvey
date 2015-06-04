select
	lista_participantes.*, .estudio.nombre_estudio as estudio, estudio.id_estudio as id_estudio
from 
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio, 
	ajvieira_isurvey_app.estudio
where
	estudio.id_estudio = int_lista_participantes_estudio.id_estudio
	and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes
	and estudio.id_empresa = ${fld:id_empresa}
	and lista_participantes.oculta = 'No'
order by 
	id_lista_participantes
