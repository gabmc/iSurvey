document.form1.nombre_estudio.value='${fld:nombre_estudio@js}';
document.form1.id_empresa_name.value='${fld:nombre_empresa_pl0@js}';
document.form1.id_empresa.value='${fld:id_empresa}';
document.form1.id.value='${fld:id_estudio}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

