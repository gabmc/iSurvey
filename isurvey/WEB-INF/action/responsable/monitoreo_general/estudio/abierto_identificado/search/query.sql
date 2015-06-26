select participante.*, 99 as completado 
from ajvieira_isurvey_app.participante
where id_participante in 
	(select id_participante 
	from ajvieira_isurvey_app.int_participante_instrumento 
	where 
		(estatus = 'Incompleta' or estatus = 'Completa')
		and id_instrumento in 
			(select id_instrumento
			from ajvieira_isurvey_app.instrumento
			where id_estudio = ${fld:id_estudio}))

union

select participante.*, 99 as completado
from ajvieira_isurvey_app.participante
where id_estudio_identificado = ${fld:id_estudio}
and id_participante in
	(select id_participante 
	from ajvieira_isurvey_app.int_participante_instrumento 
	where id_instrumento in 
		(select id_instrumento
		from ajvieira_isurvey_app.instrumento
		where id_estudio = ${fld:id_estudio}))