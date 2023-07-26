package br.com.Student_Academy.model;

import br.com.Student_Academy.Enums.Curso;
import br.com.Student_Academy.Enums.Status;
import br.com.Student_Academy.Enums.Turno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
 * Informando ao spring que esta classe é uma entidade do objeto aluno, por isto a importação a cima
 * para fazer persistência dos dados desta classe no banco, a @Column serve pra dizer que é uma coluna,
 * nela especificamos nome e tamanho etc...
 */

@Entity
public class Aluno {
	
	/*@Id diz quem é o indentificador da classe, já o  @GeneratedValue serve para dizer que ele é auto
	 * incrementavel, geralmente neste a estrategia usada é para incrementar automatico o valor do id,
	 * ou seja criando um database no terminal ou não, não precisa criar a tabela pois já lhe informamos
	 * que a classe é a tabela e seus Atributos as colunas que não são nulas, bem prático.*/
	
	/* @NotNull = Verifica se o campo é nulo mas não se for vazio.
	 * @NotEmpty = Verifica se esta nulo ou vazio, mas não o tamanho do campo.
	 * @NotBlank = Verifica se o campo é vazio, nulo e o tamanho.*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "nome")
	@Size(min = 5, max = 70, message = "O nome deve conter no minimo 5 caracteres.")
	@NotBlank(message = "Este campo não pode ser vazio.")
	private String nome;
		
	@Column(name = "curso")
	@Enumerated(EnumType.STRING) //Convertendo para string antes de salvar no banco.
	@NotNull(message = "Este campo não pode ser vazio.")
	private Curso curso;
	
	@Column(name = "matricula")
	@NotNull( message = "Clique no Botão Gerar!")
	@Size(min = 3, message = "Clique no Botão Gerar!")
	private String matricula;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Este campo não pode ser vazio.")
	private Status status;
	
	@Column(name = "turno")
	@Enumerated(EnumType.STRING)
	@NotNull(message = "Este campo não pode ser vazio.")
	private Turno turno;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}


	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Turno getTurno() {
		return turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}		
}


