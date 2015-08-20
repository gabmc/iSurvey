select nombre_participante, apellido_participante, email_participante
from ajvieira_isurvey_app.participante
where id_participante = ${fld:id_participante}
and id_empresa = ${fld:id_empresa}
