select
	participante.nombre_participante, participante.apellido_participante, participante.email_participante, participante.sexo, participante.empresa,
	participante.area, participante.cargo, participante.telefono, participante.supervisor, participante.fecha_nacimiento, 
	participante.fecha_ingreso, participante.tipo_nomina, participante.funcion, participante.id_participante
from
	ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes
where
	int_participante_lista_participantes.id_participante = participante.id_participante
	and int_participante_lista_participantes.id_lista_participantes = '${fld:id_lista_participantes}'
	and participante.id_empresa in (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')
order by
	id_participante