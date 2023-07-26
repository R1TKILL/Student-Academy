package br.com.Student_Academy.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.Student_Academy.dao.AlunoDao;
import br.com.Student_Academy.model.Aluno;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AlunoController {
	
	/* O spring me permite faer a injeção de depêndencias com apenas esta anotação sem precisar
	 * instânciar a interface e fazer códigos repetidos só pra funcionar.*/
	
	@Autowired
	private AlunoDao alunoRepository;
	
	@Autowired
	private UsuarioController usuariocontroller;
	
	private String acesslog;
	
	/* Atravez desta classe que iremos pegar os dados do front para redirecionar para a paágina de 
	 * inserir um novo Aluno.*/
	
	@GetMapping("/inserirAlunos")
	public ModelAndView redirectAlunos(Aluno aluno, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/formAluno");
			//Assim que poderei chamar o objeto aluno no front, (nome, o que representa).
			mv.addObject("aluno", new Aluno());
		} catch (NullPointerException e) {
			return usuariocontroller.login();
		}
		return mv;
	}
	
	/* Com o @PostMapping estamos recebendo esses alunos criados e mandando para listAlunos e salvando
	 * no banco de dados com JPA atraves do objeto com a interface, jáo @Valid esta me trazendo a validação
	 * de error que especifiquei na classe Aluno com as validações.*/

	
	@PostMapping("InsertAluno")
	public ModelAndView InsertAlunos(@Valid Aluno aluno, BindingResult br) {
		ModelAndView mv = new ModelAndView();	
		if(br.hasErrors()) {
			mv.setViewName("Aluno/formAluno");
			mv.addObject("aluno");
		}else {
			mv.setViewName("redirect:/listagemAlunos");
			alunoRepository.save(aluno);
		}
		return mv;
	}
	
	@GetMapping("listagemAlunos")
	public ModelAndView listAlunos(Aluno aluno, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/listAlunos");
			/* Estou fazendo uma busca completa do que há no banco de dados,
			 * listando e armazenando em alunosList.*/
			mv.addObject("alunosList", alunoRepository.findAll());
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}
		
		return mv;
	}
	
	/* No método para pegar o id e mandar para fazer alterações nos alunos cadastrados no banco,
	 * @PathVariable esta pegando o meu campo id e o atribuindo a variavel id que eu criei, que
	 * repasso para o objeto do tipo Aluno abaixo o repassando tambem a uma nova variavel para 
	 * a view.*/
	
	/*A SESSÃO CHAMA CERTO, MAS O CSS NÃO E RECONHECIDO NÃO SEI PORQUE!!!*/
	@GetMapping("/alterar/{id}")
	public ModelAndView AlterAluno(@PathVariable("id") Integer id, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/alterAlunos");
			Aluno aluno = alunoRepository.getReferenceById(id);
			mv.addObject("aluno", aluno);
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}		
		return mv;
	}
	
	@PostMapping("/alterar")
	public ModelAndView AlterAluno(Aluno aluno) {
		ModelAndView mv = new ModelAndView();
		alunoRepository.save(aluno);
		mv.setViewName("redirect:/listagemAlunos");
		return mv;
	}
	
	//Pego o id e deleto pelo id, sem página desnecessária e retorno.
	
	/*A SESSÃO CHAMA CERTO, MAS O CSS NÃO E RECONHECIDO NÃO SEI PORQUE!!!*/
	@GetMapping("/excluir/{id}")
	public ModelAndView deleteAluno(@PathVariable("id") Integer id, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			alunoRepository.deleteById(id);
			mv.setViewName("redirect:/listagemAlunos");
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}
		return mv;
	}
	
	//Adicionei um Objeto aluno necessario para fazer as pesquisas e separar o que quero obter
	@GetMapping("filtroAlunos")
	public ModelAndView filterAlunos(HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/filterAlunos");
			mv.addObject("aluno", new Aluno());
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}
		return mv;
	}
	
	
	//Para as pesquisas dos alunos padronizadas.
	
	@GetMapping("alunos-ativos")
	public ModelAndView listaAlunosAtivos(Aluno aluno, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/listAlunosActivate");
			mv.addObject("alunosAtivos", alunoRepository.findByStatusAtivo());
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}		
		return mv;
	}
	
	@GetMapping("alunos-inativos")
	public ModelAndView listaAlunosInativos(Aluno aluno, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/listAlunosInactive");
			mv.addObject("alunosInativos", alunoRepository.findByStatusInativo());
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}		
		return mv;
	}
	
	@GetMapping("alunos-trancados")
	public ModelAndView listaAlunosTrancados(Aluno aluno, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/listAlunosBraided");
			mv.addObject("alunosTrancados", alunoRepository.findByStatusTrancado());
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}		
		return mv;
	}
	
	@GetMapping("alunos-cancelados")
	public ModelAndView listaAlunosCancelados(Aluno aluno, HttpSession session) {
		ModelAndView mv = new ModelAndView();
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			mv.setViewName("Aluno/listAlunosCanceled");
			mv.addObject("alunosCancelados", alunoRepository.findByStatusCancelado());
		}catch (NullPointerException e) {
			return usuariocontroller.login();
		}	
		return mv;
	}
	
	// Esta lista vai receber o resultado do DAO e vamos repasar.
	private List<Aluno> listaNomesAlunos;
	
	//Para as pesquisas dos alunos por nomes que recebo da view pelo @requestParam, não obrigatorio.
	@PostMapping("pesquisar-aluno")
	public ModelAndView SearchAlunos(@RequestParam(required = false) String nome) {
		ModelAndView mv = new ModelAndView();
		
		if(nome == null || nome.trim().isEmpty()) {
			listaNomesAlunos = alunoRepository.findAll();
		}else {
			listaNomesAlunos = alunoRepository.findByNomeContainingIgnoreCase(nome);
		}
		
		mv.addObject("listaNomes", listaNomesAlunos);
		mv.setViewName("Aluno/resultSearchName");
		return mv;
	}
	
	/* Parte responsavel pelos relatorios dos alunos cadastrados em PDF, futuramente junto com o
	 * usuário queo cadastrou.*/
	
	@GetMapping("relatorio-ativos")
	public ModelAndView reportActivate(HttpServletResponse response, HttpSession session) {		
		ModelAndView mv = new ModelAndView();
		Document dmt = new Document();	
		
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			/*1- tipo do doc, 2 - nome do doc, 3 - cria o doc, 4 - abrir o doc para escrever*/		
			response.setContentType("apllication/pdf");
			response.addHeader("Content-Disposition", "inline; filename="+"Alunos Ativos.pdf");
			PdfWriter.getInstance(dmt, response.getOutputStream());
			dmt.open();
			
			//Fontes.
			Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD | Font.UNDERLINE);
			Font f2 = new Font(Font.FontFamily.TIMES_ROMAN,11);
			Font f3 = new Font(Font.getFamily("Arial"),12,Font.BOLD);
			
			//Esta classe image deve ser impotada do itext.
			Image imgLogo = Image.getInstance("../Student_Academy/src/main/resources/static/img/logo_Oficial.png");
			imgLogo.scaleToFit(80, 80);
			imgLogo.setAlignment(1);
			dmt.add(imgLogo);
			dmt.add(new Paragraph(" "));//quebra de linha.
			dmt.add(new Paragraph(" "));		
			
			Paragraph ph1 = new Paragraph("Relatorio dos alunos ativos em Student Academy", f1);
			ph1.setAlignment(1);
			dmt.add(ph1);
			dmt.add(new Paragraph(" "));
			
			//Numero de colunas na tabela e suas larguras.
			float widthCol[] = {30f, 30f, 25f, 25f, 15f};
			
			PdfPTable dmtTable = new PdfPTable(widthCol);
			
			//Cabeçalho da tabela
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome", f3));
			PdfPCell col2 = new PdfPCell(new Paragraph("Curso", f3));
			PdfPCell col3 = new PdfPCell(new Paragraph("Matricula", f3));
			PdfPCell col4 = new PdfPCell(new Paragraph("Status", f3));
			PdfPCell col5 = new PdfPCell(new Paragraph("Turno", f3));
			
			//Espessura das bordas.
			col1.setBorderWidth(2);
			col2.setBorderWidth(2);
			col3.setBorderWidth(2);
			col4.setBorderWidth(2);
			col5.setBorderWidth(2);
			
			//Adicionando as colunas na tabela com cor junto.
			dmtTable.addCell(col1).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col2).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col3).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col4).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col5).setBackgroundColor(new BaseColor(187, 88, 87));
			
			//Populando a tabela com os dados dos alunos.
			List<Aluno> list = alunoRepository.findByStatusAtivo();
			BaseColor color;
			for(int i = 0; i < list.size(); i++) {			
				if(i % 2 == 0){color = new BaseColor(253, 185, 149);}else{color = new BaseColor(254, 220, 201);}	
				
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getNome().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getCurso().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getMatricula().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getStatus().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getTurno().toString(), f2))).setBackgroundColor(color);
			}
			
			dmt.add(dmtTable);
			dmt.close();
			
		} catch (DocumentException e) {
			dmt.close();
		}catch (IOException e) {
			dmt.close();
		}catch (NullPointerException e) {
			dmt.close();
			return usuariocontroller.login();
		}
		
		return mv;
	}
	
	@GetMapping("relatorio-inativos")
	public ModelAndView reportInactivate(HttpServletResponse response, HttpSession session) {		
		ModelAndView mv = new ModelAndView();
		Document dmt = new Document();	
		
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			response.setContentType("apllication/pdf");
			response.addHeader("Content-Disposition", "inline; filename="+"Alunos Inativos.pdf");
			PdfWriter.getInstance(dmt, response.getOutputStream());
			dmt.open();
			
			Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD | Font.UNDERLINE);
			Font f2 = new Font(Font.FontFamily.TIMES_ROMAN,11);
			Font f3 = new Font(Font.getFamily("Arial"),12,Font.BOLD);
			
			Image imgLogo = Image.getInstance("../Student_Academy/src/main/resources/static/img/logo_Oficial.png");
			imgLogo.scaleToFit(80, 80);
			imgLogo.setAlignment(1);
			dmt.add(imgLogo);
			dmt.add(new Paragraph(" "));
			dmt.add(new Paragraph(" "));		
			
			Paragraph ph1 = new Paragraph("Relatorio dos alunos inativos em Student Academy", f1);
			ph1.setAlignment(1);
			dmt.add(ph1);
			dmt.add(new Paragraph(" "));
			
			float widthCol[] = {30f, 30f, 25f, 25f, 15f};		
			PdfPTable dmtTable = new PdfPTable(widthCol);
			
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome", f3));
			PdfPCell col2 = new PdfPCell(new Paragraph("Curso", f3));
			PdfPCell col3 = new PdfPCell(new Paragraph("Matricula", f3));
			PdfPCell col4 = new PdfPCell(new Paragraph("Status", f3));
			PdfPCell col5 = new PdfPCell(new Paragraph("Turno", f3));
			
			col1.setBorderWidth(2);
			col2.setBorderWidth(2);
			col3.setBorderWidth(2);
			col4.setBorderWidth(2);
			col5.setBorderWidth(2);
			
			dmtTable.addCell(col1).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col2).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col3).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col4).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col5).setBackgroundColor(new BaseColor(187, 88, 87));
			
			List<Aluno> list = alunoRepository.findByStatusInativo();
			BaseColor color;
			for(int i = 0; i < list.size(); i++) {			
				if(i % 2 == 0){color = new BaseColor(253, 185, 149);}else{color = new BaseColor(254, 220, 201);}	
				
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getNome().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getCurso().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getMatricula().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getStatus().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getTurno().toString(), f2))).setBackgroundColor(color);
			}
			
			dmt.add(dmtTable);
			dmt.close();
			
		} catch (DocumentException e) {
			dmt.close();
		}catch (IOException e) {
			dmt.close();
		}catch (NullPointerException e) {
			dmt.close();
			return usuariocontroller.login();
		}
		
		return mv;
	}
	
	@GetMapping("relatorio-trancados")
	public ModelAndView reportBraindeds(HttpServletResponse response, HttpSession session) {		
		ModelAndView mv = new ModelAndView();
		Document dmt = new Document();	
		
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			response.setContentType("apllication/pdf");
			response.addHeader("Content-Disposition", "inline; filename="+"Alunos Trancados.pdf");
			PdfWriter.getInstance(dmt, response.getOutputStream());
			dmt.open();
			
			Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD | Font.UNDERLINE);
			Font f2 = new Font(Font.FontFamily.TIMES_ROMAN,11);
			Font f3 = new Font(Font.getFamily("Arial"),12,Font.BOLD);
			
			Image imgLogo = Image.getInstance("../Student_Academy/src/main/resources/static/img/logo_Oficial.png");
			imgLogo.scaleToFit(80, 80);
			imgLogo.setAlignment(1);
			dmt.add(imgLogo);
			dmt.add(new Paragraph(" "));
			dmt.add(new Paragraph(" "));		
			
			Paragraph ph1 = new Paragraph("Relatorio dos alunos trancados em Student Academy", f1);
			ph1.setAlignment(1);
			dmt.add(ph1);
			dmt.add(new Paragraph(" "));
			
			float widthCol[] = {30f, 30f, 25f, 25f, 15f};		
			PdfPTable dmtTable = new PdfPTable(widthCol);
			
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome", f3));
			PdfPCell col2 = new PdfPCell(new Paragraph("Curso", f3));
			PdfPCell col3 = new PdfPCell(new Paragraph("Matricula", f3));
			PdfPCell col4 = new PdfPCell(new Paragraph("Status", f3));
			PdfPCell col5 = new PdfPCell(new Paragraph("Turno", f3));
			
			col1.setBorderWidth(2);
			col2.setBorderWidth(2);
			col3.setBorderWidth(2);
			col4.setBorderWidth(2);
			col5.setBorderWidth(2);
			
			dmtTable.addCell(col1).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col2).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col3).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col4).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col5).setBackgroundColor(new BaseColor(187, 88, 87));
			
			List<Aluno> list = alunoRepository.findByStatusTrancado();
			BaseColor color;
			for(int i = 0; i < list.size(); i++) {			
				if(i % 2 == 0){color = new BaseColor(253, 185, 149);}else{color = new BaseColor(254, 220, 201);}	
				
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getNome().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getCurso().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getMatricula().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getStatus().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getTurno().toString(), f2))).setBackgroundColor(color);
			}
			
			dmt.add(dmtTable);
			dmt.close();
			
		} catch (DocumentException e) {
			dmt.close();
		}catch (IOException e) {
			dmt.close();
		}catch (NullPointerException e) {
			dmt.close();
			return usuariocontroller.login();
		}
		
		return mv;
	}
	
	@GetMapping("relatorio-cancelados")
	public ModelAndView reportCanceleds(HttpServletResponse response, HttpSession session) {		
		ModelAndView mv = new ModelAndView();
		Document dmt = new Document();	
		
		try {	
			acesslog = session.getAttribute("usuarioLogado").toString();
			response.setContentType("apllication/pdf");
			response.addHeader("Content-Disposition", "inline; filename="+"Alunos Cancelados.pdf");
			PdfWriter.getInstance(dmt, response.getOutputStream());
			dmt.open();
			
			Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD | Font.UNDERLINE);
			Font f2 = new Font(Font.FontFamily.TIMES_ROMAN,11);
			Font f3 = new Font(Font.getFamily("Arial"),12,Font.BOLD);
			
			Image imgLogo = Image.getInstance("../Student_Academy/src/main/resources/static/img/logo_Oficial.png");
			imgLogo.scaleToFit(80, 80);
			imgLogo.setAlignment(1);
			dmt.add(imgLogo);
			dmt.add(new Paragraph(" "));
			dmt.add(new Paragraph(" "));		
			
			Paragraph ph1 = new Paragraph("Relatorio dos alunos cancelados em Student Academy", f1);
			ph1.setAlignment(1);
			dmt.add(ph1);
			dmt.add(new Paragraph(" "));
			
			float widthCol[] = {30f, 30f, 25f, 25f, 15f};		
			PdfPTable dmtTable = new PdfPTable(widthCol);
			
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome", f3));
			PdfPCell col2 = new PdfPCell(new Paragraph("Curso", f3));
			PdfPCell col3 = new PdfPCell(new Paragraph("Matricula", f3));
			PdfPCell col4 = new PdfPCell(new Paragraph("Status", f3));
			PdfPCell col5 = new PdfPCell(new Paragraph("Turno", f3));
			
			col1.setBorderWidth(2);
			col2.setBorderWidth(2);
			col3.setBorderWidth(2);
			col4.setBorderWidth(2);
			col5.setBorderWidth(2);
			
			dmtTable.addCell(col1).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col2).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col3).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col4).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col5).setBackgroundColor(new BaseColor(187, 88, 87));
			
			List<Aluno> list = alunoRepository.findByStatusCancelado();
			BaseColor color;
			for(int i = 0; i < list.size(); i++) {			
				if(i % 2 == 0){color = new BaseColor(253, 185, 149);}else{color = new BaseColor(254, 220, 201);}	
				
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getNome().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getCurso().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getMatricula().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getStatus().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getTurno().toString(), f2))).setBackgroundColor(color);
			}
			
			dmt.add(dmtTable);
			dmt.close();
			
		} catch (DocumentException e) {
			dmt.close();
		}catch (IOException e) {
			dmt.close();
		}catch (NullPointerException e) {
			dmt.close();
			return usuariocontroller.login();
		}
		
		return mv;
	}
	
	@GetMapping("relatorio-nomes")
	public ModelAndView reportNames(HttpServletResponse response, HttpSession session) {		
		ModelAndView mv = new ModelAndView();
		Document dmt = new Document();	
		
		try {
			acesslog = session.getAttribute("usuarioLogado").toString();
			response.setContentType("apllication/pdf");
			response.addHeader("Content-Disposition", "inline; filename="+"Alunos Pesquisados.pdf");
			PdfWriter.getInstance(dmt, response.getOutputStream());
			dmt.open();
			
			Font f1 = new Font(Font.FontFamily.TIMES_ROMAN,18,Font.BOLD | Font.UNDERLINE);
			Font f2 = new Font(Font.FontFamily.TIMES_ROMAN,11);
			Font f3 = new Font(Font.getFamily("Arial"),12,Font.BOLD);
			
			Image imgLogo = Image.getInstance("../Student_Academy/src/main/resources/static/img/logo_Oficial.png");
			imgLogo.scaleToFit(80, 80);
			imgLogo.setAlignment(1);
			dmt.add(imgLogo);
			dmt.add(new Paragraph(" "));
			dmt.add(new Paragraph(" "));		
			
			Paragraph ph1 = new Paragraph("Relatorio dos alunos pesquisados em Student Academy", f1);
			ph1.setAlignment(1);
			dmt.add(ph1);
			dmt.add(new Paragraph(" "));
			
			float widthCol[] = {30f, 30f, 25f, 25f, 15f};		
			PdfPTable dmtTable = new PdfPTable(widthCol);
			
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome", f3));
			PdfPCell col2 = new PdfPCell(new Paragraph("Curso", f3));
			PdfPCell col3 = new PdfPCell(new Paragraph("Matricula", f3));
			PdfPCell col4 = new PdfPCell(new Paragraph("Status", f3));
			PdfPCell col5 = new PdfPCell(new Paragraph("Turno", f3));
			
			col1.setBorderWidth(2);
			col2.setBorderWidth(2);
			col3.setBorderWidth(2);
			col4.setBorderWidth(2);
			col5.setBorderWidth(2);
			
			dmtTable.addCell(col1).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col2).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col3).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col4).setBackgroundColor(new BaseColor(187, 88, 87));
			dmtTable.addCell(col5).setBackgroundColor(new BaseColor(187, 88, 87));
			
			List<Aluno> list = listaNomesAlunos;
			BaseColor color;
			for(int i = 0; i < list.size(); i++) {			
				if(i % 2 == 0){color = new BaseColor(253, 185, 149);}else{color = new BaseColor(254, 220, 201);}	
				
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getNome().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getCurso().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getMatricula().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getStatus().toString(), f2))).setBackgroundColor(color);
				dmtTable.addCell(new PdfPCell(new Paragraph(list.get(i).getTurno().toString(), f2))).setBackgroundColor(color);
			}
			
			dmt.add(dmtTable);
			dmt.close();
			
		} catch (DocumentException e) {
			dmt.close();
		}catch (IOException e) {
			dmt.close();
		}catch (NullPointerException e) {
			dmt.close();
			return usuariocontroller.login();
		}		
		return mv;
	}
	
}


