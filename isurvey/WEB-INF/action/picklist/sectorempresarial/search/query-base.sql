select	
	ajvieira_isurvey_app.sector_empresarial.*
from
	ajvieira_isurvey_app.sector_empresarial 
where
	id_sector_empresarial is not null
	${filter}