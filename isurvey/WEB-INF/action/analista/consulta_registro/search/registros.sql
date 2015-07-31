select instrumento.nombre, registro_carga_instrumentos.id_participante, 
'${def:user}' as userlogin, registro_carga_instrumentos.fecha_registro
from ajvieira_isurvey_app.registro_carga_instrumentos, ajvieira_isurvey_app.instrumento
where id_usuario = (select user_id from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')
and instrumento.id_instrumento = registro_carga_instrumentos.id_instrumento