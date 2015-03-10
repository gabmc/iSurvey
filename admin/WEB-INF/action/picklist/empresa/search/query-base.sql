select	
	ajvieira_isurvey_app.empresa.*
from
	ajvieira_isurvey_app.empresa 
where
	id_empresa is not null
	${filter}