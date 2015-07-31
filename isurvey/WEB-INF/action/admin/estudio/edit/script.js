document.form1.nombre_estudio.value='${fld:nombre_estudio@js}';
document.form1.tipo.value='${fld:tipo@js}';
document.form1.id_empresa_name.value='${fld:nombre_empresa_pl0@js}';
document.form1.id_empresa.value='${fld:id_empresa}';
document.form1.id.value='${fld:id_estudio}';
document.form1.titulo_email.value='${fld:titulo_email@js}';
document.form1.cuerpo_email.value='${fld:cuerpo_email@js}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
document.getElementById("tipo").disabled=true;
setFocusOnForm("form1");

