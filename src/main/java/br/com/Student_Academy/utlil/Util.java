package br.com.Student_Academy.utlil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	
	/* Para um resumo bem simples, a classe util serve para implementar métodos Utéis, mas
	 * a frente sera percebtivel a presença e importancia desses métodos, nesse vou fazer
	 * a criptografia da senha dos usuarios no banco, com o padrão MD5, para TESTES, em um
	 * banco real a criptografia sera mais especializada.*/
	
	 public static String md5(String senha) throws NoSuchAlgorithmException{
		 //Aqui vou traduzir a mensagem para MD5.
		 MessageDigest md = MessageDigest.getInstance("MD5");
		 BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
		 return hash.toString(16);
	 }

}
