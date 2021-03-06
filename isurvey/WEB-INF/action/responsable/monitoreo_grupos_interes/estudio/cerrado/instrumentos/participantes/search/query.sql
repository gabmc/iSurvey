select participante.area, participante.cargo, participante.sexo, participante.tipo_nomina
from ajvieira_isurvey_app.participante, ajvieira_isurvey_app.int_participante_instrumento
where
participante.id_participante in 
(select id_participante 
from ajvieira_isurvey_app.int_participante_instrumento 
where id_instrumento = ${fld:id_instrumento})
and int_participante_instrumento.id_participante = participante.id_participante
and int_participante_instrumento.id_instrumento = ${fld:id_instrumento}

and concat(participante.id_participante) like ${fld:identificador}
and estatus like ${fld:estatus}
and upper(nombre_participante) like upper(${fld:nombre})
and upper(apellido_participante) like upper(${fld:apellido})
and upper(email_participante) like upper(${fld:email})
