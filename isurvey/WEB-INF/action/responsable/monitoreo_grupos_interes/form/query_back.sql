select e.id_estudio, 

	case e.tipo
	when 'Cerrado' then 
	concat('<a href="${def:context}${def:actionroot}/estudio/form?id=',e.id_estudio,'">',e.nombre_estudio, '</a>')
	when 'Abierto-Identificado' then
	concat('<a href="${def:context}${def:actionroot}/estudio/form?id=',e.id_estudio,'">',e.nombre_estudio, '</a>')
	when 'Abierto-Anonimo' then
	e.nombre_estudio 
	end as nombre_estudio,
	
	(select count(instrumento.id_instrumento) 
	from  ajvieira_isurvey_app.instrumento 
	where instrumento.id_estudio = e.id_estudio ) as numero_encuestas,
	
	case e.tipo
	when 'Cerrado' then
		(select count(int_participante_instrumento.token_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
			(select id_instrumento from ajvieira_isurvey_app.instrumento 
				where id_estudio = e.id_estudio))
	when 'Abierto-Anonimo' then 
		(select count(*) from ajvieira_isurvey_lime.survey_379754)
	when 'Abierto-Identificado' then
		(select count(int_participante_instrumento.token_participante) + 
				(select count(participante.id_estudio_identificado)
				from ajvieira_isurvey_app.participante
				where id_participante not in 
					(select id_participante 
					from ajvieira_isurvey_app.int_participante_instrumento 
					where (estatus = 'Completa' or estatus = 'Incompleta')
					and id_instrumento in 
						(select id_instrumento from ajvieira_isurvey_app.instrumento 
							where id_estudio = e.id_estudio)))
from ajvieira_isurvey_app.int_participante_instrumento
where (estatus = 'Completa' or estatus = 'Incompleta')
and int_participante_instrumento.id_instrumento in
	(select id_instrumento from ajvieira_isurvey_app.instrumento
	where id_estudio = e.id_estudio))
	end as numero_participantes,
	
	case e.tipo
	when 'Cerrado' then
		(select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Completa')
	when 'Abierto-Anonimo' then 
		(select count(*) from ajvieira_isurvey_lime.survey_379754
		where submitdate)
	when 'Abierto-Identificado' then
		(select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Completa')
	end as completado,

	case e.tipo
	when 'Cerrado' then
		concat(round((select ((select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Completa')*100)/(select count(int_participante_instrumento.token_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
			(select id_instrumento from ajvieira_isurvey_app.instrumento 
				where id_estudio = e.id_estudio)))),'%')
	when 'Abierto-Anonimo' then
		concat(round((select ((select count(*) from ajvieira_isurvey_lime.survey_379754
		where submitdate) * 100) / (select count(*) from ajvieira_isurvey_lime.survey_379754))),'%')
	when 'Abierto-Identificado' then
		concat(round((select (((select count(int_participante_instrumento.id_participante)
				from ajvieira_isurvey_app.int_participante_instrumento
				where int_participante_instrumento.id_instrumento in 
				(select id_instrumento from ajvieira_isurvey_app.instrumento 
					where id_estudio = 10) 
				and estatus = 'Completa') * 100) / (select count(int_participante_instrumento.token_participante) + (select count(participante.id_estudio_identificado)
					from ajvieira_isurvey_app.participante
					where id_participante not in (select id_participante from ajvieira_isurvey_app.int_participante_instrumento where estatus = 'Completa' 
						and id_instrumento in 
						(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = 10)))
						from ajvieira_isurvey_app.int_participante_instrumento
						where (estatus = 'Completa' or estatus = 'Incompleta')
						and int_participante_instrumento.id_instrumento in
							(select id_instrumento from ajvieira_isurvey_app.instrumento
							where id_estudio = 10))))),'%')
	end as porcentaje_completado,

	case e.tipo
	when 'Cerrado' then
		(select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Incompleta')
	when 'Abierto-Anonimo' then 
		(select (select count(*) from ajvieira_isurvey_lime.survey_379754) - (select count(*) from ajvieira_isurvey_lime.survey_379754
		where submitdate))
	when 'Abierto-Identificado' then
		(select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Incompleta')
	end as incompleto,

	case e.tipo
	when 'Cerrado' then
		concat(round((select ((select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Incompleta')*100)/(select count(int_participante_instrumento.token_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
			(select id_instrumento from ajvieira_isurvey_app.instrumento 
				where id_estudio = e.id_estudio)))),'%')
	when 'Abierto-Anonimo' then
		concat(round((select ((select (select count(*) from ajvieira_isurvey_lime.survey_379754) - (select count(*) from ajvieira_isurvey_lime.survey_379754
		where submitdate)) * 100) / ((select count(*) from ajvieira_isurvey_lime.survey_379754)))),'%')
	when 'Abierto-Identificado' then
		concat(round((select (((select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Incompleta') * 100) / (select count(int_participante_instrumento.token_participante) + 
				(select count(participante.id_estudio_identificado)
				from ajvieira_isurvey_app.participante
				where id_participante not in 
					(select id_participante 
					from ajvieira_isurvey_app.int_participante_instrumento 
					where (estatus = 'Completa' or estatus = 'Incompleta')
					and id_instrumento in 
						(select id_instrumento from ajvieira_isurvey_app.instrumento 
							where id_estudio = e.id_estudio)))
from ajvieira_isurvey_app.int_participante_instrumento
where (estatus = 'Completa' or estatus = 'Incompleta')
and int_participante_instrumento.id_instrumento in
	(select id_instrumento from ajvieira_isurvey_app.instrumento
	where id_estudio = e.id_estudio))))),'%')
	end as porcentaje_incompleto,

	case e.tipo
	when 'Cerrado' then
		(select count(int_participante_instrumento.id_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Sin Iniciar')
	when 'Abierto-Anonimo' then 
		'--'
	when 'Abierto-Identificado' then
		(select count(participante.id_estudio_identificado) + 0
			from ajvieira_isurvey_app.participante
			where id_participante not in (select id_participante from ajvieira_isurvey_app.int_participante_instrumento where estatus = 'Completa' 
				and id_instrumento in 
				(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = e.id_estudio)))
	end as sin_iniciar,

	case e.tipo
	when 'Cerrado' then
		concat(round((select ((select count(int_participante_instrumento.id_participante)
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
		(select id_instrumento from ajvieira_isurvey_app.instrumento 
			where id_estudio = e.id_estudio) and estatus = 'Sin Iniciar')*100)/(select count(int_participante_instrumento.token_participante) + 0
		from ajvieira_isurvey_app.int_participante_instrumento
		where int_participante_instrumento.id_instrumento in 
			(select id_instrumento from ajvieira_isurvey_app.instrumento 
				where id_estudio = e.id_estudio)))),'%')
	when 'Abierto-Anonimo' then
		'--'
	when 'Abierto-Identificado' then
		concat(round((select ((select count(participante.id_estudio_identificado) + 0
			from ajvieira_isurvey_app.participante
			where id_participante not in (select id_participante from ajvieira_isurvey_app.int_participante_instrumento where estatus = 'Completa' 
				and id_instrumento in 
				(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = e.id_estudio)))*100)/(select count(int_participante_instrumento.token_participante) + 
				(select count(participante.id_estudio_identificado)
				from ajvieira_isurvey_app.participante
				where id_participante not in 
					(select id_participante 
					from ajvieira_isurvey_app.int_participante_instrumento 
					where (estatus = 'Completa' or estatus = 'Incompleta')
					and id_instrumento in 
						(select id_instrumento from ajvieira_isurvey_app.instrumento 
							where id_estudio = e.id_estudio)))
from ajvieira_isurvey_app.int_participante_instrumento
where (estatus = 'Completa' or estatus = 'Incompleta')
and int_participante_instrumento.id_instrumento in
	(select id_instrumento from ajvieira_isurvey_app.instrumento
	where id_estudio = e.id_estudio)))),'%')
	end as porcentaje_sin_iniciar

from ajvieira_isurvey_app.estudio as e
where e.id_empresa  in (select id_empresa from ajvieira_isurvey_security.s_user 
							where userlogin = '${def:user}')