select	
	ajvieira_isurvey_app.sector_empresarial.id_sector_empresarial, 
	ajvieira_isurvey_app.sector_empresarial.nombre_sector as nombre_sector_pl0,
	ajvieira_isurvey_app.empresa.*
from
	ajvieira_isurvey_app.sector_empresarial,
	ajvieira_isurvey_app.empresa 
where
	ajvieira_isurvey_app.sector_empresarial.id_sector_empresarial=ajvieira_isurvey_app.empresa.id_sector_empresarial 

order by 
	id_empresa
