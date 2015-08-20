select participante.id_participante, participante.id_empresa
from ajvieira_isurvey_app.participante
where
participante.id_participante = ${fld:id_participante}
and participante.id_empresa = ${fld:id_empresa}
and participante.email_participante = ${fld:email}