select	
	ajvieira_isurvey_app.estudio.id_estudio, 
	ajvieira_isurvey_app.estudio.nombre_estudio as nombre_estudio_pl0,
	ajvieira_isurvey_app.instrumento.*
from
	ajvieira_isurvey_app.estudio,
	ajvieira_isurvey_app.instrumento 
where
	ajvieira_isurvey_app.estudio.id_estudio=ajvieira_isurvey_app.instrumento.id_estudio 

order by 
	id_instrumento
