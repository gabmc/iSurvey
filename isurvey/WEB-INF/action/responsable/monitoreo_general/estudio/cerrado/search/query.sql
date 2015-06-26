select id_participante, nombre_participante, apellido_participante, email_participante, empresa
from ajvieira_isurvey_app.participante
where
id_participante in 
(select id_participante 
from ajvieira_isurvey_app.int_participante_instrumento 
where id_instrumento in 
(select id_instrumento
from ajvieira_isurvey_app.instrumento
where id_estudio = ${fld:id_estudio}))