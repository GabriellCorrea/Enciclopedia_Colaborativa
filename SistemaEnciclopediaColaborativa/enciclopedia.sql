CREATE SCHEMA enciclopedia;
USE enciclopedia;

-- Usuario
CREATE TABLE Usuario (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nomeUsuario VARCHAR(100),
    dtNascimento DATE,
    emailUsuario VARCHAR(50),
    senhaUsuario VARCHAR(100),
    avaliacao FLOAT
);

-- PaginaPrincipal
CREATE TABLE PaginaPrincipal (
    idPaginaPrincipal INT AUTO_INCREMENT PRIMARY KEY
);

-- Artigo (1:N com Pagina Principal)
CREATE TABLE Artigo (
    idArtigo INT AUTO_INCREMENT PRIMARY KEY,
    tituloArtigo VARCHAR(30),
    categoria VARCHAR(50),
    dtUltimaMod DATE,
    idPaginaPrincipal INT,
    FOREIGN KEY (idPaginaPrincipal) REFERENCES PaginaPrincipal(idPaginaPrincipal)
);

-- PaginaArtigo (1:1 com Artigo)
CREATE TABLE PaginaArtigo (
    idPaginaArtigo INT AUTO_INCREMENT PRIMARY KEY,
    idArtigo INT,
    FOREIGN KEY (idArtigo) REFERENCES Artigo(idArtigo)
);

-- Tabela relacionamento: Contem (N:N com PaginaPrincipal - Artigo)
CREATE TABLE Contem (
	idPaginaPrincipal INT,
    idArtigo INT,
    PRIMARY KEY (idPaginaPrincipal, idArtigo),
    FOREIGN KEY (idPaginaPrincipal) REFERENCES PaginaPrincipal(idPaginaPrincipal),
    FOREIGN KEY (idArtigo) REFERENCES Artigo(idArtigo)
);


-- Tabela relacionamento: Escreve (N:N com Usuário - Artigo)
CREATE TABLE Escreve (
    idArtigo INT,
    idUsuario INT,
    PRIMARY KEY (idArtigo, idUsuario),
    FOREIGN KEY (idArtigo) REFERENCES Artigo(idArtigo),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);

-- Topico (1:N com Artigo)
CREATE TABLE Topico (
    idTopico INT AUTO_INCREMENT PRIMARY KEY,
    nomeTopico VARCHAR(30),
    textoTopico VARCHAR(10000),
    idArtigo INT,
    FOREIGN KEY (idArtigo) REFERENCES Artigo(idArtigo)
);

-- Comentario (1:N com Artigo)(1:N com Usuário)(1:N com Pagina Artigo)
CREATE TABLE Comentario (
    idComentario INT AUTO_INCREMENT PRIMARY KEY,
    avaliacao INT CHECK (avaliacao BETWEEN 0 AND 5),
    texto VARCHAR(300),
    idArtigo INT,
    idUsuario INT,
    idPaginaArtigo INT,
    FOREIGN KEY (idArtigo) REFERENCES Artigo(idArtigo),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario),
    FOREIGN KEY (idPaginaArtigo) REFERENCES PaginaArtigo(idPaginaArtigo)
);