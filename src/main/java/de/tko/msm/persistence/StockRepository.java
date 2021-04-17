package de.tko.msm.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import de.tko.msm.persistence.entity.StockEntity;
import lombok.val;
import org.bson.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.or;

@ApplicationScoped
public class StockRepository implements IStockManagerRepository<StockEntity> {

    @Inject
    private MongoClient mongoClient;

    public Optional<StockEntity> getStockByWknOrIsin(String wknOrIsin) {
        val doc = getCollection().find().filter(or(eq("wkn", wknOrIsin), eq("isin", wknOrIsin))).first();
        if (doc == null) {
            return Optional.empty();
        }
        return Optional.of(modelFromDocument(doc));
    }

    @Override
    public StockEntity modelFromDocument(Document document) {
        return StockEntity.builder()
                .uuid(document.getString("uuid"))
                .wkn(document.getString("wkn"))
                .isin(document.getString("isin"))
                .name(document.getString("name"))
                .customName(document.getString("customName"))
                .build();
    }

    @Override
    public MongoCollection<Document> getCollection() {
        // TODO: Shift db and collection name in applications file @ConfigProperty(name = "greeting.message")
        return mongoClient.getDatabase("my-stock-manager").getCollection("stocks");
    }
}
