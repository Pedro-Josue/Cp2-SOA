CREATE TABLE pagamentos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pedido_id BIGINT NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    forma_pagamento VARCHAR(50),
    data_pagamento TIMESTAMP,

    CONSTRAINT fk_pagamentos_pedidos
        FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);