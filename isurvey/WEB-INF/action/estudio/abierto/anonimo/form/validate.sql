select estudio.banner
from ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento
where
instrumento.id_instrumento = ${fld:id}
and instrumento.id_estudio = estudio.id_estudio