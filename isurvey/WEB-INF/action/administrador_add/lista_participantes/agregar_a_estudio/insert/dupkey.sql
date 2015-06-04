select id_estudio, id_lista_participantes
from ajvieira_isurvey_app.int_lista_participantes_estudio
where id_estudio = ${fld:id_estudio}
and id_lista_participantes = ${fld:id_lista_participantes}