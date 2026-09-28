CREATE TABLE tb_eventos (
    id BINARY(16) NOT NULL,
    nome VARCHAR(150) NOT NULL,
    dataHoraEvento DATETIME NOT NULL,
    local VARCHAR(200) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado CHAR(2) NOT NULL,
    qtde INTEGER NOT NULL,

    PRIMARY KEY (id)
);