select	
	sector_empresarial.id_sector_empresarial,
	sector_empresarial.nombre_sector
from
	ajvieira_isurvey_app.sector_empresarial 
where
	id_sector_empresarial is not null
	${filter}