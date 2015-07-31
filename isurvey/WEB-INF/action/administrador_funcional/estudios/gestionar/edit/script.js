document.form1.nombre_estudio.value='${fld:nombre_estudio@js}';
document.form1.estatus.value='${fld:estatus@js}';
document.form1.id_estudio.value='${fld:id_estudio}';

//document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

