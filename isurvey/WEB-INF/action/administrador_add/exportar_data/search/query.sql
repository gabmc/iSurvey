select instrumento.id_instrumento, instrumento.nombre as nombre_instrumento, estudio.nombre_estudio as nombre_estudio 
from ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.estudio
where instrumento.id_estudio = estudio.id_estudio
order by instrumento.id_estudio