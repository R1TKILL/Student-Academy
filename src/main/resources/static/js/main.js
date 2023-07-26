const MATRICULA = document.getElementById('matricula')

function GerarMatricula(){
	var prefixTXT = "STACA"
	var randomNumber = Math.floor(Math.random() * 150000)
	MATRICULA.value = (prefixTXT + randomNumber)
}