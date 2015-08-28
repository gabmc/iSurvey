select participante.nombre_participante
from ajvieira_isurvey_app.participante
where
upper(participante.nombre_participante) = upper(${fld:nombre_participante})
and upper(participante.apellido_participante) = upper(${fld:apellido_participante})
and upper(participante.email_participante) = upper(${fld:email_participante})
and participante.id_empresa = 
(select estudio.id_empresa 
	from ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento 
	where instrumento.id_instrumento = ${fld:id_instrumento}
	and instrumento.id_estudio = estudio.id_estudio)