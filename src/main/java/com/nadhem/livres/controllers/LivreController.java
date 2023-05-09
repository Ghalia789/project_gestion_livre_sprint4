package com.nadhem.livres.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nadhem.livres.entities.Genre;
import com.nadhem.livres.entities.Livre;
import com.nadhem.livres.service.GenreService;
import com.nadhem.livres.service.LivreService;

@Controller
public class LivreController {
	@Autowired
	LivreService livreService;
	
	@Autowired
	GenreService genreService;

	@GetMapping("/showCreate")
	public String showCreate(ModelMap model) {
		List<Genre> genres = genreService.getAllGenres();
		model.addAttribute("genres", genres);
		model.addAttribute("livre", new Livre());
		model.addAttribute("mode", "new");
		return "formLivre";
	}

	/*@PostMapping("/saveLivre")
	public String saveLivre(@ModelAttribute("livre")@Valid Livre livre,
	                         @RequestParam("datePublication") String datePublication,
	                         Model model,  BindingResult bindingResult) throws ParseException {

	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = formatter.parse(datePublication);
	    livre.setDatePublication(date);	    
	    if (bindingResult.hasErrors()) return "createLivre";
	    System.out.println(livre);
	    livreService.saveLivre(livre);
	    
	    return "redirect:listeLivres";
	}*/
	
	@PostMapping("/saveLivre")
	public String saveLivre(@ModelAttribute("livre") @Valid Livre livre,
	                         @RequestParam("genre") Long genreId,
	                         @RequestParam("datePublication") String datePublication,
	                         Model model, BindingResult bindingResult) throws ParseException {
	    Genre genre = genreService.getGenreById(genreId);
	    livre.setGenre(genre);

	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = formatter.parse(datePublication);
	    livre.setDatePublication(date);

	    if (bindingResult.hasErrors()) {
	        model.addAttribute("genres", genreService.getAllGenres());
	        return "formLivre";
	    }

	    livreService.saveLivre(livre);

	    return "redirect:/listeLivres";
	}




	@GetMapping("/listeLivres")
	public String listeLivres(ModelMap modelMap, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {
		Page<Livre> livres = livreService.getAllLivresParPage(page, size);
		modelMap.addAttribute("livres", livres);
		modelMap.addAttribute("pages", new int[livres.getTotalPages()]);
		modelMap.addAttribute("currentPage", page);
		modelMap.addAttribute("size", size);
		return "listeLivres";
	}

	@GetMapping("/supprimerLivre")
	public String supprimerLivre(@RequestParam("id") Long id, ModelMap modelMap,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "3") int size) {
		livreService.deleteLivreById(id);
		Page<Livre> livres = livreService.getAllLivresParPage(page, size);
		modelMap.addAttribute("livres", livres);
		modelMap.addAttribute("pages", new int[livres.getTotalPages()]);
		modelMap.addAttribute("currentPage", page);
		modelMap.addAttribute("size", size);
		return "listeLivres";
	}

	@GetMapping("/modifierLivre")
	public String editerLivre(@RequestParam("id") Long idLivre, ModelMap modelMap) {
	    Livre livre = livreService.getLivre(idLivre);
	    modelMap.addAttribute("mode", "edit");
	    modelMap.addAttribute("livre", livre);
	    modelMap.addAttribute("genres", genreService.getAllGenres());
	    return "formLivre";
	}

	@PostMapping("/updateLivre")
	public String updateLivre(@ModelAttribute("livre") Livre livre, BindingResult result,
			@RequestParam("datePublication") String datePublication,
	        @RequestParam("genreId") Long genreId, ModelMap modelMap) throws ParseException {

	    if (result.hasErrors()) {
	        return "formLivre";
	    }

	    // Récupération du genre
	    Genre genre = genreService.getGenreById(genreId);
	    livre.setGenre(genre);
	    
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = formatter.parse(datePublication);
	    livre.setDatePublication(date);

	    livreService.saveLivre(livre);
	    String msg = "Livre modifié avec Id " + livre.getIdLivre();
	    modelMap.addAttribute("msg", msg);
	    return "redirect:listeLivres";
	}


}