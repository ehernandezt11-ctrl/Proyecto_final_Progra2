package com.mycompany.taller.controlador;

import com.mycompany.taller.dao.ClienteDao;
import com.mycompany.taller.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteControlador {
    
    @Autowired
    private ClienteDao clienteDao;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteDao.listar());
        model.addAttribute("cliente", new Cliente());
        return "clientes";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("cliente") Cliente cliente) {
        if (cliente.getIdCliente() > 0) {
                clienteDao.actualizar(cliente);
        }  else {
                clienteDao.agregar(cliente);
        }
            return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id) {
        clienteDao.eliminar(id);
        return "redirect:/clientes";
    }
    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, Model model) {
        Cliente cliente = clienteDao.buscarPorId(id);
        model.addAttribute("cliente", cliente);
        model.addAttribute("clientes", clienteDao.listar()); 
        return "clientes"; 
    }

    
}
