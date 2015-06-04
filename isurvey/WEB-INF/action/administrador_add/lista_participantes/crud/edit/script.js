document.form1.nombre_lista_participantes.value='${fld:nombre_lista_participantes@js}';
document.form1.id.value='${fld:id_lista_participantes}';
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("id_empresa").value = ${fld:id_empresa};
document.getElementById("id_empresa_name").value = '${fld:nombre_empresa}';
    document.getElementById("buscar").removeAttribute("onclick");
    document.getElementById("borrar").removeAttribute("onclick");
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

