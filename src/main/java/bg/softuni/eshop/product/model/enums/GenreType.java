package bg.softuni.eshop.product.model.enums;

public enum GenreType {
    ACTION("Action"),
    ADVENTURE("Adventure"),
    MYSTERY("Mystery"),
    HORROR("Horror"),
    DRAMA("Drama"),
    COMEDY("Comedy"),
    THRILLER("Thriller"),
    CRIME("Crime"),
    SCI_FI("Sci-Fi");

    private String type;

    GenreType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
