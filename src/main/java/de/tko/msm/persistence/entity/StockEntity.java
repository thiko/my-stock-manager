package de.tko.msm.persistence.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;


@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@SuperBuilder
public class StockEntity extends AbstractStockManagerEntity {

    String wkn;
    String isin;
    String name;
    String customName;

    @Override
    public Document toDocument() {
        val doc = new Document();
        if (uuid != null && !uuid.isEmpty())
            doc.append("uuid", uuid);

        // TODO: validate / exception if wkn/isin/name is not present?

        doc.append("wkn", ObjectUtils.defaultIfNull(wkn, ""));
        doc.append("isin", ObjectUtils.defaultIfNull(isin, ""));
        doc.append("name", ObjectUtils.defaultIfNull(name, ""));
        doc.append("customName", ObjectUtils.defaultIfNull(customName, ""));

        return doc;
    }
}
