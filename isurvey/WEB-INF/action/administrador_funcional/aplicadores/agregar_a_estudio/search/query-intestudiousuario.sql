select
	ajvieira_isurvey_app.estudio.id_estudio,
	ajvieira_isurvey_app.estudio.nombre_estudio as nombre_estudio_pl0,
	ajvieira_isurvey_app.int_estudio_usuario.*,
        ajvieira_isurvey_security.s_user.userlogin as userlogin_p10
from
	ajvieira_isurvey_app.estudio,
	ajvieira_isurvey_app.int_estudio_usuario,
        ajvieira_isurvey_security.s_user
where
	ajvieira_isurvey_app.estudio.id_estudio=ajvieira_isurvey_app.int_estudio_usuario.id_estudio
	and ajvieira_isurvey_security.s_user.user_id = ajvieira_isurvey_app.int_estudio_usuario.id_usuario

order by
	id_int_estudio_usuario