document.form1.nombre_lista_participantes.value='${fld:nombre_lista_participantes@js}';
document.form1.id.value='${fld:id_lista_participantes}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

