package com.laioffer.jupiter.dao;

import com.laioffer.jupiter.entity.db.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;

@Repository
//本质是一个@Component
public class RegisterDao {
    @Autowired
    private SessionFactory sessionFactory;

    public boolean register(User user) {
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            //关系型数据库中：
            // petition-> A+B，如果有一个table fail，能够rollback（把执行成功的operation撤销，重新让AB都退回上个状态）
            //保证了原子性
            session.save(user);
            session.getTransaction().commit();
            //commit()去数据库执行operation；transaction这里是保证原子性的
        } catch (PersistenceException | IllegalStateException ex) {
            // if hibernate throws this exception, it means the user already be register
            ex.printStackTrace();
            session.getTransaction().rollback();
            return false;
        } finally {
            if (session != null) session.close();
            //session被用过了
        }
        return true;
    }
}
