select estudio.banner, empresa.logo, empresa.id_empresa
from ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.empresa
where
instrumento.id_instrumento = ${fld:id}
and instrumento.id_estudio = estudio.id_estudio
and empresa.id_empresa = estudio.id_empresa