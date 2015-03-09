document.form1.id_sector_empresarial_name.value='${fld:nombre_sector_pl0@js}';
document.form1.id_sector_empresarial.value='${fld:id_sector_empresarial}';
document.form1.nombre_empresa.value='${fld:nombre_empresa@js}';
document.form1.telefono.value='${fld:telefono@js}';
document.form1.tipo.value='${fld:tipo@js}';
document.form1.rif.value='${fld:rif@js}';
document.form1.id.value='${fld:id_empresa}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

