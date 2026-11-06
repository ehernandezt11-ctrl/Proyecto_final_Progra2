package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.ServicioDao;
import com.mycompany.taller.modelo.Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicios")
public class ServicioControlador {
    @Autowired
    private ServicioDao servicioDao;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("servicios", servicioDao.listar());
        model.addAttribute("servicio", new Servicio());
        return "servicios";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("servicio") Servicio servicio) {
        if (servicio.getIdServicio() > 0){
            servicioDao.actualizar(servicio);
        }else{
            servicioDao.agregar(servicio);
        }
        return "redirect:/servicios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        servicioDao.eliminar(id);
        return "redirect:/servicios";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Servicio servicio = servicioDao.buscarPorId(id);
        model.addAttribute("servicio", servicio);
        model.addAttribute("servicios", servicioDao.listar());
        return "servicios";
    }
    
    
}
