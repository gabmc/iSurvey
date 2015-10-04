select 
lista_participantes.*, ${fld:id_estudio} as "id_estudio"
from
ajvieira_isurvey_app.lista_participantes, ajvieira_isurvey_app.int_lista_participantes_estudio, 
ajvieira_isurvey_app.estudio
where
lista_participantes.id_empresa = (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')
and estudio.id_estudio = ${fld:id_estudio}
and int_lista_participantes_estudio.id_estudio = estudio.id_estudio
and int_lista_participantes_estudio.id_lista_participantes = lista_participantes.id_lista_participantes