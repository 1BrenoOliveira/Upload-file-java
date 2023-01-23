package breno.dev.ProjetoUploadFile.repository;

import org.springframework.data.repository.CrudRepository;

import breno.dev.ProjetoUploadFile.model.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, Integer> {
	Produto save(Produto produto);
	Produto findById(long id);
}
