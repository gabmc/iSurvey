document.form1.id_lista_participantes_name.value='${fld:nombre_lista_participantes_pl0@js}';
document.form1.id_lista_participantes.value='${fld:id_lista_participantes}';
document.form1.nombre_participante.value='${fld:nombre_participante@js}';
document.form1.apellido_participante.value='${fld:apellido_participante@js}';
document.form1.cedula_participante.value='${fld:cedula_participante@js}';
document.form1.email_participante.value='${fld:email_participante@js}';
document.form1.cargo.value='${fld:cargo@js}';
document.form1.supervisor.value='${fld:supervisor@js}';
document.form1.fecha_nacimiento.value='${fld:fecha_nacimiento@dd-MM-yyyy}';
document.form1.fecha_ingreso.value='${fld:fecha_ingreso@dd-MM-yyyy}';
document.form1.sexo.value='${fld:sexo@js}';
document.form1.tipo_nomina.value='${fld:tipo_nomina@js}';
document.form1.funcion.value='${fld:funcion@js}';
document.form1.id.value='${fld:id_participante}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

