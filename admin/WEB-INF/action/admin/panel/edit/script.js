document.form1.service_id.value='${fld:service_id}';
document.form1.icon_path.value='${fld:icon_path@js}';
document.form1.icon_path_name.value='${fld:icon_path@js}';
document.form1.orden.value='${fld:orden}';
document.form1.id.value='${fld:panel_id}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

