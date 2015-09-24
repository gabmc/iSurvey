insert into ajvieira_isurvey_app.participante
(
	id_participante,
        nombre_participante,
        apellido_participante,
        email_participante,
        sexo,
        empresa,
        area,
        cargo,
        telefono,
        supervisor,
        fecha_nacimiento,
        fecha_ingreso,
        tipo_nomina,
        funcion,
        id_empresa,
        fecha_registro,
        verificado,
        fecha_verificacion
)
values
(
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        ?,
        (select id_empresa from ajvieira_isurvey_security.s_user where userlogin = '${def:user}'),
        curdate(),
        'Si',
        0000-00-00
)
