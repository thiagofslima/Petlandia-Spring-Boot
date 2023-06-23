package br.edu.toledoprudente.dao;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;

import br.edu.toledoprudente.pojo.Venda;
import jakarta.persistence.TypedQuery;

@Repository
public class VendaDAO extends AbstractDao<Venda, Integer> {

    // public BigDecimal findTotal(int id) {
    // BigDecimal total = this.createQuery("select c from Venda"
    // + " c where c.venda.id = ?1", id);
    // return total;
    // }

public long countVendas() {
    TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(v) FROM Venda v", Long.class);
    return query.getSingleResult();
}

public BigDecimal sumVendas() {
    TypedQuery<BigDecimal> query = entityManager.createQuery("SELECT SUM(v.valortotal) FROM Venda v", BigDecimal.class);
    return query.getSingleResult();
}

}
