package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.RepuestoDao;
import com.mycompany.taller.modelo.Repuesto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/repuestos")
public class RepuestoControlador {
    
    @Autowired
    private RepuestoDao repuestoDao;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("repuestos", repuestoDao.listar());
        model.addAttribute("repuesto", new Repuesto());
        return "repuestos";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("repuesto") Repuesto repuesto) {
        if (repuesto.getIdRepuesto() > 0) {
            repuestoDao.actualizar(repuesto);
        }else {
            repuestoDao.agregar(repuesto);
        }
        return "redirect:/repuestos";
    }
    

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        repuestoDao.eliminar(id);
        return "redirect:/repuestos";
    }
    
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Repuesto repuesto = repuestoDao.buscarPorId(id);
        model.addAttribute("repuesto", repuesto);
        model.addAttribute("repuestos", repuestoDao.listar()); 
        return "repuestos"; 
    }
    
    @PostMapping("/abastecer")
    public String abastecer(@RequestParam("idRepuesto") int idRepuesto, @RequestParam("cantidad") int cantidad ){
        repuestoDao.abastecer(idRepuesto, cantidad);
        return "redirect:/repuestos";
    }
    
    @GetMapping("/min_Stock")
    public String listaStock (Model model) {
        model.addAttribute("repuestos", repuestoDao.statusInventario(6));
        model.addAttribute("repuesto", new Repuesto());
        return "repuestos";
    }
    
    
}
