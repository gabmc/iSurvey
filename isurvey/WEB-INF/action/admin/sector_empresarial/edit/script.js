document.form1.nombre_sector.value='${fld:nombre_sector@js}';
document.form1.id.value='${fld:id_sector_empresarial}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

