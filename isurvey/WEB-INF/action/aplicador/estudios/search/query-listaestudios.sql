select
	estudio.*
from
	ajvieira_isurvey_app.estudio, ajvieira_isurvey_app.int_estudio_usuario
where
	int_estudio_usuario.id_estudio = estudio.id_estudio
	and int_estudio_usuario.id_usuario in 
		(select user_id from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')