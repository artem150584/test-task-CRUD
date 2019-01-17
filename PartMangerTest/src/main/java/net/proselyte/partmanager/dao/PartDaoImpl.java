package net.proselyte.partmanager.dao;

import net.proselyte.partmanager.PartNameNotFoundException;
import net.proselyte.partmanager.model.Part;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PartDaoImpl implements PartDao {
    private static final Logger logger = LoggerFactory.getLogger(PartDaoImpl.class);

    private PartDao partDao;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addPart(Part part) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(part);
        logger.info("Part successfully saved. Part details: " + part);
    }

    @Override
    public void updatePart(Part part) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(part);
        logger.info("Part successfully update. Part details: " + part);
    }

    @Override
    public void removePart(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Part part = (Part) session.load(Part.class, new Integer(id));

        if (part != null) {
            session.delete(part);
        }
        logger.info("Part successfully removed. Part details: " + part);
    }

    @Override
    public Part getPartById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Part part = (Part) session.load(Part.class, new Integer(id));
        logger.info("Part successfully loaded. Part details: " + part);

        return part;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Part> listParts() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Part> partList = session.createQuery("from Part").list();

        for (Part part : partList) {
            logger.info("Part list: " + part);
        }

        return partList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Part findPartByName(String parName) {

        Part part = null;

        Session session = this.sessionFactory.getCurrentSession();
        try {
//            List<Part> partList = session.createQuery("FROM Part WHERE name = :partname").setString("partname", parName).list();
            List<Part> partList = session.createQuery("FROM Part WHERE name = :partname").setParameter("partname", parName).list();

            if (partList.size() == 0) {
                throw new PartNameNotFoundException("Sorry. Part: " + parName + " not found");
            } else part = partList.get(0);

        } catch (PartNameNotFoundException e) {
            e.printStackTrace();
        }
        logger.info("Part successfully find by Name. User details: " + part);

        return part;
    }
}
