select
	participante.id_participante, participante.nombre_participante, participante.apellido_participante,
	int_participante_instrumento.token_participante, estudio.id_estudio,
	case estudio.tipo
		when 'Cerrado' then concat('http://localhost/isurvey/action/estudio/cerrado/form?token=', int_participante_instrumento.token_participante)
		when 'Abierto-Anonimo' then concat('http://localhost/isurvey/action/estudio/abierto/anonimo/form?id=', instrumento.id_instrumento)
        when 'Abierto-Identificado' then concat('http://localhost/isurvey/action/estudio/abierto/identificado/form?id=', instrumento.id_instrumento)
        end as link,
    case (select count(estatus) from ajvieira_isurvey_app.int_participante_instrumento 
    				where id_participante = participante.id_participante and id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = ${fld:id_estudio}
    				and estatus = 'Completa')
					group by id_participante)
					when 0 then
					0
					when null then
					0
					else
					(select count(estatus) from ajvieira_isurvey_app.int_participante_instrumento 
					where estatus = 'Completa' and id_participante = participante.id_participante and id_instrumento in 
					(select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = ${fld:id_estudio} 
					and estatus = 'Completa')) 
end as completado
from
	ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes,
	ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio,
	ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.int_estudio_usuario, ajvieira_isurvey_app.int_participante_instrumento,
	ajvieira_isurvey_app.instrumento
where
	int_estudio_usuario.id_usuario = (select ajvieira_isurvey_security.s_user.user_id from ajvieira_isurvey_security.s_user where ajvieira_isurvey_security.s_user.userlogin = '${def:user}')
	and int_estudio_usuario.id_estudio = estudio.id_estudio
	and int_lista_participantes_estudio.id_estudio = estudio.id_estudio
	and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes
	and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes
	and int_participante_lista_participantes.id_participante = participante.id_participante
	and participante.id_empresa = (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')
	and estudio.id_estudio = ${fld:id_estudio}
	and int_participante_instrumento.id_participante = participante.id_participante
	and instrumento.id_instrumento = int_participante_instrumento.id_instrumento
	and instrumento.id_estudio = estudio.id_estudio
	and concat(participante.id_participante) like ${fld:identificador}
	and upper(participante.nombre_participante) like upper(${fld:nombre})
	and upper(participante.apellido_participante) like upper(${fld:apellido})
group by
	id_participante
order by
	id_participante
