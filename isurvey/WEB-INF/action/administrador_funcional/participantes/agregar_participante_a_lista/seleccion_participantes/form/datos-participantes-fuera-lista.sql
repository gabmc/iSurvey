select 
participante.id_participante, participante.nombre_participante, participante.apellido_participante,
participante.email_participante, participante.empresa, participante.empresa,
participante.cargo, participante.supervisor, participante.fecha_nacimiento, 
participante.fecha_ingreso, participante.sexo, participante.tipo_nomina, participante.funcion,
${fld:id_lista_participantes} as "id_lista_participantes"
from
ajvieira_isurvey_app.participante
where
participante.id_empresa = (select id_empresa from ajvieira_isurvey_app.lista_participantes where id_lista_participantes=${fld:id_lista_participantes})
and ((participante.id_participante not in 
(select id_participante from ajvieira_isurvey_app.int_participante_lista_participantes 
	where id_empresa_participante = 
	(select id_empresa from ajvieira_isurvey_app.lista_participantes 
		where id_lista_participantes=${fld:id_lista_participantes}))) or participante.id_participante not in 
(select id_participante from ajvieira_isurvey_app.int_participante_lista_participantes 
	where id_lista_participantes = ${fld:id_lista_participantes}
	and id_empresa_participante = 
	(select id_empresa from ajvieira_isurvey_app.lista_participantes 
		where id_lista_participantes=${fld:id_lista_participantes})))