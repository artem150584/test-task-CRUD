package net.proselyte.partmanager.controller;

import net.proselyte.partmanager.model.Part;
import net.proselyte.partmanager.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PartController {
    private PartService partService;
    private int readyForAssembly = 0;
    private boolean isFilteres = false;
    private String filter = "all";

    private Part partFound = null;
    private int pages = 0;
    private int currentPage = 1;
    private boolean filterChanged = false;
    private List<Part> listPerPage = new ArrayList<Part>();

    @Autowired(required = true)
    @Qualifier(value = "partService")
    public void setPartService(PartService partService) {
        this.partService = partService;
    }

    @RequestMapping(value = "parts", method = RequestMethod.GET)
    public String listParts(Model model, Integer page) {
        setPages(model, page);

        readyForAssembly = getComputerNumber();

        model.addAttribute("part", new Part());
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("ready", readyForAssembly);
        model.addAttribute("filter", filter);
        model.addAttribute("partFound", partFound);

        return "parts";
    }

    @RequestMapping(value = "/parts/add", method = RequestMethod.POST)
    public String addPart(@ModelAttribute("part") Part part) {
        if (part.getId() == 0) {
            this.partService.addPart(part);
        } else {
            this.partService.updatePart(part);
        }

        return "redirect:/parts";
    }

    @RequestMapping("/remove/{id}")
    public String removePart(@PathVariable("id") int id) {
        this.partService.removePart(id);

        return "redirect:/parts";
    }

    @RequestMapping("edit/{id}")
    public String editPart(@PathVariable("id") int id, Model model, Integer page) {
        setPages(model, page);
        model.addAttribute("part", partService.getPartById(id));
        model.addAttribute("listParts", partService.listParts());

        model.addAttribute("listParts", listPerPage);

        return "parts";
    }

    @RequestMapping("partdata/{id}")
    public String partData(@PathVariable("id") int id, Model model) {
        model.addAttribute("part", partService.getPartById(id));

        return "partdata";
    }

    @RequestMapping(value = "/parts/filter", method = RequestMethod.POST)
    public String Sort(@ModelAttribute("filter") String filter) {
        this.filter = filter;
        isFilteres = true;
        return "redirect:/parts";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchPart(HttpServletRequest request, Model model) {
        String partName = request.getParameter("partname");
        System.out.println("Partname in controller: " + partName);
        Part part = partService.loadPartByPartName(partName);
        String result;
        if (part == null) {
            result = "notfoundpart";
        } else {
            result = "partdata";
        }

        model.addAttribute("part", part);

        return result;
    }


    private int getComputerNumber() {
        List<Part> parts = partService.listParts();
        List<Part> requaredParts = new ArrayList<Part>();
        for (Part part : parts) {
            if (part.isNecessary()) {
                requaredParts.add(part);
            }
        }
        int amount = requaredParts.get(0).getQuantity();
        for (Part part : requaredParts) {
            if (amount > part.getQuantity()) {
                amount = part.getQuantity();
            }
        }
        return amount;
    }

    private void setPages(Model model, Integer page) {
        List<Part> allParts = partService.listParts();
        List<Part> displeyedParts = new ArrayList<Part>();
        if (partFound != null) {
            pages = 1;
            currentPage = 1;
            displeyedParts.clear();
            displeyedParts.add(partFound);
        } else if (filter.equals("all")) displeyedParts = allParts;
        else if (filter.equals("required")) {
            displeyedParts.clear();
            for (Part part : allParts) {
                if (part.isNecessary()) {
                    displeyedParts.add(part);
                }
            }
            if (filterChanged) {
                currentPage = 1;
                filterChanged = false;
            }
        } else if (filter.equals("optional")) {
            displeyedParts.clear();
            for (Part part : allParts) {
                if (!part.isNecessary()) {
                    displeyedParts.add(part);
                }
            }
            if (filterChanged) {
                currentPage = 1;
                filterChanged = false;
            }
        }

        if (partFound == null) {
            pages = displeyedParts.size() % 10 == 0 ? displeyedParts.size() / 10 : displeyedParts.size() / 10 + 1;
        }
        listPerPage = displeyedParts.subList((currentPage - 1) * 10, (currentPage) * 10 < displeyedParts.size() ? (currentPage) * 10 : displeyedParts.size());

        List<Part> parts = displeyedParts;
        PagedListHolder<Part> pagedListHolder = new PagedListHolder<Part>(parts);
        pagedListHolder.setPageSize(10);
        model.addAttribute("maxPages", pagedListHolder.getPageCount());

        if (page == null || page < 1 || page > pagedListHolder.getPageCount()) {
            page = 1;
        }

        model.addAttribute("page", page);

        if (page > pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page);
        } else if (page <= pagedListHolder.getPageCount()) {
            pagedListHolder.setPage(page - 1);
        }

        model.addAttribute("listParts", pagedListHolder.getPageList());
    }
}

