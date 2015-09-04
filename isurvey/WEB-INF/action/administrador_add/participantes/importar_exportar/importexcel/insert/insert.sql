insert into ajvieira_isurvey_app.participante
(
	id_participante,
        nombre_participante,
        apellido_participante,
        email_participante,
        empresa,
        sector_empresa,
        cargo,
        supervisor,
        fecha_nacimiento,
        fecha_ingreso,
        sexo,
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
        {{id_empresa}},
        curdate(),
        'Si',
        now()
)
