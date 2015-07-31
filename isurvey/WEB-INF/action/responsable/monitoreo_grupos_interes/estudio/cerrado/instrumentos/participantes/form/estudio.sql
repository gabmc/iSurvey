select nombre_estudio from ajvieira_isurvey_app.estudio 
where id_estudio = 
(select id_estudio from ajvieira_isurvey_app.instrumento where id_instrumento = ${fld:id})