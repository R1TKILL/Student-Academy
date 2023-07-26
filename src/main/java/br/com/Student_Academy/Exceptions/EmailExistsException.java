package br.com.Student_Academy.Exceptions;

public class EmailExistsException extends Exception{
	
	/* Como disse que iria criar uma exceção, aqui ela tem a função de exibir a mensagem,
	 * abaixo, por fim a serialização definida de antemão para novas versões desta classe
	 * se manterem conpativeis umas com as outras, principalmente em produção. */

	public EmailExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
		
	private static final long serialVersionUID = 1L;
	
}
