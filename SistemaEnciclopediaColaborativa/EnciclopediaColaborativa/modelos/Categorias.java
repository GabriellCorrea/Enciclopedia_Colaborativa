package modelos;

public enum Categorias {
    ARTE("Arte"), BIOGRAFIA("Biografia"), CIENCIA("Ciência"), ESPORTES("Esportes"), FILOSOFIA("Filosofia"),
    GEOGRAFIA("Geografia"), HISTORIA("História"), LITERATURA("Literatura"), POLITICA("Política"), TECNOLOGIA("Tecnologia"),
    INDEFINIDO("Indefinido");

    private String nome;

    Categorias(String nome) {
        this.nome = nome;
    }

    public String getNomeCategoria() {
        return nome;
    }

    public static Categorias getCategoriaPorString(String nome) {
        switch (nome) {
            case "Arte" -> { return ARTE; }
            case "Biografia" -> { return BIOGRAFIA; }
            case "Ciência" -> { return CIENCIA; }
            case "Esportes" -> { return ESPORTES; }
            case "Filosofia" -> { return FILOSOFIA; }
            case "Geografia" -> { return GEOGRAFIA; }
            case "História" -> { return HISTORIA; }
            case "Literatura" -> { return LITERATURA; }
            case "Política" -> { return POLITICA; }
            case "Tecnologia" -> { return TECNOLOGIA; }
            default -> {
                return INDEFINIDO;
            }
        }
    }
}
