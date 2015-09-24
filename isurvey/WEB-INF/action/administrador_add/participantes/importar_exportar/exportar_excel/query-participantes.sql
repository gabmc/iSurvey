select
	participante.nombre_participante, participante.apellido_participante, participante.email_participante, participante.sexo, participante.empresa,
	participante.area, participante.cargo, participante.telefono, participante.supervisor, participante.fecha_nacimiento, 
	participante.fecha_ingreso, participante.tipo_nomina, participante.funcion, participante.id_participante
from
	ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_lista_participantes
where
	ajvieira_isurvey_app.int_participante_lista_participantes.id_participante = ajvieira_isurvey_app.participante.id_participante
	and ajvieira_isurvey_app.int_participante_lista_participantes.id_lista_participantes = '${fld:id_lista_participantes}'
	and participante.id_empresa = ${fld:id_empresa}
order by
	id_participante