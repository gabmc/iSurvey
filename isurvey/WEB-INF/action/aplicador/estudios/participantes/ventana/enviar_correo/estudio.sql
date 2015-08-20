select concat('Enlace del Estudio ', nombre_estudio) as nombre
from ajvieira_isurvey_app.estudio
where id_estudio = ${fld:id_estudio}