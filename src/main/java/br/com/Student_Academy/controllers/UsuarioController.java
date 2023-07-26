package br.com.Student_Academy.controllers;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.Student_Academy.Exceptions.ServiceExceptionEntrance;
import br.com.Student_Academy.dao.UsuarioDao;
import br.com.Student_Academy.model.Usuario;
import br.com.Student_Academy.service.EmailService;
import br.com.Student_Academy.service.ServiceUsuario;
import br.com.Student_Academy.utlil.Util;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

//Apenas informo ao Spring que na arquitetura MVC esta classe é um controller ou seja uma rota.
@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioDao usuarioRepository;
	
	@Autowired
	private ServiceUsuario serviceUsuario;
	
	@Autowired
	private EmailService emailService;
	
	private String acesslog;
	
	//Página inicial/raiz do projeto, usa get para requisitarmos sua leitura e exibição.
	@GetMapping("/")
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("usuario", new Usuario());
		/* Aqui basicamente, instâncio o objeto ModelAndView e digo qual arquivo estatico ele
		 * vai buscar/representar*/
		mv.setViewName("Login/login");
		return mv;
	}
		
	@GetMapping("index")
	public ModelAndView index(HttpSession session) {		
		ModelAndView mv = new ModelAndView();
		
		//Verificando a sessão para permitir o acesso, sem sessão sem acesso.
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("home/index");
		}catch (NullPointerException e) {
			return login();
		}		
		
		return mv;		
	}
	
	@GetMapping("/cadastro")
	public ModelAndView cadastrar() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("usuario", new Usuario());
		mv.setViewName("Login/cadastro");
		return mv;
	}
	
	@PostMapping("salvarUsuario")
	public ModelAndView cadastrar(@Valid Usuario usuario, BindingResult br) throws Exception {
		ModelAndView mv = new ModelAndView();
		
		if(br.hasErrors()) {
			mv.setViewName("Login/cadastro");
		}
		
		/*Se o usuario já existe ou se os campos tão vazio.*/
		if(usuarioRepository.findByEmail(usuario.getEmail()) != null) {
			mv.addObject("msg", "Já existe um email cadastrado para: " + usuario.getEmail());
		}else {
			//Salvando o usuario no banco.
			serviceUsuario.salvarUsuario(usuario);
			
			//Tentando enviar o e-mail com o nome e conteudo após salvar o usuario.
			String content = emailService.contentMail(usuario.getUser());
			emailService.sendEmailToClient("Cadastro no Sistema", usuario.getEmail(), content);
			
			//redirecionando pro login.
			mv.setViewName("redirect:/");
		}
		
		return mv;
	}
	
	/*Rota que verifica as informações recebidas do google, se não existir, cadastra e redireciona,
	 *mas se existir compara, é igual?, se sim entra, se não volta pro logn com mensagem.*/
	@PostMapping("loginGoogle")
	public ModelAndView googleLogin(@Valid Usuario usuario, BindingResult br, HttpSession session) throws NoSuchAlgorithmException, ServiceExceptionEntrance {
		ModelAndView mv = new ModelAndView();
		Usuario userLogin = serviceUsuario.loginUser(usuario.getUser(), Util.md5(usuario.getSenha()));
		
		if(usuarioRepository.findByEmail(usuario.getEmail()) != null) {
			//Se o usuário já existe.
			try {
				session.setAttribute("usuarioLogado", userLogin);
				return index(session);
			} catch (NullPointerException e) {
				mv.addObject("msg", "As credencias não coincidem, verifique se o email já foi cadastrado.");
			}
		}else {
			//Se o usuário não existe.
			try {
				serviceUsuario.salvarUsuario(usuario);
				
				String content = emailService.contentMail(usuario.getUser());
				emailService.sendEmailToClient("Cadastro no Sistema", usuario.getEmail(), content);
				
				session.setAttribute("usuarioLogado", userLogin);
				return index(session);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return mv;
	}
	
	@PostMapping("/login")
	public ModelAndView login(@Valid Usuario usuario, BindingResult br, HttpSession session) throws NoSuchAlgorithmException, ServiceExceptionEntrance {
		ModelAndView mv = new ModelAndView();
		
		if(br.hasErrors()) {
			mv.setViewName("Login/login");
		}
				
		Usuario userLogin = serviceUsuario.loginUser(usuario.getUser(), Util.md5(usuario.getSenha()));
		if(userLogin == null) {
			mv.addObject("msg", "Usuário não encontrado, verifique se o nome ou a senha estão incorretos.");
		}else {
			session.setAttribute("usuarioLogado", userLogin);
			return index(session);
		}		
		return mv;
	}
	
	/*Aqui vamos fazer a invalidação da sessaõ do usuário*/
	@PostMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		return login();
	}
}

