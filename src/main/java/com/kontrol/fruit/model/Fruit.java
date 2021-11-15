package com.kontrol.fruit.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@SecondaryTable(
        name = "fruit_nutrition",
        pkJoinColumns =@PrimaryKeyJoinColumn(name = "fruit_id", referencedColumnName = "id")
)
public class Fruit extends PanacheEntityBase {
    @Id
    @NotNull
    public Long id;

    @NotBlank
    @NotEmpty
    public String name;

    public String genus;

    public String family;

    @Column(name = "\"order\"") //escape to prevent being treated as sql keyword 'order'
    public String order;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "carbohydrates", column = @Column(name = "carbohydrates", table = "fruit_nutrition")),
            @AttributeOverride(name = "protein", column = @Column(name = "protein", table = "fruit_nutrition")),
            @AttributeOverride(name = "fat", column = @Column(name = "fat", table = "fruit_nutrition")),
            @AttributeOverride(name = "calories", column = @Column(name = "calories", table = "fruit_nutrition")),
            @AttributeOverride(name = "sugar", column = @Column(name = "sugar", table = "fruit_nutrition"))
    })
    public Nutrition nutritions;
}
