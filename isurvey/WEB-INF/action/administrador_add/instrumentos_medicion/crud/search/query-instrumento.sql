select	
	estudio.id_estudio, 
	estudio.nombre_estudio as nombre_estudio_pl0,
	instrumento.*,
	concat('http://www.compensa.com.ve/isurvey_lime/index.php/admin/survey/sa/view/surveyid/',instrumento.id_instrumento) as link
from
	ajvieira_isurvey_app.estudio,
	ajvieira_isurvey_app.instrumento 
where
	ajvieira_isurvey_app.estudio.id_estudio=ajvieira_isurvey_app.instrumento.id_estudio 

order by 
	id_instrumento
