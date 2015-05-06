select	
	estudio.id_estudio, 
	estudio.nombre_estudio as nombre_estudio_pl0,
	instrumento.*,
	case estudio.tipo
		when 'Cerrado' then concat('http://localhost:8090/limesurvey/index.php/', instrumento.id_instrumento)
		when 'Abierto-Anonimo' then concat('http://localhost/isurvey/action/estudio/abierto/anonimo/form?id=', instrumento.id_instrumento)
        when 'Abierto-Identificado' then concat('http://localhost/isurvey/action/estudio/abierto/identificado/form?id=', instrumento.id_instrumento)
        end as link
from
	ajvieira_isurvey_app.estudio,
	ajvieira_isurvey_app.instrumento 
where
	ajvieira_isurvey_app.estudio.id_estudio=ajvieira_isurvey_app.instrumento.id_estudio 

order by 
	id_instrumento
