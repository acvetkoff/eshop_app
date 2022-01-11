package bg.softuni.eshop.product.model.entity;

import bg.softuni.eshop.product.model.enums.ProductType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

import static bg.softuni.eshop.product.model.enums.ProductType.GAME;

@Data
@Entity
@Table(name = "games")
public class GameEntity extends Product {

    @Column(name = "company")
    private String company;

    @ManyToMany
    private Set<PlatformEntity> platforms;

    public GameEntity() {
        this.setType(GAME);
    }

    @Override
    public ProductType getType() {
        return GAME;
    }

    public void addPlatform(PlatformEntity platformEntity){
        this.platforms.add(platformEntity);
    }
}
