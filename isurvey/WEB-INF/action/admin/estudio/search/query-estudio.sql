select	
	empresa.id_empresa, 
	empresa.nombre_empresa as nombre_empresa_pl0,
	estudio.*
from
	ajvieira_isurvey_app.empresa,
	ajvieira_isurvey_app.estudio 
where
	empresa.id_empresa=estudio.id_empresa 

order by 
	id_estudio
