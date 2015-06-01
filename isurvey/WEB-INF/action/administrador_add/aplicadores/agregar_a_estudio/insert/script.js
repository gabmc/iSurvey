alert ('El Aplicador fue agregado al Estudio');
addNew();
search(${fld:id_empresa});
document.getElementById('id_empresa_search').value = ${fld:id_empresa};
document.getElementById('id_empresa_name_search').value = "${fld:nombre_empresa}";