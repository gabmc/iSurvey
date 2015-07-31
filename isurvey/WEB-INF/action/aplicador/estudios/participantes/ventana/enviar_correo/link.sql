select token_participante
from ajvieira_isurvey_app.int_participante_instrumento
where id_participante = ${fld:id_participante}
and id_instrumento in 
(select id_instrumento from ajvieira_isurvey_app.estudio where id_estudio = ${fld:id_estudio})
limit 1