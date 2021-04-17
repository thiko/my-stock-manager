package de.tko.msm.persistence.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.bson.Document;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@SuperBuilder
public abstract class AbstractStockManagerEntity {

    @EqualsAndHashCode.Include
    String uuid;

    public abstract Document toDocument();
}
