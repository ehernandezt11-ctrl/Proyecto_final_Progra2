package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.MecanicoDao;
import com.mycompany.taller.modelo.Mecanico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mecanicos")
public class MecanicoControlador {
    
    @Autowired
    private MecanicoDao mecanicoDao;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("mecanicos", mecanicoDao.listar());
          model.addAttribute("mecanico", new Mecanico());
        return "mecanicos";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("mecanico") Mecanico mecanico) {
        if(mecanico.getIdMecanico() > 0) {
            
        }else {
                mecanicoDao.agregar(mecanico);
        }        
        return "redirect:/mecanicos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        mecanicoDao.eliminar(id);
        return "redirect:/mecanicos";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Mecanico mecanico = mecanicoDao.buscarPorId(id);
        model.addAttribute("mecanico", mecanico);
        model.addAttribute("mecanicos", mecanicoDao.listar()); 
        return "mecanicos"; 
    }
    
    
    
    
}
