package breno.dev.ProjetoUploadFile.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import breno.dev.ProjetoUploadFile.model.Produto;
import breno.dev.ProjetoUploadFile.repository.ProdutoRepository;
/*
 * requisitos para funcionar...
 * - utilizar banco mysql
 * - criar o banco com nome "UploadFile"
 * 
 * */
@Controller
public class ArquivoController {
	@Autowired
	ProdutoRepository repositorioProduto;
	
	@RequestMapping(value="/form/arquivo", method = RequestMethod.GET)
	public ModelAndView form() {
		ModelAndView mv = new ModelAndView("/formArquivo");
		mv.addObject("produtos", repositorioProduto.findAll());
		return mv ;
	}
	@RequestMapping(value="/form/arquivo", method = RequestMethod.POST)
	public String Salvarform(Produto produto, @RequestParam("uploadImagem")MultipartFile file) {
		try {
			produto.setImagem(file.getBytes());
			produto.setNomeArquivo(file.getOriginalFilename());
		} catch (IOException e) {
			e.printStackTrace();
		}
		repositorioProduto.save(produto);
		return "redirect: /form/arquivo";
	}
	
	
	
	
	@GetMapping("imagem/{idprod}")//apresentar imagem em table
	@ResponseBody
	public byte[] exibirImagem(@PathVariable("idprod") long idprod) {
		Produto produto = repositorioProduto.findById(idprod);
		return produto.getImagem();
	}
	
	@RequestMapping(value = "/download/{idprod}", method = RequestMethod.GET)//baixar imagem
    public HttpEntity<byte[]> download(@PathVariable("idprod") long idprod ) throws IOException {
    	Produto produto = repositorioProduto.findById(idprod);
        byte[] arquivo = produto.getImagem();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Content-Disposition", "attachment;filename=\"Arquivo."+getFileExtension(produto.getNomeArquivo())+"\"");

        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(arquivo, httpHeaders);

        return entity;
    }

    static String getFileExtension(String filename) { //pegar so a extensão
        if (filename.contains("."))
            return filename.substring(filename.lastIndexOf(".") + 1);
        else
            return "";
    }
}
