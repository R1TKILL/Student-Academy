package br.com.Student_Academy.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.Student_Academy.Exceptions.ServiceExceptionEntrance;
import br.com.Student_Academy.Exceptions.CriptoExistException;
import br.com.Student_Academy.Exceptions.EmailExistsException;
import br.com.Student_Academy.dao.UsuarioDao;
import br.com.Student_Academy.model.Usuario;
import br.com.Student_Academy.utlil.Util;


/*Esta é a anotação para dizer que esta classe é um Service.*/
@Service
public class ServiceUsuario {
	
	/* Este método vai salvar no lugar do JPA como nos outros métodos que salvam, vou substituir
	 * apenas em Usuario, pois é necessario para a criptofia das senhas, e para verifiacar se os
	 * email passados já existem.*/
	
	@Autowired
	private UsuarioDao userRepositorio;
	
	public void salvarUsuario(Usuario usuario) throws Exception{
		try {
			
			//Aqui eu chamei a exceção que vou criar, para os email repetidos.
			if(userRepositorio.findByEmail(usuario.getEmail()) != null) {
				throw new EmailExistsException("Já existe um email cadastrado para: " + usuario.getEmail());
			}
			
			//E aqui vou criptografar de fato e tratar caso ouver erro na criptografia.
			usuario.setSenha(Util.md5(usuario.getSenha()));
			
		} catch (NoSuchAlgorithmException e) {
			throw new CriptoExistException("Ocorreu um erro ao criptografar a senha!");
		}
		
		//Aqui ainda uso o JPA pra salvar, mas só depois da verificação de email e criptografia.
		userRepositorio.save(usuario);
	}
	
	
	//Método para obter o user e a senha.
	public Usuario loginUser(String user, String senha) throws ServiceExceptionEntrance{	
		Usuario userLogin = userRepositorio.verifyLogin(user, senha);
		return userLogin;
	}
}
