document.form1.id_estudio_name.value='${fld:nombre_estudio_pl0@js}';
document.form1.id_estudio.value='${fld:id_estudio}';
document.form1.nombre.value='${fld:nombre@js}';
document.form1.id_survey_name.value='${fld:id_instrumento}';
document.form1.id_survey.value='${fld:id_instrumento}';
document.form1.id.value='${fld:id_instrumento}';
alert(document.form1.id.value);
 
document.getElementById("formTitle").innerHTML = "Editar registro";
document.getElementById("grabar").disabled=false;
setFocusOnForm("form1");

