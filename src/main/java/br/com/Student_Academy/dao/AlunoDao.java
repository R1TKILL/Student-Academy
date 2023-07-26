package br.com.Student_Academy.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.Student_Academy.model.Aluno;

public interface AlunoDao extends JpaRepository<Aluno, Integer> {
	//Interface criada com unico propósito de, me permitir utlizar os métodos do JPA.
		
	/* Também usarei o JPQL (JAVA Percistence Query Lang) nesta interface para fazer as pesquisas
	 * padronizadas no database, atraves de métodos padronizando algo.*/
	
	
	//Nesta anotação posso executar um comando SQL com uma variavel, exemplo R de (R1TKILL).
	
	@Query(" select R from Aluno R where R.status = 'ATIVO' ")
	public List<Aluno> findByStatusAtivo();
	
	@Query(" select R from Aluno R where R.status = 'INATIVO' ")
	public List<Aluno> findByStatusInativo();
	
	@Query(" select R from Aluno R where R.status = 'TRANCADO' ")
	public List<Aluno> findByStatusTrancado();
	
	@Query(" select R from Aluno R where R.status = 'CANCELADO' ")
	public List<Aluno> findByStatusCancelado();
	
	/* O JPA permite que com isso, façamos as pesquisas por nome de qualquer maneira
	 * ex: (Guilherme, gUILHERME, guilherme, GUILHERME, GuIlHErmE), só  não pode escrever
	 * errado o nome.*/
	public List<Aluno> findByNomeContainingIgnoreCase(String nome);
	
}
