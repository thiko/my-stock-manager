package de.tko.msm.persistence;

import com.mongodb.client.MongoCollection;
import de.tko.msm.persistence.entity.AbstractStockManagerEntity;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public interface IStockManagerRepository<T extends AbstractStockManagerEntity> {


    default List<T> getAll() {
        val result = new ArrayList<T>();

        try (val cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                val document = cursor.next();
                result.add(modelFromDocument(document));
            }
        }

        return result;
    }


    default Optional<T> getResultByUuid(String uuid) {
        val doc = getCollection().find().filter(eq("uuid", uuid)).first();
        if (doc == null) {
            return Optional.empty();
        }
        return Optional.of(modelFromDocument(doc));
    }

    default Optional<T> getResultById(ObjectId id) {
        val doc = getCollection().find().filter(eq("_id", id)).first();
        if (doc == null) {
            return Optional.empty();
        }
        return Optional.of(modelFromDocument(doc));
    }

    default Optional<T> insertOrReplace(T entity) {
        String uuid = "";
        boolean insertNewEntry = false;
        if (entity.getUuid() != null && !entity.getUuid().isEmpty()) {
            uuid = entity.getUuid();
        } else {
            uuid = UUID.randomUUID().toString();
            insertNewEntry = true;
        }

        val document = entity.toDocument();
        document.append("uuid", uuid);

        if (insertNewEntry) {
            val updatedEntryId = Objects
                    .requireNonNull(getCollection().insertOne(document).getInsertedId());
            return getResultById(updatedEntryId.asObjectId().getValue());
        } else {
            getCollection().replaceOne(eq("uuid", uuid), document);
            return getResultByUuid(uuid);
        }
    }

    T modelFromDocument(Document document);

    MongoCollection<Document> getCollection();
}
