select email_participante, nombre_participante, apellido_participante, 
(select token_participante from ajvieira_isurvey_app.int_participante_instrumento 
	where id_participante = part.id_participante and id_instrumento in 
	(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = ${fld:id_estudio}) limit 1) as token_participante
from ajvieira_isurvey_app.participante as part
where part.id_participante = ${fld:id_participante}
and part.id_empresa = (select id_empresa from ajvieira_isurvey_app.estudio where id_estudio = ${fld:id_estudio})
