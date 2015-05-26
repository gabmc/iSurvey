select	
	empresa.id_empresa,
	empresa.id_sector_empresarial,
	empresa.nombre_empresa,
	empresa.telefono,
	empresa.tipo,
	empresa.rif,
	empresa.logo
from
	ajvieira_isurvey_app.empresa 
where
	id_empresa is not null
	${filter}