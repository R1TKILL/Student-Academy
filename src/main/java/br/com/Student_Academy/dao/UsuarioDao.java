package br.com.Student_Academy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.Student_Academy.model.Usuario;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {
	
	@Query("select R from Usuario R where R.email = :email")	
	public Usuario findByEmail(String email);
	
	@Query("select R from Usuario R where R.user = :user and R.senha = :senha")	
	public Usuario verifyLogin(String user, String senha);

}
