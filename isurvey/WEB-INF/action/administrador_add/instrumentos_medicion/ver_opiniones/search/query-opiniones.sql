select opinion.*, instrumento.nombre, estudio.nombre_estudio, 
concat(participante.nombre_participante, ' ',participante.apellido_participante) as nombre_participante
from ajvieira_isurvey_app.opinion, ajvieira_isurvey_app.instrumento, 
ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.participante
where opinion.id_instrumento = instrumento.id_instrumento
and instrumento.id_estudio = estudio.id_estudio
and participante.id_participante = opinion.id_participante
and participante.id_empresa = estudio.id_empresa
and upper(estudio.nombre_estudio) like upper(${fld:estudio})
and upper(instrumento.nombre) like upper(${fld:instrumento})
and upper(nombre_participante) like upper(${fld:participante})
and upper(opinion.tipo) like upper(${fld:tipo})
and upper(concat(opinion.fecha_registro)) like upper(${fld:fecha})