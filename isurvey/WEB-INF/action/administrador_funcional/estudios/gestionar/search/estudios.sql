select * from ajvieira_isurvey_app.estudio
where id_empresa = (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}')