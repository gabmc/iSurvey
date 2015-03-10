document.form2.description.value = "${fld:description@js}"
document.form2.path.value = "${fld:path@js}"
document.form2.service_id.value = "${fld:service_id}"
setComboValue('app_id','${fld:app_id}','form2')
document.getElementById("formTitle").innerHTML = "Editar Servicio";
document.getElementById("deleteCommand").style.display='';
document.getElementById("editor").style.display='';
document.form2.description.focus();

