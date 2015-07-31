select participante.id_participante, participante.nombre_participante, participante.apellido_participante, participante.email_participante, porcentaje_completado as completado, token_participante, id_instrumento
from ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_instrumento
where participante.id_participante in 
	(select id_participante 
	from ajvieira_isurvey_app.int_participante_instrumento 
	where 
		(estatus = 'Incompleta' or estatus = 'Completa')
	and id_instrumento in 
		(select id_instrumento
		from ajvieira_isurvey_app.instrumento
		where id_estudio = ${fld:id_estudio}))
and int_participante_instrumento.id_instrumento in 
	(select id_instrumento
			from ajvieira_isurvey_app.instrumento
			where id_estudio = ${fld:id_estudio})
and participante.id_participante = int_participante_instrumento.id_participante
and concat(participante.id_participante) like ${fld:identificador}
and estatus like ${fld:estatus}
and upper(nombre_participante) like upper(${fld:nombre})
and upper(apellido_participante) like upper(${fld:apellido})
and upper(email_participante) like upper(${fld:email})


union

select participante.id_participante, participante.nombre_participante, participante.apellido_participante, participante.email_participante, porcentaje_completado as completado, token_participante, id_instrumento
from ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_instrumento
where id_estudio_identificado = ${fld:id_estudio}
and participante.id_participante in
	(select id_participante 
	from ajvieira_isurvey_app.int_participante_instrumento 
	where id_instrumento in 
		(select id_instrumento
		from ajvieira_isurvey_app.instrumento
		where id_estudio = ${fld:id_estudio}))
and participante.id_participante = int_participante_instrumento.id_participante
and concat(participante.id_participante) like ${fld:identificador}
and estatus like ${fld:estatus}
and upper(nombre_participante) like upper(${fld:nombre})
and upper(apellido_participante) like upper(${fld:apellido})
and upper(email_participante) like upper(${fld:email})


order by id_participante
