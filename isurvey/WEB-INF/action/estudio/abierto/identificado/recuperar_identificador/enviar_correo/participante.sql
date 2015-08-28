select lower(email_participante) as email_participante, nombre_participante, apellido_participante, id_participante, 'Recuperar Identificador' as subject
from ajvieira_isurvey_app.participante as part
where upper(part.nombre_participante) = upper(${fld:nombre_participante})
and upper(part.apellido_participante) = upper(${fld:apellido_participante})
and upper(part.email_participante) = upper(${fld:email_participante})
and part.id_empresa = (select estudio.id_empresa 
	from ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento 
	where instrumento.id_instrumento = ${fld:id_instrumento}
	and instrumento.id_estudio = estudio.id_estudio)
