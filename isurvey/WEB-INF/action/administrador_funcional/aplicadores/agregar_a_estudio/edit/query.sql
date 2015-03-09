select	
	ajvieira_isurvey_app.estudio.id_estudio, 
	ajvieira_isurvey_app.estudio.nombre_estudio as nombre_estudio_pl0,
	ajvieira_isurvey_app.int_estudio_usuario.*
from
	ajvieira_isurvey_app.estudio,
	ajvieira_isurvey_app.int_estudio_usuario 
where
	ajvieira_isurvey_app.estudio.id_estudio=ajvieira_isurvey_app.int_estudio_usuario.id_estudio and
	id_int_estudio_usuario = ${fld:id} 
	


