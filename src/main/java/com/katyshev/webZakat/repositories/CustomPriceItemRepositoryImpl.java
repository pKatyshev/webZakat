package com.katyshev.webZakat.repositories;

import com.katyshev.webZakat.models.PriceItem;
import jakarta.persistence.NamedQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomPriceItemRepositoryImpl implements CustomPriceItemRepository{

    private final SessionFactory sessionFactory;

    @Autowired
    public CustomPriceItemRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<PriceItem> findAllByQuery(String query) {

        try(Session session = sessionFactory.openSession()) {
            String[] words = query.split(" ");
            StringBuilder sql = new StringBuilder("SELECT * FROM price_item WHERE ");

            //создаем запрос
            for (int i = 0; i < words.length; i++) {
                sql.append(" name ILIKE ?");
                if (i < words.length - 1) {
                    sql.append(" AND ");
                }
            }

            NativeQuery<PriceItem> nativeQuery = session.createNativeQuery(sql.toString(), PriceItem.class);

            // сэттим параметры
            for (int i = 0; i < words.length; i++) {
                nativeQuery.setParameter(i + 1, "%" + words[i] + "%");
            }

            return nativeQuery.list();
        }
    }
}
