package com.cp2.SOA.Produto.Service;

import com.cp2.SOA.Produto.Model.Produto;
import com.cp2.SOA.Produto.Repository.ProdutoRepository;
import com.cp2.SOA.Shared.Exception.ProdutoNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }
}
