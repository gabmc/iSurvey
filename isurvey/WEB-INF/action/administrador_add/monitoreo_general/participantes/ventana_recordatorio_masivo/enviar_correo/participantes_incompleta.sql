select distinct email_participante, nombre_participante, apellido_participante, 
(select token_participante from ajvieira_isurvey_app.int_participante_instrumento 
	where id_participante = part.id_participante and id_instrumento in 
	(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = ${fld:id_estudio}) limit 1) as token_participante
from ajvieira_isurvey_app.participante as part, ajvieira_isurvey_app.int_participante_instrumento
where part.id_participante in 
(select id_participante from ajvieira_isurvey_app.int_participante_instrumento where id_instrumento in 
	(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = ${fld:id_estudio}))
and part.id_empresa = ${fld:id_empresa}
and part.id_participante = int_participante_instrumento.id_participante
and int_participante_instrumento.id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento
where id_estudio = ${fld:id_estudio})
and int_participante_instrumento.estatus = 'Incompleta'
