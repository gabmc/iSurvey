select count(instrumento.id_instrumento) as instrumentos
from ajvieira_isurvey_app.instrumento 
where instrumento.id_estudio = ${fld:id_estudio}