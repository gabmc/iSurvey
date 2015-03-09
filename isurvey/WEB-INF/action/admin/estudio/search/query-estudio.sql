select	
	ajvieira_isurvey_app.empresa.id_empresa, 
	ajvieira_isurvey_app.empresa.nombre_empresa as nombre_empresa_pl0,
	ajvieira_isurvey_app.estudio.*
from
	ajvieira_isurvey_app.empresa,
	ajvieira_isurvey_app.estudio 
where
	ajvieira_isurvey_app.empresa.id_empresa=ajvieira_isurvey_app.estudio.id_empresa 

order by 
	id_estudio
