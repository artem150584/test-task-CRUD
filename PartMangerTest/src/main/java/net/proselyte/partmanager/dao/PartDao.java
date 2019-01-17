package net.proselyte.partmanager.dao;

import net.proselyte.partmanager.model.Part;

import java.util.List;

public interface PartDao {
    void addPart(Part part);

    void updatePart(Part part);

    void removePart(int id);

    Part getPartById(int id);

    List<Part> listParts();

    Part findPartByName(String partName);
}
