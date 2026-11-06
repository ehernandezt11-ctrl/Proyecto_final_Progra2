package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.ClienteDao;
import com.mycompany.taller.dao.VehiculoDao;
import com.mycompany.taller.modelo.Vehiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoControlador {
    @Autowired
    
    private VehiculoDao vehiculoDao;
    
    @Autowired
    private ClienteDao clienteDao;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vehiculos", vehiculoDao.listar());
        model.addAttribute("vehiculo", new Vehiculo());
        model.addAttribute("clientes", clienteDao.listar());
     
        return "vehiculos";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("vehiculo") Vehiculo vehiculo) {
        if(vehiculo.getIdVehiculo() == null ||  vehiculo.getIdVehiculo() == 0 ) {
            vehiculoDao.agregar(vehiculo);
        }else {
            vehiculoDao.actualizar(vehiculo);
        }
        return "redirect:/vehiculos";
    }
    
    

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        vehiculoDao.eliminar(id);
        return "redirect:/vehiculos";
    }
    
    @GetMapping("/editar/{id}")
    public String editar (@PathVariable int id, Model model) {
        Vehiculo vehiculo = vehiculoDao.buscarPorId(id);
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("vehiculos", vehiculoDao.listar());
        model.addAttribute("clientes", clienteDao.listar());
        return"vehiculos";
    }
    
}
