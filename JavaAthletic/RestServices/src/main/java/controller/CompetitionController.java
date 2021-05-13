package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rest_domain.Competition;
import rest_repo.ICompetitionRepository;

@RestController
@RequestMapping("/athletic/competitions")
public class CompetitionController {
    @Autowired
    private ICompetitionRepository compRepo;

    @RequestMapping(method = RequestMethod.GET)
    public Competition[] getAll() {
        System.out.println("Method getAll() called");
        return compRepo.getAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Long id) {
        System.out.println("Method getById() called");
        Competition competition = compRepo.getById(id);
        if (competition == null) {
            return new ResponseEntity<>("Competition not found", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(competition, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Competition save(@RequestBody Competition competition) {
        System.out.println("Method save() called");
        compRepo.save(competition);
        return competition;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Competition update(@RequestBody Competition competition) {
        System.out.println("Method update() called");
        compRepo.update(competition);
        return competition;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            System.out.println("Method delete() called");
            compRepo.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String competitionError(Exception ex) {
        return ex.getMessage();
    }
}
