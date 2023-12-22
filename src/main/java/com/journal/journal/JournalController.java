package com.journal.journal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JournalController {
    @Autowired
    private JournalRepository journalRepository;
    
    @GetMapping
    public String getIndex(Model model) {

        
        List<Journal> sortedEntries = journalRepository.findAllByOrderByDateDesc(); 

        model.addAttribute("journalEntries", sortedEntries);
        

        return "index";
    }

    @PostMapping("/new-entry")
        public String addNew(
        @RequestParam("entryTitle") String entryTitle,
        @RequestParam("entryText") String entryText,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) 
        @RequestParam(value = "date", required = false) LocalDateTime date) {

        System.out.println("Ny entry från form: " + entryTitle);
        Journal journal = new Journal();

        journal.setTitle(entryTitle);
        journal.setText(entryText);
        journal.setDate(date != null ? date : LocalDateTime.now());

        journalRepository.save(journal);

    return "redirect:/";
}

    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        
        System.out.println("delete mapping: " + id);
        journalRepository.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam int id, Model model) {
    Optional<Journal> optionalJournal = journalRepository.findById(id);
    if (optionalJournal.isPresent()) {
        model.addAttribute("journal", optionalJournal.get());
        return "edit";
    } else {
        System.out.println("Hittades ej");
        return "redirect:/";
    }
}

    @PostMapping("/save-entry")
    public String saveEntry(@ModelAttribute Journal journal) {
    
    if (journal.getId() != 0) {
        Journal existingJournal = journalRepository.findById(journal.getId()).orElse(null);
        if (existingJournal != null) {
            existingJournal.setTitle(journal.getTitle());
            existingJournal.setText(journal.getText());
            existingJournal.setDate(journal.getDate());
            journalRepository.save(existingJournal);
        }
    } else {
        journalRepository.save(journal);
    }

    return "redirect:/";
}

    
}
