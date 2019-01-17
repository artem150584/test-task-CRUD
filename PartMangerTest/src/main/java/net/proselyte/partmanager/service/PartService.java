package net.proselyte.partmanager.service;

import net.proselyte.partmanager.model.Part;

import java.util.List;

public interface PartService {
    void addPart(Part part);

    void updatePart(Part part);

    void removePart(int id);

    Part getPartById(int id);

    List<Part> listParts();

    Part loadPartByPartName(String partName);
}
