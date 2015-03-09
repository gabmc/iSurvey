document.form1.id_usuario.value='${fld:id_usuario}';
document.form1.id_estudio_name.value='${fld:nombre_estudio_pl0@js}';
document.form1.id_estudio.value='${fld:id_estudio}';
document.form1.id.value='${fld:id_int_estudio_usuario}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

