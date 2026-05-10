CREATE TABLE pedido_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_itens_pedido
        FOREIGN KEY (pedido_id) REFERENCES pedido(id),

    CONSTRAINT fk_itens_produtos
        FOREIGN KEY (produto_id) REFERENCES produtos(id)
);