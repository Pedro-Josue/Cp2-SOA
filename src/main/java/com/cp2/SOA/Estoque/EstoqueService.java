package com.cp2.SOA.Estoque;

import com.cp2.SOA.Produto.Model.Produto;
import com.cp2.SOA.Produto.Service.ProdutoService;
import com.cp2.SOA.Shared.Exception.EstoqueInsuficienteException;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {

    private final ProdutoService produtoService;

    public EstoqueService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void validarEstoque(Long produtoId, Integer quantidade) {
        Produto produto = produtoService.buscarPorId(produtoId);

        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para produto " + produto.getNome() +
                            ". Disponível: " + produto.getQuantidadeEstoque()
            );
        }
    }

    public void baixarEstoque(Long produtoId, Integer quantidade) {
        Produto produto = produtoService.buscarPorId(produtoId);

        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new EstoqueInsuficienteException(
                    "Não foi possível baixar estoque. Produto: " + produto.getNome()
            );
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);
        produtoService.salvar(produto);
    }
}
