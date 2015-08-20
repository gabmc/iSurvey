select participante.id_participante, participante.nombre_participante, participante.apellido_participante, 
participante.email_participante, participante.telefono, participante.empresa, participante.sector_empresa, 
participante.area, participante.cargo, participante.sexo, porcentaje_completado as completado, 
token_participante, id_instrumento, '${fld:id_estudio}' as id_estudio
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
and int_participante_instrumento.id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento
where id_estudio = ${fld:id_estudio})
and concat(participante.id_participante) like ${fld:identificador}
and estatus like ${fld:estatus}
and upper(nombre_participante) like upper(${fld:nombre})
and upper(apellido_participante) like upper(${fld:apellido})
and upper(email_participante) like upper(${fld:email})
and upper(area) like upper(${fld:area})
and upper(cargo) like upper(${fld:cargo})
and upper(sexo) like upper(${fld:sexo})
and upper(empresa) like upper(${fld:empresa})
and upper(sector_empresa) like upper(${fld:sector})

union

select participante.id_participante, participante.nombre_participante, participante.apellido_participante, 
participante.email_participante, participante.telefono, participante.empresa, participante.sector_empresa, 
participante.area, participante.cargo, participante.sexo, porcentaje_completado as completado, token_participante, id_instrumento
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
and int_participante_instrumento.id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento
where id_estudio = ${fld:id_estudio})
and concat(participante.id_participante) like ${fld:identificador}
and estatus like ${fld:estatus}
and upper(nombre_participante) like upper(${fld:nombre})
and upper(apellido_participante) like upper(${fld:apellido})
and upper(email_participante) like upper(${fld:email})
and upper(area) like upper(${fld:area})
and upper(cargo) like upper(${fld:cargo})
and upper(sexo) like upper(${fld:sexo})
and upper(empresa) like upper(${fld:empresa})
and upper(sector_empresa) like upper(${fld:sector})


order by id_participante
