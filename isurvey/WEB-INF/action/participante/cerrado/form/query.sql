select instrumento.*, int_participante_instrumento.token_participante
from ajvieira_isurvey_app.instrumento, ajvieira_isurvey_app.int_participante_instrumento
where instrumento.id_instrumento = ${fld:id_instrumento}
and int_participante_instrumento.id_instrumento = ${fld:id_instrumento}
and int_participante_instrumento.id_participante = ${fld:id_participante}
and int_participante_instrumento.id_empresa_participante = ${fld:id_empresa_participante}