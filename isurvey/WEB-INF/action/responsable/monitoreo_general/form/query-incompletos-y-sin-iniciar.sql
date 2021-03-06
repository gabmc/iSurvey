select participante.id_participante, 
       participante.nombre_participante, 
       participante.apellido_participante, 
       participante.email_participante, 
       case (select count(estatus) from ajvieira_isurvey_app.int_participante_instrumento 
                    where id_participante = ipi.id_participante and id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = {{id_estudio}}
                    and estatus = 'Completa')
                    group by id_participante)
                    when 0 then
                    0
                    when null then
                    0
                    else
                    (select count(estatus) from ajvieira_isurvey_app.int_participante_instrumento 
                    where estatus = 'Completa' and id_participante = ipi.id_participante and id_instrumento in 
                    (select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = {{id_estudio}} 
                    and estatus = 'Completa')) 
end as completado,
        case (select count(estatus) from ajvieira_isurvey_app.int_participante_instrumento 
                    where id_participante = ipi.id_participante and id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = {{id_estudio}}
                    and estatus = 'Completa')
                    group by id_participante)
                when 0 then
                    'Sin Iniciar'
                when (select count(instrumento.id_instrumento) as instrumentos
                                                 from ajvieira_isurvey_app.instrumento 
                                                where instrumento.id_estudio = {{id_estudio}}) then
                    'Completa'
                when ((select count(estatus) from ajvieira_isurvey_app.int_participante_instrumento 
                    where id_participante = ipi.id_participante and id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = {{id_estudio}}
                    and estatus = 'Completa')
                    group by id_participante) < (select count(instrumento.id_instrumento) as instrumentos
                    from ajvieira_isurvey_app.instrumento 
                    where instrumento.id_estudio = {{id_estudio}})) then
                    'Incompleta'
                else
                    'Sin Iniciar'
end as estatus1,
(select token_participante from ajvieira_isurvey_app.int_participante_instrumento 
    where id_participante = ipi.id_participante and 
    id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento 
        where id_estudio = {{id_estudio}}) limit 1) as token             

                from ajvieira_isurvey_app.participante, 
                ajvieira_isurvey_app.int_participante_lista_participantes, 
                ajvieira_isurvey_app.lista_participantes, 
                ajvieira_isurvey_app.int_lista_participantes_estudio, 
                ajvieira_isurvey_app.estudio,
                ajvieira_isurvey_app.int_participante_instrumento as ipi  
                where 
                participante.id_participante = int_participante_lista_participantes.id_participante 
                and participante.id_empresa = int_participante_lista_participantes.id_empresa_participante 
                and int_participante_lista_participantes.id_lista_participantes = lista_participantes.id_lista_participantes 
                and lista_participantes.id_empresa = participante.id_empresa 
                and lista_participantes.id_lista_participantes = int_lista_participantes_estudio.id_lista_participantes 
                and int_lista_participantes_estudio.id_estudio = estudio.id_estudio 
                and estudio.id_empresa = participante.id_empresa 
                and estudio.id_estudio = {{id_estudio}}
                and ipi.id_participante = participante.id_participante
                and ipi.id_instrumento in (select id_instrumento from ajvieira_isurvey_app.instrumento where id_estudio = {{id_estudio}})
                group by id_participante
                having estatus1 like '{{estatus}}'