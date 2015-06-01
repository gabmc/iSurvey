select id_estudio, id_usuario
from ajvieira_isurvey_app.int_estudio_usuario
where id_estudio = ${fld:id_estudio}
and id_usuario = ${fld:id_usuario}