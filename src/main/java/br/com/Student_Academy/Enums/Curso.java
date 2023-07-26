package br.com.Student_Academy.Enums;

public enum Curso {
	
	ADMINSTRACAO("Adminstração"),
	INFORMATICA("Informática"),
	CONTABILIDADE("Contabilidade"),
	PROGRAMACAO("Programação"),
	ENFERMAGEM("Enfermagem");


	private String curso;
	
	private Curso(String curso) {
		this.curso = curso;
	}
	
}
