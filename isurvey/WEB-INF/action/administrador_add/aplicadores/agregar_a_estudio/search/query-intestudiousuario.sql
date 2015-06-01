select
	estudio.id_estudio,
	estudio.nombre_estudio as nombre_estudio_pl0,
	int_estudio_usuario.*,
    s_user.userlogin as userlogin_p10,
    ${fld:id_empresa} as id_empresa
from
	ajvieira_isurvey_app.estudio,
	ajvieira_isurvey_app.int_estudio_usuario,
        ajvieira_isurvey_security.s_user
where
	estudio.id_estudio=int_estudio_usuario.id_estudio
	and s_user.user_id = int_estudio_usuario.id_usuario
	and s_user.id_empresa = ${fld:id_empresa}