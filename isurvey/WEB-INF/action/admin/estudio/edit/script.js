document.form1.nombre_estudio.value='${fld:nombre_estudio@js}';
document.form1.tipo.value='${fld:tipo@js}';
document.form1.id_empresa_name.value='${fld:nombre_empresa_pl0@js}';
document.form1.id_empresa.value='${fld:id_empresa}';
document.form1.id.value='${fld:id_estudio}';
document.form1.titulo_email.value='${fld:titulo_email@js}';
CKEDITOR.instances['cuerpo_email'].setData('${fld:cuerpo_email@js}');
document.form1.titulo_email_recordatorio.value='${fld:titulo_mail_recordatorio@js}';
CKEDITOR.instances['cuerpo_email_recordatorio'].setData('${fld:cuerpo_mail_recordatorio@js}'); 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
document.getElementById("tipo").disabled=true;
document.form1.estatus.value='${fld:estatus@js}';
setFocusOnForm("form1");

